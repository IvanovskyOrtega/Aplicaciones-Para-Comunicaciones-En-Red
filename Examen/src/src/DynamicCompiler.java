package src;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;
 
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
 
/**
 * Dynamic java class compiler and executer  <br>
 * Demonstrate how to compile dynamic java source code, <br>
 * instantiate instance of the class, and finally call method of the class <br>
 *
 * http://www.beyondlinux.com
 *
 *
 */
public class DynamicCompiler
{
    /** where shall the compiled class be saved to (should exist already) */
    public static String classOutputFolder = "./build/classes/Databases";
    public static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject>
    {
        public void report(Diagnostic<? extends JavaFileObject> diagnostic)
        {
            
            System.out.println("Line Number->" + diagnostic.getLineNumber());
            System.out.println("code->" + diagnostic.getCode());
            System.out.println("Message->"
                               + diagnostic.getMessage(Locale.ENGLISH));
            System.out.println("Source->" + diagnostic.getSource());
            System.out.println(" ");
        }
    }
 
    /** java File Object represents an in-memory java source file <br>
     * so there is no need to put the source file on hard disk  **/
    public static class InMemoryJavaFileObject extends SimpleJavaFileObject
    {
        private String contents = null;
 
        public InMemoryJavaFileObject(String className, String contents) throws Exception
        {
            super(URI.create("string:///" + className.replace('.', '/')
                             + Kind.SOURCE.extension), Kind.SOURCE);
            this.contents = contents;
        }
 
        public CharSequence getCharContent(boolean ignoreEncodingErrors)
                throws IOException
        {
            return contents;
        }
    }
 
    /** Get a simple Java File Object ,<br>
     * It is just for demo, content of the source code is dynamic in real use case */
    public static JavaFileObject getJavaFileObject(String db_name,String java_class, String table_name)
    {
        JavaFileObject jfo = null;
        try
        {
            jfo = new InMemoryJavaFileObject("/*"+db_name+"."+table_name+"*/"+table_name, java_class);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return jfo;
    }
 
    /** compile your files by JavaCompiler */
    public static void compile(Iterable<? extends JavaFileObject> files)
    {
        //get system compiler:
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
 
        // for compilation diagnostic message processing on compilation WARNING/ERROR
        MyDiagnosticListener c = new MyDiagnosticListener();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(c,
                                                                              Locale.ENGLISH,
                                                                              null);
        //specify classes output folder
        try{
            
            File f = new File(classOutputFolder);
            System.out.println(classOutputFolder);
            f.mkdir();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        Iterable options = Arrays.asList("-d", classOutputFolder);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                                                             c, options, null,
                                                             files);
        Boolean result = task.call();
        if (result == true)
        {
            //System.out.println("Succeeded");
        }
    }
    
    public DynamicCompiler(String classOutputFolder){
        DynamicCompiler.classOutputFolder = classOutputFolder;
    }
}