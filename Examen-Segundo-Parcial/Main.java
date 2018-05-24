import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Main{

	public static HashMap<String, Boolean> queueHashMap = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> queueHashMap1 = new HashMap<String, Boolean>();

	public static void main(String[] args) {
	 	
	 	//valitation of parameters
	 	if(args.length < 2){
            System.out.println("Usage: javac Wget <url> <directory>");
            System.exit(1);
        }

        //geting index in order to make directory tree
       	try{
        	
        	Wget wget = new Wget(args[0],args[1],"index.html");
        	queueHashMap = wget.getHashMap();
	        Thread h1 = new Thread(wget);
	        h1.start();
	        h1.join();

	    }catch(InterruptedException ie){ie.printStackTrace();}

	    //making the directory tree
	   	for(HashMap.Entry<String, Boolean> hm : queueHashMap.entrySet())
	   		StringAnalysis(hm.getKey());

	   	//for(HashMap.Entry<String, Boolean> hm : queueHashMap1.entrySet())
	   	//	System.out.println(hm.getKey());

   		//getting files contents

	    //ExecutorService ex = Executors.newFixedThreadPool(10);
	    for(HashMap.Entry<String, Boolean> hm : queueHashMap1.entrySet()){
			//System.out.println("clave=" + hm.getKey() + ", valor=" + hm.getValue());

			Wget wget = new Wget(args[0],"/"+hm.getKey(),hm.getKey());
			//System.out.println("\t---------pidiento: "+args[0]+"/"+hm.getKey());
			//ex.execute(wget);
		  	Thread t1 = new Thread(wget);
		  	t1.start();
		  	
		  	try{
		  		t1.join();      
			}catch(Exception e){}

		}
		//ex.shutdown();

	}

	/*public static void main(String[] args) {
		StringAnalysis("portfolio.html#medianoplazo");
		for(HashMap.Entry<String, Boolean> hm : queueHashMap1.entrySet())
	   		System.out.println(hm.getKey());
	}*/

	public static void StringAnalysis(String nombre){
		System.out.println("Name: "+nombre);
		if(nombre.contains("http")||nombre.contains("{")||nombre.contains("("))
			return;
		else if(nombre.length() < 3)
			return;
		if(nombre.contains("/")){

			int i;
			for(i = nombre.length()-1 ; i >= 0 ; i--)
				
				if (nombre.charAt(i) == '/')
					break;

			createDirectory(nombre.substring(0,i));
			queueHashMap1.put(nombre,false);

		}
		else if(nombre.contains(".html#")){
			queueHashMap1.put(nombre.substring(0,nombre.lastIndexOf("#")),false);
		}
		else{
			queueHashMap1.put(nombre,false);
		}
	}

	public static void createDirectory(String path){
		File folder = new File(path);
		folder.mkdirs();
	}

}