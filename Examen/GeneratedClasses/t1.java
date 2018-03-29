public class t1{
	char[] nombre;
	int edad;
	boolean casado;
	char[] get_nombre(){
		return this.nombre;
	}
	int get_edad(){
		return this.edad;
	}
	boolean get_casado(){
		return this.casado;
	}
	void set_nombre(char[] nombre){
		this.nombre = nombre;
	}
	void set_edad(int edad){
		this.edad = edad;
	}
	void set_casado(boolean casado){
		this.casado = casado;
	}
	public t1(char[] nombre, int edad, boolean casado){
		this.nombre = nombre;
		this.edad = edad;
		this.casado = casado;
	}
}