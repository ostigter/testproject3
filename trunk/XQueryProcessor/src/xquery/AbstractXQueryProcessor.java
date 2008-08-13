package xquery;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Abstract base class for an XQuery processor.
 * 
 * @author Oscar Stigter
 */
public abstract class AbstractXQueryProcessor implements XQueryProcessor {


    /** Absolute path to the database root directory. */
    protected static final String DB_ROOT =
        "file:///" + System.getProperty("user.dir").replaceAll("\\\\", "/");

    /** Regex pattern for extracting module namespace declarations. */
    protected final Pattern modPattern;
    
    /** XQuery module file filter. */
    protected final FileFilter filter;

    /** XQuery modules mapped by their module namespace. */ 
    protected final Map<String, String> modules;


    //------------------------------------------------------------------------
    //  Constructor
    //------------------------------------------------------------------------

    
    public AbstractXQueryProcessor() {
        modules = new HashMap<String, String>();
        filter = new XQueryModuleFileFilter();
        modPattern = Pattern.compile("module namespace (.*) = \"(.*)\";");
    }


    //------------------------------------------------------------------------
    //  Public methods
    //------------------------------------------------------------------------

    
    public final void addModuleLocation(String path) throws IOException {
        File dir = new File(path);
        if (dir.isDirectory() && dir.canRead()) {
            findModules(dir);        
        } else {
            throw new IOException("Invalid module directory: " + path);
        }
    }


    public final boolean supportsNamespace(String namespace) {
        return modules.containsKey(namespace);
    }


    public final void clearModules() {
        modules.clear();
    }
    
    
    //------------------------------------------------------------------------
    //  Abstract methods
    //------------------------------------------------------------------------

    
    public abstract OutputStream executeFunction(
            String namespace, String function,  Object ... params)
            throws XQueryException;


    //------------------------------------------------------------------------
    //  Protected methods
    //------------------------------------------------------------------------

    
    protected String createQuery(String namespace, String moduleLocation,
            String function, Object[] params) { 
        StringBuilder sb =  new StringBuilder();
        sb.append("import module namespace m = '");
        sb.append(namespace);
        sb.append("' at '");
        sb.append(moduleLocation);
        sb.append("';");

        for (int i = 0; i < params.length; i++) {
            sb.append("declare variable $par"  + (i + 1) + " external;");
        }

        sb.append("m:" + function + "(");

        for (int i = 0; i < params.length; i++) {
            sb.append("$par" + ( i + 1));
            if (i == (params.length - 1)) {
                sb.append(")");
            } else {
                sb.append(", ");
            }
        }

        return sb.toString();
    }


    //------------------------------------------------------------------------
    //  Private methods
    //------------------------------------------------------------------------

    
    private void findModules(File dir) throws IOException {
        for (File file : dir.listFiles(filter)) {
            if (file.isDirectory()) {
                findModules(file);
            } else {
                addModule(file);
            }
        }
    }


    private void addModule(File module) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(module));
        String line = reader.readLine();
        while ( line != null )  {
            Matcher m = modPattern.matcher(line);
            if (m.matches()) {
                String namespace = m.group(2);
                modules.put(namespace,
                    DB_ROOT + "/" + module.getPath().replaceAll("\\\\", "/"));
                System.out.println("Added module with namespace: " + namespace);
                break;
            }
            line = reader.readLine();            
        }
        reader.close();
    }


}
