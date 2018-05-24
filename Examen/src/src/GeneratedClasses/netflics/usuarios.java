package netflics;

import java.io.Serializable;

public class usuarios  implements Serializable{
	public String idUsuario;
	public String nombreUsuario;
	public String email;
	public String contrasenia;
	public String idTarjeta;
	public Integer usuario_idPlan;
	public String get_idUsuario(){
		return this.idUsuario;
	}
	public String get_nombreUsuario(){
		return this.nombreUsuario;
	}
	public String get_email(){
		return this.email;
	}
	public String get_contrasenia(){
		return this.contrasenia;
	}
	public String get_idTarjeta(){
		return this.idTarjeta;
	}
	public Integer get_usuario_idPlan(){
		return this.usuario_idPlan;
	}
	public void set_idUsuario(String idUsuario){
		this.idUsuario = idUsuario;
	}
	public void set_nombreUsuario(String nombreUsuario){
		this.nombreUsuario = nombreUsuario;
	}
	public void set_email(String email){
		this.email = email;
	}
	public void set_contrasenia(String contrasenia){
		this.contrasenia = contrasenia;
	}
	public void set_idTarjeta(String idTarjeta){
		this.idTarjeta = idTarjeta;
	}
	public void set_usuario_idPlan(Integer usuario_idPlan){
		this.usuario_idPlan = usuario_idPlan;
	}
	public usuarios(){
}
	public usuarios(String idUsuario, String nombreUsuario, String email, String contrasenia, String idTarjeta, Integer usuario_idPlan){
		this.idUsuario = idUsuario;
		this.nombreUsuario = nombreUsuario;
		this.email = email;
		this.contrasenia = contrasenia;
		this.idTarjeta = idTarjeta;
		this.usuario_idPlan = usuario_idPlan;
	}
}