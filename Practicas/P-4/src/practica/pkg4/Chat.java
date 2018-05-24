import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Chat{
	
	public static Scanner in = new Scanner(System.in);
	public static ArrayList<String> users = new ArrayList<String>();

	public static void main(String[] args) {
		
		int pto = 1234;
		InetAddress grupo = null;
		String dir = "229.1.1.1";

		System.out.print("Usuario: ");
		String nombre = in.nextLine();

		//introducir todos los nombres a un arraylist del archivo

		try{
			File archivo = new File("users.txt");
			FileReader fr = new FileReader (archivo);

			BufferedReader br = new BufferedReader(fr);

			String u = null;
			while((u=br.readLine())!=null)
				users.add(u);
			users.add(nombre);
			fr.close();
			
			FileWriter fw = new FileWriter (archivo);
			PrintWriter pw = new PrintWriter(fw);
			for(String out : users){
				System.out.println("->"+out);
				pw.println(out);
			}
			fw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
                
                
		try{

			MulticastSocket ms = new MulticastSocket(pto);
			ms.setReuseAddress(true);
			ms.setTimeToLive(255);

			try{
				grupo = InetAddress.getByName(dir);
			}catch(UnknownHostException u){
				System.out.println("Dir no válida");
				System.exit(1);
			}

			ms.joinGroup(grupo);

	//		Enviar enviar = new Enviar(pto,grupo,ms,nombre,users);
		//	Recibir recibir = new Recibir(ms,nombre,users);
	//		enviar.start();
		//	recibir.start();

			//enviar.destroy();
			//recibir.destroy();

		}catch(Exception e){
			e.printStackTrace();
		}

	}
}