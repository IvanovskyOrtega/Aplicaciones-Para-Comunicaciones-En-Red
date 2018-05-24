package practica.pkg4;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Enviar extends Thread{
    
    public int pto;
    public InetAddress grupo;
    public MulticastSocket ms;
    public String msj;
    public String nombre;
    public String to;
    public ArrayList<String> users;
    public int tipo;

    public Enviar(int pto, InetAddress grupo, MulticastSocket ms,String nombre,ArrayList<String> users,String msj, int tipo){
        this.pto=pto;
        this.grupo=grupo;
        this.ms=ms;
        this.msj=msj;
        this.nombre=nombre;
        this.users=users;
        this.tipo=tipo;
    }
    public Enviar(int pto, InetAddress grupo, MulticastSocket ms,String nombre,ArrayList<String> users,String msj, int tipo,String to){
        this.pto=pto;
        this.grupo=grupo;
        this.ms=ms;
        this.msj=msj;
        this.nombre=nombre;
        this.users=users;
        this.tipo=tipo;
        this.to=to;
    }

    public void enviar_msj(){
        try{
            String msj1;
  
            if(tipo == 0)
                msj1 = "<publico><"+nombre+">";
            else
                msj1 = "<privado><"+nombre+"><"+to+">";
            msj1+=msj;
            
            //System.out.println(msj1);
            byte[] b= msj1.getBytes();
            DatagramPacket p = new DatagramPacket(b,b.length,grupo,pto);
            ms.send(p);
            try{
                Thread.currentThread().sleep(500);
            }catch(InterruptedException ie){}

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        System.out.println("ENVIAR INICIADO");
        enviar_msj();
    }
}