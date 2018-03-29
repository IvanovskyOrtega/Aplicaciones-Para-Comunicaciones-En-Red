package src;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaFileObject;

public class Server {
    
    static ServerSocket s;
    
    // Input and Output streams
    static ObjectInputStream ois;
    static ObjectOutputStream oos;
    
    // Constants for petitions
    static final int CLOSE_CONNECTION = -1;
    static final int CREATE_DATABASE = 0;
    static final int DROP_DATABASE = 1;
    static final int USE_DATABASE = 2;
    static final int CREATE_TABLE = 3;
    static final int DROP_TABLE = 4;
    static final int INSERT = 5;
    static final int SHOW_DATABASES = 6;
    static final int SHOW_TABLES = 7;
    static final int UPDATE = 8;
    static final int DELETE = 9;
    static final int SELECT = 10;
    static final int UNKNOWN_PETITION = 11;
    static final int SUCCESS = 0;
    static final int FAILURE = 1;
    
    // Current database in use
    static HashMap<String,LinkedList> currentDatabase;
    static HashMap<String,HashMap<String,LinkedList>> databases = new HashMap<String,HashMap<String,LinkedList>>();
    
    public static void main(String[] args){
        
        // Start the server in the 1234 port
        try{
            
            int port = 1234;
            s = new ServerSocket(port);
            System.out.println("Starting the service...");
            System.out.println("Listening on the port "+port+".");
            while(true){
                
                // Waiting for connections
                System.out.println("Waiting for incoming connections...");
                Socket cl = s.accept();
                handleClient(cl);
                closeClientConnection(cl); 
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * This function closes the Input and Output socket streams and the socket
     * itself.
     **/
    static void closeClientConnection(Socket cl){
        
        try{
            
            oos.close();
            ois.close();
            cl.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * This function handles the current connection (client), receiving petitions
     * and evaluating them.
     **/
    static void handleClient(Socket cl){
        try{
            System.out.println("Client connected from "+cl.getInetAddress()+":"+cl.getPort());
            oos = new ObjectOutputStream(cl.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(cl.getInputStream());
            // Send the names of the databases to the client
            sendDatabases();
            int petition = ois.readInt();
            while(petition != CLOSE_CONNECTION){
                if(managePetition(petition) != 0){
                    oos.writeUTF("Error handling petition.");
                    oos.flush();
                }
                petition = ois.readInt();
            }
        }catch(Exception e){
            
        }
    }
    
    /**
     * This function send the names of the available databases in the server to
     * the client.
     **/
    static void sendDatabases(){
        
        try{
            
            // Set the path to the Databases folder
            File databases = new File("Databases/");
            // Get the files list
            String[] databasesList = databases.list();
            oos.writeInt(databasesList.length);
            oos.flush();
            
            // Send them to the client
            for(String database : databasesList){
                
                oos.writeUTF(database);
                oos.flush();
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * This function evaluates a petition of the client and returns its result.
     **/
    static int managePetition(int petition){
        
        try{
            
            switch(petition){
                
                case CREATE_DATABASE:
                    String request = ois.readUTF();
                    System.out.println("Request: "+request);
                    int result = createDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Database created successfully)");
                        oos.flush();
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(Database already exists.)");
                        oos.flush();
                    }
                    break;
                    
                case DROP_DATABASE:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = dropDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Database dropped successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The specified database doesn't exist.)");
                    }
                    oos.flush();
                    break;
                    
                case USE_DATABASE:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = useDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Using database)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The specified databases doens't exist.)");
                    }
                    oos.flush();
                    break;
                    
                case CREATE_TABLE:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = createTableClass(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Table created successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("Table already exists.");
                    }
                    oos.flush();
                    break;
                    
                case DROP_TABLE:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = createDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Database created successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("Database already exists.");
                    }
                    oos.flush();
                    break;
                    
                case INSERT:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = createDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Database created successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("Database already exists.");
                    }
                    oos.flush();
                    break;
                    
                case SHOW_DATABASES:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    ArrayList<String> names = getDatabasesNames();
                    oos.writeInt(SUCCESS);
                    oos.flush();
                    oos.writeObject(names);
                    oos.flush();
                    break;
                    
                case SHOW_TABLES:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = createDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Database created successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("Database already exists.");
                    }
                    oos.flush();
                    break;
                    
                case UPDATE:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = createDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Database created successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("Database already exists.");
                    }
                    oos.flush();
                    break;
                    
                case DELETE:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = createDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Database created successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("Database already exists.");
                    }
                    oos.flush();
                    break;
                    
                case SELECT:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = createDatabase(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Database created successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("Database already exists.");
                    }
                    oos.flush();
                    break;
                    
                default:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    oos.writeInt(FAILURE);
                    oos.flush();
                    oos.writeUTF("Bad request.");
                    oos.flush();
                    break;
                
            }
            
        }catch(Exception e){
            
        }
        
