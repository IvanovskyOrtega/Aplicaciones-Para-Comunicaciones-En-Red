import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Cliente {

	static Socket cl;
	static ObjectInputStream ois;
	static ObjectOutputStream oos;
	static BufferedReader br;
	static int puerto = 1234;
	static String dir = "127.0.0.1";
	
	
	public static void main(String[] args) {
		
		char op = '0';
		
		iniciarConexion();
		
		try {
			
			
			while(op != '6') {
				
				mostrarCumpleanios();
				menu();
				op = br.readLine().charAt(0);
				oos.writeUTF(op+"");
				oos.flush();
				
				switch(op) {
				
					case '1':
						verContactos();
						break;
					case '2':
						agregarContacto();
						break;
					case '3':
						buscarContacto();
						break;
					case '4':
						modificarContacto();
						break;
					case '5':
						eliminarContacto();
						break;
					case '6':
						break;
					default:
						System.out.println("Opcion invalida.");
				
				}
				
			}
			
			cerrarConexion();
			
			
		}catch(IOException io) {
			io.printStackTrace();
		}

	}
	
	static void iniciarConexion() {
		
		try {
			
			cl = new Socket(dir,puerto);
			oos = new ObjectOutputStream(cl.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(cl.getInputStream());
			System.out.println("Conexion establecida");
			br = new BufferedReader(new InputStreamReader(System.in));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return;
		
	}
	
	static void cerrarConexion() {
		try {
			
			ois.close();
			oos.close();
			br.close();
			cl.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	static void mostrarCumpleanios() {
		
		try {
			
			int res = ois.readInt();
			
			if(res == 0) {
				return;
			}
			
			res = ois.readInt();
			
			if(res == 0) {
				return;
			}
			
			ArrayList<String> cumpleanios = new ArrayList<>();
			cumpleanios = (ArrayList)ois.readObject();
			System.out.println();
			System.out.println("Hoy cumplen años:");
			
			for(String s : cumpleanios) {
				
				System.out.println("\t"+s);
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static void menu() {

		System.out.println();
		System.out.println("A   G   E   N   D   A");
		System.out.println("1) Ver contactos");
		System.out.println("2) Agregar contacto");
		System.out.println("3) Buscar contacto");
		System.out.println("4) Modificar contacto");
		System.out.println("5) Eliminar contacto");
		System.out.println("6) Salir");
		System.out.print("Ingrese la opcion: ");
		
		return;
		
	}
	
	static void verContactos() {
		try {
			
			int res = ois.readInt();
			
			if(res == 0) {
				
				System.out.println("No hay contactos :(");
				return;
				
			}
				
			ArrayList<Contacto> contactos = new ArrayList<Contacto>();
			contactos = (ArrayList)ois.readObject();
			
			System.out.println("ID\t:\tNombre");
			
			for(int i = 0; i< contactos.size(); i++) {
				
				System.out.println((i+1)+"\t:\t"+contactos.get(i).getNombre());
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	static void agregarContacto() {
		try {

			System.out.print("Nombre: ");
			String n = br.readLine();
			System.out.print("Telefono: ");
			String t = br.readLine();
			System.out.print("Edad: ");
			int e = Integer.parseInt(br.readLine());
			System.out.println("Fecha de nacimiento: ");
			System.out.print("\tDia: ");
			int d = Integer.parseInt(br.readLine());
			System.out.print("\tMes: ");
			int m = Integer.parseInt(br.readLine());
			System.out.print("\tAño: ");
			int a = Integer.parseInt(br.readLine());
			Calendar f = new GregorianCalendar();
			f.set(Calendar.DAY_OF_MONTH, d);
			f.set(Calendar.MONTH,m-1);
			f.set(Calendar.YEAR,a);
			Contacto c = new Contacto(n,t,e,f);
			oos.writeObject(c);
			oos.flush();
			
		}catch(IOException io) {
			io.printStackTrace();
		}
		
		return;
	}
	
	static void buscarContacto() {
		
		try {
			
			System.out.print("Busqueda por nombre: ");
			String n = br.readLine();
			oos.writeUTF(n);
			oos.flush();
			Contacto c = (Contacto)ois.readObject();
			
			if(c != null) {

				System.out.println("Se encontro : ");
				System.out.println("Nombre: "+c.getNombre()+"\nTelefono: "+c.getTelefono()+"\nEdad: "+c.getEdad());
			
			}
			else {
				System.out.println("No se encontraron coincidencias");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	static void modificarContacto() {
		
		try {
			
			System.out.print("Modificar por ID: ");
			int id = Integer.parseInt(br.readLine());
			oos.writeInt(id);
			oos.flush();
			int res = ois.readInt();
			
			if(res == 1) {

				System.out.println("Ingrese los nuevos datos:");
				System.out.print("Nombre: ");
				oos.writeUTF(br.readLine());
				oos.flush();
				System.out.print("Telefono: ");
				oos.writeUTF(br.readLine());
				oos.flush();
				System.out.print("Edad: ");
				oos.writeInt(Integer.parseInt(br.readLine()));
				oos.flush();
				System.out.println("Fecha de nacimiento: ");
				System.out.print("\tDia: ");
				int d = Integer.parseInt(br.readLine());
				System.out.print("\tMes: ");
				int m = Integer.parseInt(br.readLine());
				System.out.print("\tAño: ");
				int a = Integer.parseInt(br.readLine());
				Calendar f = new GregorianCalendar(a,m,d);
				oos.writeObject(f);
				oos.flush();
			
			}
			else {
				
				System.out.println("ID invalido");
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	static void eliminarContacto() {
		
		try {
			
			System.out.print("Eliminar por nombre: ");
			String n = br.readLine();
			oos.writeUTF(n);
			oos.flush();
			int res = ois.readInt();
			
			if(res == 1) {

				System.out.println("Se ha eliminado el contacto");
			
			}
			else {
				
				System.out.println("No se encontro coincidencia con ese nombre.");
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
		
	}

}
