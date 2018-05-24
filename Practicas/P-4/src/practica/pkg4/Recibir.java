package practica.pkg4;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JEditorPane;

public class Recibir extends Thread{
    public MulticastSocket	ms;
    public DatagramPacket p;
    public String msj;
    public String nombre; 
    public ArrayList<String> users;
    public JEditorPane jtp;
    public ArrayList<String> emojisPath;

    public Recibir(MulticastSocket ms,String nombre,ArrayList<String> users,JEditorPane jtp,ArrayList<String> emojisPath){
        this.ms=ms;
        p=null;
        msj=null;
        this.nombre=nombre;
        this.users=users;
        this.jtp=jtp;
        this.emojisPath = emojisPath;
    }

    public int fin(String msj, int index){
        int i;
        for (i = index ; i <= msj.length() ; i++)
            if(msj.charAt(i)=='>')
                break;
        return i;
    }

	public void recibir_msj(){
		try{
                    while(true){
                        p=new DatagramPacket(new byte[65500],65500);
                        ms.receive(p);
                        msj = new String(p.getData(),0,p.getLength());
                        String tipo = msj.substring(1,8);
                        int fin1 = fin(msj,10);
                        String from = msj.substring(10,fin1);
                        String texto = jtp.getText();
                        String s1 = texto.substring(0, texto.length()-16);
                        String s2 = texto.substring(texto.length()-16, texto.length());
                        String finalMsg;
                        if(tipo.equals("privado")){
                            int fin2 = fin(msj,fin1+1);
                            String to = msj.substring(fin1+2,fin2);
                            System.out.println("to: "+to+" mi nombre: "+nombre);
                            finalMsg = msj.substring(fin2+1,msj.length());
                            replaceEmojis(finalMsg);
                            if(to.equals(nombre)){
                                try{
                                    jtp.setText(s1+"<br>Mensaje privado para ti "+nombre+" de "+from+": <div style='font: italic bold 12px/30px Georgia, serif;'>"+finalMsg+"</div>"+s2);
                                }catch(Exception e){

                                }
                            }
                        }
                        
                        else if(tipo.equals("publico")){
                            finalMsg = msj.substring(fin1+1,msj.length());
                            finalMsg = replaceEmojis(finalMsg);
                            System.out.println(finalMsg);
                            try{
                                jtp.setText(s1+"<p><span style='font: bold serif; color:#4682B4;'>"+from+":</span> <span style='font: serif;'>"+finalMsg+"</span></p>"+s2);
                            }catch(Exception e){

                            }
                        }
                        //jtp.
                        System.out.println("------------------------------");
                    }
                }catch(Exception e){
                       
                }
	}

    @Override
    public void run(){
        System.out.println("RECIBIR INICIADO");
        recibir_msj();
    }
    
    

