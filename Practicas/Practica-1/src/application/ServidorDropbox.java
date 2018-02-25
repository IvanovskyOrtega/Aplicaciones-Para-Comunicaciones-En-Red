
import java.net.*;
import java.io.*;
import java.lang.Thread;

public class ServidorDropbox{
        
    static Socket cl;
    static DataOutputStream dos1;
    static DataInputStream dis;
    
    public static void main(String[] args){

        try{
            
            
            ServerSocket s = new ServerSocket(7777);
            System.out.println("Servido iniciado... Esperando clientes.");

            while(true){

                cl = s.accept();
                System.out.println("Cliente conectado desde: "+cl.getInetAddress()+":"+cl.getPort());
                dis = new DataInputStream(cl.getInputStream());
                dos1 = new DataOutputStream(cl.getOutputStream());
                int opcion = dis.readInt();

                switch(opcion){
                    case 1:
                        int numArchivos = dis.readInt();
                        int i = 0;
                        while(i < numArchivos){
                            recibirArchivos(); 
                            i++;  
                        }
                        dis.close();
                        dos1.close();
                        cl.close();
                        break;

                    case 2:
                        System.out.println("Mandando archivos diponibles para descarga");
                        File directorio = new File("archivos/");
                        String archivos[] = directorio.list();

                        if (archivos == null){
                            System.out.println("No hay ficheros en el directorio especificado");
                            dos1.writeInt(-1);
                            dos1.flush();
                            break;
                        }

                        dos1.writeInt(1);
                        dos1.flush();

                        dos1.write(archivos.length);
                        dos1.flush();
                        
                        //System.out.println(archivos.length);
                        for (int x=0;x < archivos.length;x++){
                            dos1.writeUTF(archivos[x]);
                            dos1.flush();
                        }

                        int a=dis.readInt();
                        File trans = new File("archivos/"+archivos[a-1]);

                        transmitirArchivo(trans.length(),trans.getAbsolutePath(),trans.getName());
                        dis.close();
                        dos1.close();
                        cl.close();
                        break;
                }
            }
        
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }

    static void transmitirArchivo(long tam, String path, String nombre){

        try{
            dos1.writeUTF(nombre);
            dos1.flush();
            dos1.writeLong(tam);
            dos1.flush();
            System.out.println("Transmitiendo el archivo: "+nombre);
            int n = 0, porcentaje = 0;
            long enviados = 0;

            DataInputStream dis2 = new DataInputStream(new FileInputStream(path));
            byte[] b = new byte[1500];

            while(enviados < tam){

                n = dis2.read(b);
                enviados += n;
                dos1.write(b,0,n);
                dos1.flush();
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

    static void recibirArchivos(){
        try{
            String nombre = dis.readUTF();
            long tam = dis.readLong();
            System.out.println("Recibiendo el archivo: "+nombre);
            long recibidos = 0;
            int n = 0, porcentaje = 0;
            DataOutputStream dos2 = new DataOutputStream(new FileOutputStream("archivos/"+nombre));

            while(recibidos < tam){

                byte[] b = new byte[1500];
                n = dis.read(b);
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

}