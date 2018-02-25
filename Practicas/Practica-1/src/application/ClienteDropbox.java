import java.net.*;
import java.io.*;
import javax.swing.JFileChooser;
import java.util.*;

public class ClienteDropbox{

    static DataInputStream dis1;
    static DataOutputStream dos;
    static String[] nombres;
    static long[] tamanios;
    static String[] paths;
    static JFileChooser jf;
	static File[] f;
	static Scanner entrada=new Scanner(System.in);

	public static void main(String[] args){

		try{

			System.out.println("Elige una opción: \n1-Subir Archivos\n2-Bajar Archivos");
			int opcion = entrada.nextInt();

			int puerto = 7777;
			String dir = "127.0.0.1";
			Socket cl = new Socket(dir,puerto);
			System.out.println("Conexion establecida");
			dis1 = new DataInputStream(cl.getInputStream());
			dos = new DataOutputStream(cl.getOutputStream());
			dos.writeInt(opcion);
			dos.flush();

			switch(opcion){

				case 1:
					jf = new JFileChooser();
					jf.requestFocus();
					jf.setMultiSelectionEnabled(true);
					int r = jf.showOpenDialog(null);

					if(r == JFileChooser.APPROVE_OPTION){
						f = jf.getSelectedFiles();

						dos.writeInt(f.length);
						dos.flush();

						int i = 0;

						while(i < f.length){
							transmitirArchivo(f[i].length(),f[i].getAbsolutePath(),f[i].getName());
							i++;
						}
					}
					break;
				case 2:
					
					//System.out.println("Opcíón dos :)");
					System.out.print("\033[H\033[2J");	
					int status=dis1.readInt();
					if(status == -1){
						System.out.println("No hay archivos para descargar");
						break;
					}


					int noArchivos = dis1.read();
					//System.out.println("no archivos que recibiré:"+noArchivos);

					String nombre;
					for (int i=0;i<noArchivos;i++){
						nombre=dis1.readUTF();
						System.out.println((i+1)+".- "+nombre);
					}

					System.out.println("Número de archivo a descargar: ");
					int a= entrada.nextInt();
					dos.writeInt(a);
					dos.flush();
					recibirArchivos();

					break;
			}

			dos.close();
			dis1.close();
			cl.close();

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	 static void recibirArchivos(){
        try{
            String nombre = dis1.readUTF();
            long tam = dis1.readLong();
            System.out.println("Recibiendo el archivo: "+nombre);
            long recibidos = 0;
            int n = 0, porcentaje = 0;
            DataOutputStream dos2 = new DataOutputStream(new FileOutputStream(nombre));

            while(recibidos < tam){

                byte[] b = new byte[1500];
                n = dis1.read(b);
                recibidos += n;
                dos2.write(b,0,n);
                dos2.flush();
                porcentaje = (int)((recibidos*100)/tam);
                System.out.print("\rRecibido el "+porcentaje+"%");

            }

            System.out.println();

            dos2.close();
        
        }catch(Exception e){
            e.printStackTrace();
        }

    }

	static void transmitirArchivo(long tam, String path, String nombre){

		try{
			dos.writeUTF(nombre);
			dos.flush();
			dos.writeLong(tam);
			dos.flush();
			System.out.println("Transmitiendo el archivo: "+nombre);
			int n = 0, porcentaje = 0;
			long enviados = 0;

			DataInputStream dis2 = new DataInputStream(new FileInputStream(path));
			byte[] b = new byte[1500];

			while(enviados < tam){

				n = dis2.read(b);
				enviados += n;
				dos.write(b,0,n);
				dos.flush();
				porcentaje = (int)((enviados*100)/tam);
				System.out.print("\rTransmitiendo el "+porcentaje+"% del archivo");

			}
			Thread.currentThread().sleep(1000);
			System.out.println();
			dis2.close();

		}catch(Exception e){
			e.printStackTrace();
		}

		return;

	}

}