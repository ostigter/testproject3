package xquery;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for a XQuery processor.
 * 
 * @author Oscar Stigter
 */
public interface XQueryProcessor {

	/**
	 * Executes a ad-hoc query.
	 * 
	 * @param query
	 *            The query text.
	 * 
	 * @return The query result.
	 * 
	 * @throws XQueryException
	 *             If the query could not be executed.
	 */
	OutputStream executeQuery(String query) throws XQueryException;

	/**
	 * Adds an XQuery module location (URI), which can reference an individual
	 * module or a directory/collection with modules.
	 * 
	 * Modules are recognized by comparing the file extention to a list of known
	 * file extentions for XQuery modules.
	 * 
	 * If a collection is specified, it will be searched recursively for
	 * modules.
	 * 
	 * @param location
	 *            The module location (URI).
	 * 
	 * @throws IOException
	 *             If the location is invalid.
	 */
	void addModuleLocation(String location) throws IOException;

	/**
	 * Clears all modules.
	 */
	void clearModules();

	/**
	 * Indicates whether an XQuery module with a specific namespace exists.
	 * 
	 * @param namespace
	 *            The module namespace.
	 * 
	 * @return True if it exists, otherwise false.
	 */
	boolean supportsNamespace(String namespace);

	/**
	 * Executes an XQuery function.
	 * 
	 * The function is identified by the combination of the module namespace and
	 * the function name.
	 * 
	 * Java objects can be specified as optional query parameters. Saxon will
	 * automatically try to convert them to their XML equivalents.
	 * 
	 * @param namespace
	 *            The module namespace.
	 * @param function
	 *            The function name.
	 * @param params
	 *            Any optional parameters.
	 * 
	 * @return The query result.
	 * 
	 * @throws XQueryException
	 *             If the query could not be executed.
	 */
	OutputStream executeFunction(
			String namespace, String function, Object... params) throws XQueryException;

}
