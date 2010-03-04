package xquery;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.Configuration;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

/**
 * XQuery processor implementation with Saxon.
 * 
 * @author Oscar Stigter
 */
public class SaxonXQueryProcessor extends AbstractXQueryProcessor {

	/** Saxon configuration. */
	private final Configuration config;

	/** Static query context. */
	private final StaticQueryContext staticQueryContext;

	/** Dynamic query context. */
	private final DynamicQueryContext dynamicQueryContext;

	/**
	 * Constructor.
	 */
	public SaxonXQueryProcessor() {
		super();
		config = new Configuration();
		staticQueryContext = new StaticQueryContext(config);
		dynamicQueryContext = new DynamicQueryContext(config);
	}

	/*
	 * (non-Javadoc)
	 * @see xquery.AbstractXQueryProcessor#executeQuery(java.lang.String)
	 */
	@Override
	public OutputStream executeQuery(String query) throws XQueryException {
		logger.debug("Executing query: \n" + query);
		try {
			XQueryExpression expr = staticQueryContext.compileQuery(query);
			dynamicQueryContext.clearParameters();
			OutputStream os = new ByteArrayOutputStream();
			Result result = new StreamResult(os);
			expr.run(dynamicQueryContext, result, null);
			return os;
		} catch (XPathException e) {
			throw new XQueryException("Error executing query: "
					+ e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see xquery.AbstractXQueryProcessor#executeFunction(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public OutputStream executeFunction(String namespace, String function,
			Object... params) throws XQueryException {
		String location = modules.get(namespace);
		if (location == null) {
			throw new XQueryException("Namespace " + namespace
					+ " is not supported");
		}
		String query = createQuery(namespace, location, function, params);
		logger.debug("Executing query: \n" + query);

		try {
			XQueryExpression expr = staticQueryContext.compileQuery(query);
			dynamicQueryContext.clearParameters();
			for (int i = 0; i < params.length; i++) {
				dynamicQueryContext.setParameter("par" + String.valueOf(i + 1),
						params[i]);
			}
			OutputStream os = new ByteArrayOutputStream();
			Result result = new StreamResult(os);
			expr.run(dynamicQueryContext, result, null);
			return os;
		} catch (XPathException xpe) {
			throw new XQueryException(xpe.getMessage());
		}
	}

}
