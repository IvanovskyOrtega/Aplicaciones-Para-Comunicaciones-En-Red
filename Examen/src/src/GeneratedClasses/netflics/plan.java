package netflics;

import java.io.Serializable;

public class plan  implements Serializable{
	public Integer idPlan;
	public String nombre;
	public Double precio;
	public Integer get_idPlan(){
		return this.idPlan;
	}
	public String get_nombre(){
		return this.nombre;
	}
	public Double get_precio(){
		return this.precio;
	}
	public void set_idPlan(Integer idPlan){
		this.idPlan = idPlan;
	}
	public void set_nombre(String nombre){
		this.nombre = nombre;
	}
	public void set_precio(Double precio){
		this.precio = precio;
	}
	public plan(){
}
	public plan(Integer idPlan, String nombre, Double precio){
		this.idPlan = idPlan;
		this.nombre = nombre;
		this.precio = precio;
	}
}