    private String replaceEmojis(String msg) throws IOException {
        
        String finalMsg = msg.replaceAll("<alien>", emojisPath.get(0));
        finalMsg = finalMsg.replaceAll("<angel>", emojisPath.get(1));
        finalMsg = finalMsg.replaceAll("<anguished>", emojisPath.get(2));
        finalMsg = finalMsg.replaceAll("<crying>", emojisPath.get(3));
        finalMsg = finalMsg.replaceAll("<sad1>", emojisPath.get(4));
        finalMsg = finalMsg.replaceAll("<devil>", emojisPath.get(5));
        finalMsg = finalMsg.replaceAll("<dizzy>", emojisPath.get(6));
        finalMsg = finalMsg.replaceAll("<drooling>", emojisPath.get(7));
        finalMsg = finalMsg.replaceAll("<clown>", emojisPath.get(8));
        finalMsg = finalMsg.replaceAll("<cowboy>", emojisPath.get(9));
        finalMsg = finalMsg.replaceAll("<happy1>", emojisPath.get(10));
        finalMsg = finalMsg.replaceAll("<sad2>", emojisPath.get(11));
        finalMsg = finalMsg.replaceAll("<smile1>", emojisPath.get(12));
        finalMsg = finalMsg.replaceAll("<smirk>", emojisPath.get(13));
        finalMsg = finalMsg.replaceAll("<sunglasses>", emojisPath.get(14));
        finalMsg = finalMsg.replaceAll("<thinking>", emojisPath.get(15));
        finalMsg = finalMsg.replaceAll("<unamused>", emojisPath.get(16));
        finalMsg = finalMsg.replaceAll("<sad3>", emojisPath.get(17));
        finalMsg = finalMsg.replaceAll("<worried>", emojisPath.get(18));
        finalMsg = finalMsg.replaceAll("<surprised>", emojisPath.get(19));
        finalMsg = finalMsg.replaceAll("<nomouth>", emojisPath.get(20));
        finalMsg = finalMsg.replaceAll("<expless>", emojisPath.get(21));
        finalMsg = finalMsg.replaceAll("<fearful>", emojisPath.get(22));
        finalMsg = finalMsg.replaceAll("<flushed>", emojisPath.get(23));
        finalMsg = finalMsg.replaceAll("<frowning>", emojisPath.get(24));
        finalMsg = finalMsg.replaceAll("<ghost>", emojisPath.get(25));
        finalMsg = finalMsg.replaceAll("<grinmacing>", emojisPath.get(26));
        finalMsg = finalMsg.replaceAll("<happycat>", emojisPath.get(27));
        finalMsg = finalMsg.replaceAll("<happy2>", emojisPath.get(28));
        finalMsg = finalMsg.replaceAll("<hearteyes>", emojisPath.get(29));
        finalMsg = finalMsg.replaceAll("<hugging>", emojisPath.get(30));
        finalMsg = finalMsg.replaceAll("<hungry>", emojisPath.get(31));
        finalMsg = finalMsg.replaceAll("<kiss1>", emojisPath.get(32));
        finalMsg = finalMsg.replaceAll("<kiss2>", emojisPath.get(33));
        finalMsg = finalMsg.replaceAll("<kiss3>", emojisPath.get(34));
        finalMsg = finalMsg.replaceAll("<maddevil>", emojisPath.get(35));
        finalMsg = finalMsg.replaceAll("<money>", emojisPath.get(36));
        finalMsg = finalMsg.replaceAll("<neutral>", emojisPath.get(37));
        finalMsg = finalMsg.replaceAll("<tears>", emojisPath.get(38));
        finalMsg = finalMsg.replaceAll("<omg>", emojisPath.get(39));
        finalMsg = finalMsg.replaceAll("<liar>", emojisPath.get(40));
        finalMsg = finalMsg.replaceAll("<pskull>", emojisPath.get(41));
        finalMsg = finalMsg.replaceAll("<poison>", emojisPath.get(42));
        finalMsg = finalMsg.replaceAll("<poop>", emojisPath.get(43));
        finalMsg = finalMsg.replaceAll("<pumpkin>", emojisPath.get(44));
        finalMsg = finalMsg.replaceAll("<relieved>", emojisPath.get(45));
        finalMsg = finalMsg.replaceAll("<robot>", emojisPath.get(46));
        finalMsg = finalMsg.replaceAll("<rolleye>", emojisPath.get(47));
        finalMsg = finalMsg.replaceAll("<shy>", emojisPath.get(48));
        finalMsg = finalMsg.replaceAll("<sick1>", emojisPath.get(49));
        finalMsg = finalMsg.replaceAll("<sick2>", emojisPath.get(50));
        finalMsg = finalMsg.replaceAll("<sick3>", emojisPath.get(51));
        finalMsg = finalMsg.replaceAll("<skull>", emojisPath.get(52));
        finalMsg = finalMsg.replaceAll("<sleep>", emojisPath.get(53));
        finalMsg = finalMsg.replaceAll("<smile2>", emojisPath.get(54));
        finalMsg = finalMsg.replaceAll("<smile3>", emojisPath.get(55));
        finalMsg = finalMsg.replaceAll("<catsmile>", emojisPath.get(56));
        finalMsg = finalMsg.replaceAll("<smile4>", emojisPath.get(57));
        finalMsg = finalMsg.replaceAll("<smile5>", emojisPath.get(58));
        finalMsg = finalMsg.replaceAll("<smile6>", emojisPath.get(59));
        return finalMsg;
    }
}