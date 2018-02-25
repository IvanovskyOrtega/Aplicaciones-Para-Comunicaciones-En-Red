import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;

public class Servidor {

	static ArrayList<Contacto> contactos = new ArrayList<>();
	static ServerSocket s;
	static Socket cl;
	static ObjectInputStream ois;
	static ObjectOutputStream oos;
	
	public static void main(String[] args) {

		inicializarAgenda();
		
		try {
			
			s = new ServerSocket(1234);
			System.out.println("Servidor iniciado... Esperando clientes...");
			
			while(true) {
				
				cl = s.accept();
				System.out.println("Cliente conectado desde: "+cl.getInetAddress()+":"+cl.getPort());
				oos = new ObjectOutputStream(cl.getOutputStream());
				ois = new ObjectInputStream(cl.getInputStream());
				oos.flush();
				char op = '0';
				String nombre = "";
				
				while(op != '6') {

					enviarCumpleanios();
					op = ois.readUTF().charAt(0);
					System.out.println("Codigo de peticion: "+op);
					
					switch(op) {
					
					case '1':
						enviarContactos();
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
						System.out.println("Peticion invalida.");
				
					}
					
				}
				
				oos.close();
				ois.close();
				cl.close();
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static void inicializarAgenda() {
		
		Calendar hoy = Calendar.getInstance();
		Calendar f1 = new GregorianCalendar(1997,0,25);
		Contacto c1 = new Contacto("Andres","5517141819",hoy.get(Calendar.YEAR)-f1.get(Calendar.YEAR),f1);
		Calendar f2 = new GregorianCalendar(2000,1,24);
		Contacto c2 = new Contacto("Luis","5584847454",hoy.get(Calendar.YEAR)-f2.get(Calendar.YEAR),f2);
		Calendar f3 = new GregorianCalendar(1950,2,4);
		Contacto c3 = new Contacto("Roxana","5598968487",hoy.get(Calendar.YEAR)-f3.get(Calendar.YEAR),f3);
		Calendar f4 = new GregorianCalendar(1986,3,5);
		Contacto c4 = new Contacto("Teresa","5536251471",hoy.get(Calendar.YEAR)-f4.get(Calendar.YEAR),f4);
		Calendar f5 = new GregorianCalendar(1997,4,15);
		Contacto c5 = new Contacto("Rosa","5531616487",hoy.get(Calendar.YEAR)-f5.get(Calendar.YEAR),f5);
		Calendar f6 = new GregorianCalendar(1997,5,9);
		Contacto c6 = new Contacto("Fatima","5598949768",hoy.get(Calendar.YEAR)-f6.get(Calendar.YEAR),f6);
		Calendar f7 = new GregorianCalendar(1997,6,16);
		Contacto c7 = new Contacto("Jennifer","5564676869",hoy.get(Calendar.YEAR)-f7.get(Calendar.YEAR),f7);
		Calendar f8 = new GregorianCalendar(1997,7,23);
		Contacto c8 = new Contacto("Nancy","5521030608",hoy.get(Calendar.YEAR)-f8.get(Calendar.YEAR),f8);
		Calendar f9 = new GregorianCalendar(1997,8,11);
		Contacto c9 = new Contacto("Pedro","5574809463",hoy.get(Calendar.YEAR)-f9.get(Calendar.YEAR),f9);
		contactos.add(c1);
		contactos.add(c2);
		contactos.add(c3);
		contactos.add(c4);
		contactos.add(c5);
		contactos.add(c6);
		contactos.add(c7);
		contactos.add(c8);
		contactos.add(c9);
		return;
		
	}
	
	static void enviarCumpleanios() {
		
		try {
			
			if(contactos.size() == 0) {
				oos.writeInt(0);
				oos.flush();
				return;
			}
			
			oos.writeInt(1);
			oos.flush();
			Calendar hoy = Calendar.getInstance();
			
			ArrayList<String> cumpleanios = new ArrayList<>();
			
			for(Contacto c : contactos) {
				
				int dia = c.getFecha().get(Calendar.DAY_OF_MONTH)-hoy.get(Calendar.DAY_OF_MONTH);
				int mes = c.getFecha().get(Calendar.MONTH)-hoy.get(Calendar.MONTH);
				
				if(dia == 0 && mes == 0) {
					
					cumpleanios.add(c.getNombre());
					
				}
				
			}
			
			if(cumpleanios.isEmpty()) {
				
				oos.writeInt(0);
				oos.flush();
				return;
				
			}
			
			oos.writeInt(1);
			oos.flush();
			oos.writeObject(cumpleanios);
			oos.flush();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	static void enviarContactos() {
		
		try {
			if(contactos.isEmpty()) {
				
				oos.writeInt(0);
				oos.flush();
				return;
				
			}

			ArrayList<Contacto> c = new ArrayList<>();
			c.addAll(0, contactos);
			oos.writeInt(1);
			oos.flush();
			oos.writeObject(c);
			oos.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return;
		
	}
	
	static void agregarContacto() {
		
		try {
			
			Contacto c = (Contacto)ois.readObject();
			//System.out.println("Recibi el objecto con los datos: ");
			//System.out.println("Nombre: "+c.getNombre()+"\nTelefono: "+c.getTelefono()+"\nEdad: "+c.getEdad());
			contactos.add(c);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static void buscarContacto() {
		
		try {
			
			String nombre = ois.readUTF();
			
			for(Contacto c : contactos) {
				
				if(c.getNombre().equals(nombre)) {

						oos.writeObject(c);
						oos.flush();
						return;
					
				}
				
			}
			
			oos.writeObject(null);
			oos.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	static void modificarContacto() {
		
		try {
			
			int index = ois.readInt();
			if(index > contactos.size() || index < 1) {
				
				oos.writeInt(0);
				oos.flush();
				return;
				
			}
			
			index--;
			oos.writeInt(1);
			oos.flush();
			Contacto nuevo = new Contacto();
			nuevo.setNombre(ois.readUTF());
			nuevo.setTelefono(ois.readUTF());
			nuevo.setEdad(ois.readInt());
			nuevo.setFecha((Calendar)ois.readObject());
			contactos.set(index, nuevo);
			return;
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	static void eliminarContacto() {
		
		try {
			
			String nombre = ois.readUTF();
			
			for(Contacto c : contactos) {
				
				if(c.getNombre().equals(nombre)) {

						oos.writeInt(1);
						oos.flush();
						contactos.remove(c);
						return;
					
				}
				
			}
			
			oos.writeInt(0);
			oos.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
		
	}

}
