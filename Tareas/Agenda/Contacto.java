import java.io.Serializable;
import java.util.Calendar;

public class Contacto implements Serializable {
	
	String nombre;
	String telefono;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	int edad;
	Calendar fecha;
	
	Contacto(String n, String t, int e, Calendar f){
		this.nombre = n;
		this.telefono = t;
		this.edad = e;
		this.fecha = f;
	}
	
	Contacto(){
		
	}
	
	
	
}
