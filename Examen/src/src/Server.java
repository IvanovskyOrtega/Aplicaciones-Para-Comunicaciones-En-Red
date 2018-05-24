package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
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
    static String currentDatabaseName;
    static HashMap<String,HashMap<String,LinkedList>> databases = new HashMap<String,HashMap<String,LinkedList>>();
    
    public static void main(String[] args){
        
        // Start the server in the 1234 port
        try{
            
            // Dynamic classes folder
            File d_classes_folder = new File("build/classes/Databases/");
            if (!d_classes_folder.exists()) {
                if (d_classes_folder.mkdir()) {
                    System.out.println("Dynamic classes folder created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }
            
            // Databases folder
            File db_folder = new File("Databases/");
            if (!db_folder.exists()) {
                if (db_folder.mkdir()) {
                    System.out.println("Databases folder created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }
            
            // Generated Java Files from the dynamic classes
            File g_c_java_files = new File("src/src/GeneratedClasses/");
            if (!g_c_java_files.exists()) {
                if (g_c_java_files.mkdir()) {
                    System.out.println("Databases folder created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }
            
            int port = 1234;
            s = new ServerSocket(port);
            System.out.println("Starting the service...");
            System.out.println("Listening on the port "+port+".");
            while(true){
                loadDatabases();
                // Waiting for connections
                System.out.println("Waiting for incoming connections...");
                Socket cl = s.accept();
                handleClient(cl);
                closeClientConnection(cl); 
                writeDatabasesToFile();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * This function load th databases contained in their corresponding file.
     **/
    static void loadDatabases(){
        try{
            
            databases.clear();
            System.out.println("Loading databases...");
            File db_dir = new File("Databases/");
            String[] db_list = db_dir.list();
            
            if(db_list.length == 0){
                System.out.println("No databases to load.");
                return;
            }
            
            for(String db_filename : db_list){
                
                // Read the database from the input file
                FileInputStream fis = new FileInputStream("Databases/"+db_filename);
                ObjectInputStream db_reader = new ObjectInputStream(fis);
                HashMap<String,LinkedList> db = (HashMap<String,LinkedList>)db_reader.readObject();
                // Load it into the Database HashMap
                String db_name = db_filename.substring(0,db_filename.length()-3);
                databases.put(db_name, db);
                
                // Close File and Object input streams
                db_reader.close();
                fis.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * This function write each of the databases to its corresponding file
     * using a ObjectOutputStream.
     **/
    static void writeDatabasesToFile(){
        
        try{
            
            if(databases.isEmpty())
                return;
            
            Set<String> keys = databases.keySet();
            
            for(String key : keys){
                FileOutputStream fos = new FileOutputStream("Databases/"+key+".db");
                ObjectOutputStream db_writer = new ObjectOutputStream(fos);
                db_writer.writeObject(databases.get(key));
                db_writer.flush();
                db_writer.close();
                fos.close();
            }
            
        }catch(Exception e){
            
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
            Set<String> db = databases.keySet();
            oos.writeInt(db.size());
            oos.flush();
            
            // Send them to the client
            for(String database : db){
                
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
                        sendDatabases();
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
                        sendDatabases();
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
                        oos.flush();
                        oos.writeUTF(currentDatabaseName);
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
                    result = dropTable(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Table deleted successfully)");
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        if(currentDatabaseName == null)
                            oos.writeUTF("No database in use.");
                        else
                            oos.writeUTF("Failed to delete.");
                    }
                    oos.flush();
                    break;
                    
                case INSERT:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = insertIntoTable(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Insert OK)");
                    }
                    else if(result == 1){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(No database in use.)");
                    }
                    else if(result == 2){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The given table doesn't exist.)");
                    }
                    else if(result == 3){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("\n(One of the specified values have a wrong datatype.)");
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
                    if(currentDatabaseName != null){
                        ArrayList<String> tables = showTables();
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeObject(tables);
                        oos.flush();
                    }
                    else{
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(No database in use.)\n");
                    }
                    oos.flush();
                    break;
                    
                case UPDATE:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = updateRequest(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Update successful)");
                    }
                    else if(result == 1){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(No database in use.)");
                    }
                    else if(result == 2){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The table doesn't exist.)");
                    }
                    else if(result == 3){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(Empty table.)");
                    }
                    else if(result == 4){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The given table field doesn't exist.)");
                    }
                    else if(result == 5){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The given parameter doesn't correspond to the type of the table field.)");
                    }
                    oos.flush();
                    break;
                    
                case DELETE:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    result = deleteRequest(request);
                    if(result == 0){
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Delete successful)");
                    }
                    else if(result == 1){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(No database in use.)");
                    }
                    else if(result == 2){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The table doesn't exist.)");
                    }
                    else if(result == 3){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(Empty table.)");
                    }
                    else if(result == 4){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The given table field doesn't exist.)");
                    }
                    else if(result == 5){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(The given parameter doesn't correspond to the type of the table field.)");
                    }
                    oos.flush();
                    break;
                    
                case SELECT:
                    request = ois.readUTF();
                    System.out.println("Request: "+request);
                    if(currentDatabaseName == null){
                        oos.writeInt(FAILURE);
                        oos.flush();
                        oos.writeUTF("(No database in use.)");
                    }else{
                        DefaultTableModel query = selectRequest(request);
                        oos.writeInt(SUCCESS);
                        oos.flush();
                        oos.writeUTF("(Select OK)");
                        oos.flush();
                        oos.writeObject(query);
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
        
        System.out.println("Creating database...");
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
        
        try{
            
            File file = new File("Databases/"+db_name+".db");

            if(file.delete())
                    System.out.println(file.getName() + " database has been deleted!");
            else
                    System.out.println("Delete file operation failed. (File doesn't exist or Database doesn't exit)");
            
            return 0;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
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
        currentDatabaseName = db_name;
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
        
        if(currentDatabase.containsKey(table_name))
                return 1;
        
        try {
            // Read the Java File generated by PLY.
            File file = new File("src/src/GeneratedClasses/"+currentDatabaseName+"/"+table_name+".java");
            StringBuilder fileContents = new StringBuilder((int)file.length());
            Scanner scanner;
            scanner = new Scanner(file);
            String lineSeparator = System.getProperty("line.separator");
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            
            scanner.close();
            String java_class = fileContents.toString();
            int res = compileClass(java_class, table_name,currentDatabaseName);
            
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

    static int compileClass(String java_class, String table_name, String db_name) {
        DynamicCompiler dc = new DynamicCompiler("build/classes/Databases/"+db_name+"/");
        //1.Construct an in-memory java source file from your dynamic code
        JavaFileObject file = dc.getJavaFileObject(db_name,java_class,table_name);
        Iterable<? extends JavaFileObject> files = Arrays.asList(file);
 
        // 2.Compile your files by JavaCompiler
        dc.compile(files);
        return 0;
    }
    
    /**
     * This function loads an instance of the class which corresponds to the 
     * given table.
     **/
    static Object loadInstanceOfClass(String db_name, String table_name){
        
        File file = new File("./build/classes/Databases/"+db_name);
        
        try{
            
            URL url = file.toURL(); // file:/classes/db_name/
            URL[] urls = new URL[] { url };
            // Create a new class loader with the directory
            ClassLoader loader = new URLClassLoader(urls);
            Class thisClass = loader.loadClass(db_name+"."+table_name);
            //System.out.println("Instance of the class loaded succesfully");
            Object table = thisClass.newInstance(); 
            return table;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * This function attempts to run a setter method of a class which has been 
     * created dynamically with the DynamicCompiler class.
     **/
    static int attemptToRunSetterMethod(Object instanceOfTable, String attribute, Object param){
        try{
            
            String methodName = "set_"+attribute;
            Method setMethod = instanceOfTable.getClass().getMethod(methodName, param.getClass());
            setMethod.invoke(instanceOfTable, param); // pass arg
            //System.out.println("Setter executed successfully.");
            return 0;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return 1;
        
    }
    
    /**
     * This function attempts to run a getter method of a class which has been 
     * created dynamically with the DynamicCompiler class.
     **/
    static Object attemptToRunGetterMethod(Object instanceOfTable, String attribute){
        
        try{
            
            String methodName = "get_"+attribute;
            Method getNameMethod = instanceOfTable.getClass().getMethod(methodName);
            Object ret = getNameMethod.invoke(instanceOfTable); // explicit cast
            //System.out.println("Valor devuelto por metodo:"+ret);
            return ret;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return null;
        
    }

    /**
     * This function drops a table from the current database (if there's current
     * database in use and if the table exists).
     **/
    static int dropTable(String request) {
        
        // If there's no database in use
        if(currentDatabase == null)
            return 1;
        
        String table_name = request.substring(11,request.length()-1);
        System.out.println(table_name);
        // Check if the current database contains the given table
        if(!currentDatabase.containsKey(table_name))
            return 1;
        
        // Delete the table from the current database
        currentDatabase.remove(table_name);
        
        try{
            
            // Delete the .java file created with PLY
            File javaFile = new File("src/src/GeneratedClasses/"+currentDatabaseName+"/"+table_name+".java");
            File classFile = new File("build/classes/Databases/"+currentDatabaseName+"/"+table_name+".java");
            if(javaFile.delete())
                System.out.println(javaFile.getName() + " database has been deleted!");
            else
                System.out.println("Delete file operation failed. (File doesn't exist or Database doesn't exit)");
            if(classFile.delete())
                System.out.println(classFile.getName() + " database has been deleted!");
            else
                System.out.println("Delete javaclass file operation failed. Maybe in use.");
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return 0;
    }

    /**
     * This function returns an ArrayList which contains the name of the tables
     * of the current database in use.
     **/
    static ArrayList<String> showTables() {
        ArrayList<String> t = new ArrayList<>();
        Set<String> t_names = currentDatabase.keySet();
        for(String t_name : t_names)
            t.add(t_name);
        return t;
    }

    /**
     * this function attempts to insert an element into a table of the current
     * database (if there's a database in use).
     **/
    static int insertIntoTable(String request) {
        
        if(currentDatabaseName == null)
            return 1;
        
        String[] s1 = request.split("( values)|( VALUES)");
        
        // Get the table name and the fields separated
        String table_name = s1[0].substring(12, s1[0].length());
        // System.out.println(table_name);
        
        //Check if the current database contains the given table
        if(!currentDatabase.containsKey(table_name))
            return 2;
        
        String[] values = s1[1].replaceAll("\\(|\\)|;", "").split(",");
        
        // Attempt to create an instance of the given table
        Object o = loadInstanceOfClass(currentDatabaseName, table_name);
        Field[] f = o.getClass().getDeclaredFields();
        int i = 0;
        for(Field fi : f){
            
            // If the value is a possible String, replace all the apostrophes with blanks
            if(values[i].contains("'"))
                values[i] = values[i].replaceAll("'", "");
            
            // Try to parse the value to its correspondig type
            Object param = setParam(fi.getType().getName(),values[i]);
            
            // If there was an error while parsing
            if(param.equals("Error parsing"))
                return 3;
            
            // Run the setter to initialize the object values
            attemptToRunSetterMethod(o, fi.getName(), param);
            i++;
            
        }
        
        // Add the new element to the database
        currentDatabase.get(table_name).add(o);
        
        return 0;
        
    }
    
    /**
     * This function attempts to parse a given field into the corresponding 
     * class type according to the class fields.
     **/
    static Object setParam(String objectClass, String field){
        Object param;
        switch(objectClass){
            case "java.lang.Byte":
                try{
                    param = Byte.parseByte(field);
                    return param;
                }catch(Exception e){
                    return "Error Parsing";
                }
            case "java.lang.Short":
                try{
                    param = Short.parseShort(field);
                    return param;
                }catch(Exception e){
                    return "Error Parsing";
                }
            case "java.lang.Integer":
                try{
                    param = Integer.parseInt(field);
                    return param;
                }catch(Exception e){
                    return "Error Parsing";
                }
            case "java.lang.Long":
                try{
                    param = Long.parseLong(field);
                    return param;
                }catch(Exception e){
                    return "Error Parsing";
                }
            case "java.lang.Float":
                try{
                    param = Float.parseFloat(field);
                    return param;
                }catch(Exception e){
                    return "Error Parsing";
                }
            case "java.lang.Double":
                try{
                    param = Double.parseDouble(field);
                    return param;
                }catch(Exception e){
                    return "Error Parsing";
                }
            case "java.lang.String":
                param = field;
                return param;
            case "java.lang.Boolean":
                try{
                    param = Boolean.parseBoolean(field);
                    return param;
                }catch(Exception e){
                    return "Error Parsing";
                }
            default:
                return null;
        }
    }

    /**
     * This function returns a TableModel which contains the results of a query
     * select * from table;.
     **/
    static DefaultTableModel selectRequest(String request) {
        
        DefaultTableModel model = new DefaultTableModel();
        
        // Check if the table exist
        String table_name = request.substring(14,request.length()-1);
        if(!currentDatabase.containsKey(table_name))
            return model;   // Returns an empty model
        
        //Get the elements of the table
        LinkedList<Object> elements = currentDatabase.get(table_name);
        
        if(elements.isEmpty())
            return model; // Return an empty model
        
        Field[] fields = elements.getFirst().getClass().getDeclaredFields();
        String[] columnIdentifiers = new String[fields.length];
        int i = 0;
        for(Field f : fields){
                columnIdentifiers[i] = f.getName();
                i++;
        }
        int row = 0;
        int column = 0;
        model.setColumnCount(fields.length);
        model.setNumRows(elements.size());
        model.setColumnIdentifiers(columnIdentifiers);
        for(Object element : elements){
            for(Field f : fields){
                Object ret = attemptToRunGetterMethod(element, f.getName());
                model.setValueAt(ret, row, column);
                column++;
            }
            row++;
            column = 0;
        }
        
        return model;
    }

    /**
     * This function attempts to updates an element of a table in the current
     * database.
     **/
    static int updateRequest(String request) {
        
        // If there's no database in use
        if(currentDatabaseName == null)
            return 1;
        
        // Get the table name
        String table_name = request.split(" ")[1];
        
        // If the table doesn't exist in the current database
        if(!currentDatabase.containsKey(table_name))
            return 2;
        
        // If the table is empty
        if(currentDatabase.get(table_name).isEmpty())
            return 3;
        
        // Get the table field to update
        String[] split = request.split("=");
        String table_field = "";
        for(int i = split[0].length()-1; i >=0; i--){
            if(split[0].charAt(i)==' '){
                table_field = split[0].substring(i+1);
                break;
            }
              
        }
        String new_value = split[1].replaceAll(";", "");
        
        Object o = currentDatabase.get(table_name).getFirst();
        Field[] fields = o.getClass().getFields();
        String type = "";
        for(Field f : fields){
            if(f.getName().equals(table_field))
                type = f.getType().getName();
        }
        
        // If the given field doesn't exist in the table
        if(type.equals(""))
            return 4;
        
        // Attempt to update the elements of the table
        for(Object element : currentDatabase.get(table_name)){
            
            // Try to parse the value to its correspondig type
            Object param = setParam(type,new_value);
            if(attemptToRunSetterMethod(element, table_field, param) != 0)
                return 5; // Not the expected parameter
            
        }
        return 0;
        
    }

    /**
     * This function attempts to delete elements of a table evaluating the given
     * condition in the request.
     **/
    static int deleteRequest(String request) {
        
        // If there's no database in use
        if(currentDatabaseName == null)
            return 1;
        
        String table_name = request.split(" ")[2];
        
        if(!currentDatabase.containsKey(table_name))
            return 2;
        
        // If the table is not empty
        if(currentDatabase.get(table_name).isEmpty())
            return 3;
        
        // Get the parameters of the condition
        String[] split = request.split("(where )|(WHERE )");
        String condition = split[1];
        String table_field;
        int end=0;
        for(int i = 0; i < condition.length(); i++){
            if(condition.charAt(i) != ' ' && condition.charAt(i) != '=')
                end++;
            else
                break;
        }
        table_field = condition.substring(0, end);
        String value = condition.split("=")[1].replaceAll(";", "");
        
        // If the given field doesn't exist in the table
        Object o = currentDatabase.get(table_name).getFirst();
        Field[] fields = o.getClass().getFields();
        String type = "";
        for(Field f : fields){
            if(f.getName().equals(table_field))
                type = f.getType().getName();
        }
        
        // If the given field doesn't exist in the table
        if(type.equals(""))
            return 4;
        
        // Attempt to delet the elements of the table
        for(Object element : currentDatabase.get(table_name)){
            
            // Run the getter
            Object ret = attemptToRunGetterMethod(element, table_field);
            if(!ret.equals(null)){
                if(ret.equals(value))
                    currentDatabase.get(table_name).remove(element);
            }
            else
                return 5;
            
        }
        
        return 0;
        
    }
    
}
