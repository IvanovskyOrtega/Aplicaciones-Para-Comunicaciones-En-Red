import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import javax.lang.model.util.ElementScanner6;
import javax.swing.text.StyledEditorKit.BoldAction;

import java.io.*;

public class Wget implements Runnable{

    static File f;
    static KMP kmp = new KMP();
    static HashMap<String,Boolean> queueHashMap = new HashMap<>();
    static Set<String> completed;
    static String args0,args1;
    
    static String currentFileName;
    String dir;
    String hostname;
    URL url;
    HttpURLConnection connection; 
    DataInputStream dis;
    //BufferedReader br;

    public HashMap<String,Boolean> getHashMap(){
        return queueHashMap;
    }

    public void set(String url, String dir){
        try {
            if(!url.startsWith("http")){
                this.url = new URL("http://"+url+dir);
            }
            else{
                this.url = new URL(url+dir);
            }
            if(dir.equals("") || dir.equals("/"))
                currentFileName = "index.html";
            else if(dir.startsWith("/"))
                currentFileName = dir.substring(1,dir.length());
            else
                currentFileName = dir;
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public Wget(String url, String dir){

        try {

            set(url,dir);
            this.connection = (HttpURLConnection) this.url.openConnection();
            this.connection.setRequestMethod("GET");
            this.dis = new DataInputStream(this.connection.getInputStream());

        } catch (Exception e) {
            System.err.println("Failed to instantiate Wget");
        }

    }

    public Wget(String[] args){

        this.args0 = args[0];
        this.args1 = args[1];
        currentFileName = null;
    }

    public Wget(String url, String dir, String currentFileName){
        try {
            this.args0 = url;
        this.args1 = dir;
        this.currentFileName = currentFileName;
        set(url,dir);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    @Override
    public void run(){
        execution();
    }

    public void execution() {

        try {

            /*if(args.length < 2){
                System.out.println("Usage: javac Wget <url> <directory>");
                System.exit(1);
            }*/
            Wget wget = new Wget(args0, args1);
            wget.receive();
            //System.out.println(queueHashMap.toString());
            wget.dis.close();
            wget.connection.disconnect();
            //wget.br.close();
            //wget.sock.close();

        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    void receive(){
        try {
            int type;
            String contentType = this.connection.getContentType();
            if(contentType.contains("text/html")){
                type = 1;
            }
            else{
                type = 2;
            }
            System.out.println(contentType);
            System.out.println("Type: "+type);
            switch (type) {
                case 1:
                    reciveHTMLDocument();
                    break;
                default:
                    receiveFile();
                    break;
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    void reciveHTMLDocument(){
        
        try {
            
            receiveFile();
            lookForHrefAndSrc(currentFileName);

        } catch (Exception e) {
            System.err.println("Error receiving file");
        }
        
    }

    void lookForHrefAndSrc(String filename){

        try {
            
            FileInputStream fis = new FileInputStream(new File(filename));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while((line = br.readLine()) != null){
                //System.out.println(line);
                lookForHref(line);
                lookForSrc(line);
            }
            br.close();
            fis.close();

        } catch (Exception e) {
            //TODO: handle exception
        }

    }

    void receiveFile(){
        try {
            System.out.println("Filename: "+this.currentFileName);
            f = new File(this.currentFileName);
            FileOutputStream fos = new FileOutputStream(f);
            System.out.println("Download Started...");
            int readBytes;
            int read = 0;
            byte[] b = new byte[4096];

            while ((readBytes = this.dis.read(b,0,b.length)) != -1) {
                fos.write(b,0,readBytes);
                fos.flush();
                read += 1;
            }
            System.out.println("The file: "+this.currentFileName+" has been downloaded.");
            System.out.println(read+" bytes have been read.");
            fos.close();
            
        } catch (Exception e) {
            System.err.println("Receive failed");
        }
    }
 

    void lookForSrc(String line){

        try {
            ArrayList<Integer> indexes1 = kmp.KMPSearch("src=\"", line);
            ArrayList<Integer> indexes2 = kmp.KMPSearch("src='", line);

            for(Integer start : indexes1){
                String file = line.substring(start, line.length());
                int end = file.indexOf('\"');
                file = file.substring(0,end);
                queueHashMap.put(file, false);
            }

            for(Integer start : indexes2){
                String file = line.substring(start, line.length());
                int end = file.indexOf('\'');
                file = file.substring(0,end);
                queueHashMap.put(file, false);
            }

        } catch (Exception e) {
            System.err.println("lookForSrc() failed");
        }

    }

    void lookForHref(String line){

        try {

            ArrayList<Integer> indexes1 = kmp.KMPSearch("href=\"", line);
            ArrayList<Integer> indexes2 = kmp.KMPSearch("href='", line);

            for(Integer start : indexes1){
                String file = line.substring(start, line.length());
                int end = file.indexOf('\"');
                file = file.substring(0,end);
                queueHashMap.put(file, false);
            }

            for(Integer start : indexes2){
                String file = line.substring(start, line.length());
                int end = file.indexOf('\'');
                file = file.substring(0,end);
                queueHashMap.put(file, false);
            }

        } catch (Exception e) {
            System.err.println("lookForHref() failed");
        }
        
    }
}