        return 0;
        
    }

    static int createDatabase(String request) {
        
        System.out.println("Creating database");
        // Get the db_name
        String[] tokens = request.split(" ");
        String db_name = tokens[tokens.length-1].replace(";", "");
        
        if(databases.isEmpty() || !databases.containsKey(db_name)){
            
            // Add the database to the server databases
            HashMap<String,LinkedList> tables = new HashMap<String,LinkedList>();
            databases.put(db_name, tables);
            System.out.println("The database: "+db_name+" has been added.");
            return 0;
            
        }

        return -1;
    }

    static int dropDatabase(String request) {
        
        // Get the db_name
        String[] tokens = request.split(" ");
        String db_name = tokens[tokens.length-1].replace(";", "");
        
        // Check if the database exists
        if(!databases.containsKey(db_name) || databases.isEmpty()){
            return 1;
        }
        
        // Remove it from the databases
        databases.remove(db_name);
        return 0;
        
    }

    static int useDatabase(String request) {
        
        //Get the database name
        String[] tokens = request.split(" ");
        String db_name = tokens[tokens.length-1].replace(";", "");
        
        // Check if the database exists
        if(!databases.containsKey(db_name) || databases.isEmpty())
            return 1;
        
        // Set the current database in use
        currentDatabase = databases.get(db_name);
        return 0;
        
    }

    static ArrayList<String> getDatabasesNames() {
        
        ArrayList<String> names = new ArrayList<String>();
        for(String key : databases.keySet())
            names.add(key);
        
        return names;
        
    }

    static int createTableClass(String request) {
        
        // If there's not database in use
        if(currentDatabase == null){
            return 1;
        }
        
        // Get the simple table structure without the SQL instructions
        String table = request.substring(13,request.length()-1);
        
        // Get just the table name
        String table_name = "";
        int i = 0;
        
        while(table.charAt(i) != '('){
            table_name += table.charAt(i);
            i++;
        }
        
        try {
            // Read the Java File generated by PLY.
            File file = new File("GeneratedClasses/"+table_name+".java");
            StringBuilder fileContents = new StringBuilder((int)file.length());
            Scanner scanner;
            scanner = new Scanner(file);
            String lineSeparator = System.getProperty("line.separator");
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            
            scanner.close();
            String java_class = fileContents.toString();
            int res = compileClass(java_class, table_name);
            
            if(res != 0)
                return 1;
            
            LinkedList<Object> t = new LinkedList<>();
            currentDatabase.put(table_name, t);
            
            return 0;
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 1;
    }

    static int compileClass(String java_class, String table_name) {
        DynamicCompiler dc = new DynamicCompiler();
        //1.Construct an in-memory java source file from your dynamic code
        JavaFileObject file = dc.getJavaFileObject(java_class,table_name);
        Iterable<? extends JavaFileObject> files = Arrays.asList(file);
 
        //2.Compile your files by JavaCompiler
        dc.compile(files);
        //3.Load your class by URLClassLoader, then instantiate the instance, and call method by reflection
        //runIt("Tabla 1","","");
        System.out.println("fin del programa..");
        return 0;
    }
    
}
