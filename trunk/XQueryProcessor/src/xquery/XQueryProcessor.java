package xquery;


import java.io.IOException;
import java.io.OutputStream;


/**
 * Interface for a XQueryProcessor.
 * 
 * @author Oscar Stigter
 */
public interface XQueryProcessor {
	
	
	OutputStream executeQuery(String query) throws XQueryException;
    

    /**
     * Adds an XQuery module location (URI), which can reference an individual
     * module or a directory/collection with modules.
     * 
     * Modules are recognized by comparing the file extention to a list of
     * known file extentions for XQuery modules.
     * 
     * If a collection is specified, it will be searched recursively for
     * modules.
     *  
     * @param location  the module location (URI)
     * 
     * @throws IOException  if the location is invalid  
     */
    void addModuleLocation(String location) throws IOException;
    

    /**
     * Clears all modules.
     */
    void clearModules();
            
    
    /**
     * Indicates whether a module with a specific namespace exists.
     *  
     * @param namespace  the module namespace
     * 
     * @return  true if it exists, otherwise false
     */
    boolean supportsNamespace(String namespace);
    
    
    /**
     * Executes an XQuery function.
     * 
     * The function is identified by the combination of the module namespace
     * and the function name.
     * 
     * Java objects can be specified as optional query parameters. Saxon will
     * automatically try to convert them to their XML equivalents.
     * 
     * @param  namespace  the module namespace
     * @param  function   the function name
     * @param  params     any optional parameters
     * 
     * @return  the query result as OutputStream 
     *  
     * @throws XQueryException  if the query could not be executed
     */
    OutputStream executeFunction(
            String namespace, String function, Object... params)
            throws XQueryException;
    

}
