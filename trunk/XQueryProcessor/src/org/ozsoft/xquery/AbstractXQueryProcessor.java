package org.ozsoft.xquery;

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

import org.apache.log4j.Logger;

/**
 * Abstract base class for an XQuery processor.
 * 
 * @author Oscar Stigter
 */
public abstract class AbstractXQueryProcessor implements XQueryProcessor {

	/** log4j logger. */
	protected static final Logger logger = Logger.getLogger(XQueryProcessor.class);

	/** Regex pattern for extracting module namespace declarations. */
	protected final Pattern modPattern;

	/** XQuery module file filter. */
	protected final FileFilter filter;

	/** XQuery modules mapped by their module namespace. */
	protected final Map<String, String> modules;

	/**
	 * Constructor.
	 */
	public AbstractXQueryProcessor() {
		modules = new HashMap<String, String>();
		filter = new XQueryModuleFileFilter();
		modPattern = Pattern.compile("module namespace (.*) = \"(.*)\";");
	}

	/*
	 * (non-Javadoc)
	 * @see xquery.XQueryProcessor#addModuleLocation(java.lang.String)
	 */
	@Override
	public final void addModuleLocation(String path) throws IOException {
		File dir = new File(path);
		if (dir.isDirectory() && dir.canRead()) {
			logger.debug(String.format("Add module location '%s'", dir
					.getAbsolutePath()));
			findModules(dir);
		} else {
			throw new IOException("Invalid module directory: " + path);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see xquery.XQueryProcessor#supportsNamespace(java.lang.String)
	 */
	@Override
	public final boolean supportsNamespace(String namespace) {
		return modules.containsKey(namespace);
	}

	/*
	 * (non-Javadoc)
	 * @see xquery.XQueryProcessor#clearModules()
	 */
	@Override
	public final void clearModules() {
		modules.clear();
		logger.debug("Modules cleared.");
	}

	/*
	 * (non-Javadoc)
	 * @see xquery.XQueryProcessor#executeQuery(java.lang.String)
	 */
	@Override
	public abstract OutputStream executeQuery(String query) throws XQueryException;

	/*
	 * (non-Javadoc)
	 * @see xquery.XQueryProcessor#executeFunction(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public abstract OutputStream executeFunction(
			String namespace, String function, Object... params) throws XQueryException;

	/**
	 * Returns the query text for calling an XQuery function.
	 * 
	 * @param namespace
	 *            the module namespace
	 * @param moduleLocation
	 *            the module URI
	 * @param function
	 *            the function name
	 * @param params
	 *            optional query parameters
	 */
	protected String createQuery(String namespace, String moduleLocation,
			String function, Object[] params) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nimport module namespace m = '");
		sb.append(namespace);
		sb.append("'\n  at '");
		sb.append(moduleLocation);
		sb.append("';\n\n");

		for (int i = 0; i < params.length; i++) {
			sb.append("declare variable $par" + (i + 1) + " external;\n");
		}

		sb.append("\nm:" + function + "(");

		for (int i = 0; i < params.length; i++) {
			sb.append("$par" + (i + 1));
			if (i < (params.length - 1)) {
				sb.append(", ");
			}
		}
		sb.append(")\n");

		return sb.toString();
	}

	private void findModules(File dir) throws IOException {
		for (File file : dir.listFiles(filter)) {
			if (file.isDirectory()) {
				findModules(file);
			} else {
				addModule(file);
			}
		}
	}

	private void addModule(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		while (line != null) {
			Matcher m = modPattern.matcher(line);
			if (m.matches()) {
				String namespace = m.group(2);
				modules.put(namespace, file.getPath().replaceAll("\\\\", "/"));
				logger.debug(String.format("Found module with namespace '%s'",
						namespace));
				break;
			}
			line = reader.readLine();
		}
		reader.close();
	}

}
