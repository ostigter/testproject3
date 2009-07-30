package org.ozsoft.courier.action;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.ozsoft.courier.Context;
import org.ozsoft.courier.CourierException;

/**
 * Action that performs an XSLT transformation.
 * 
 * @author Oscar Stigter
 */
public class XsltAction implements Action {
	
    /** The log. */
    private static final Logger LOG = Logger.getLogger(XsltAction.class);
    
	/** The transformation factory. */
	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
	
	/** The XSLT stylesheet. */
	private final File xsltFile;
	
	/**
	 * Constructor.
	 * 
	 * @param path  The path to the XSLT stylesheet.
	 */
	public XsltAction(String path) throws CourierException {
		xsltFile = new File(path);
		if (!xsltFile.isFile()) {
			throw new CourierException(
			        String.format("XSLT stylesheet not found: '%s'", path));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.courier.Action#execute(org.ozsoft.courier.Context)
	 */
	public void execute(Context context) throws CourierException {
	    LOG.debug(String.format("Transforming message using stylesheet '%s'", xsltFile));
		Source xslt = new StreamSource(xsltFile);
		try {
			Transformer transformer = TRANSFORMER_FACTORY.newTransformer(xslt);
			transformer.setOutputProperty("indent", "yes");
			DOMResult result = new DOMResult();
			transformer.transform(new DOMSource(context.getMessage()), result);
			context.setMessage(result.getNode());
		} catch (TransformerException e) {
		    String msg = "Error transforming message";
		    LOG.error(msg, e);
		    throw new CourierException(msg, e);
		}
	}

}
