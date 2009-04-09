package org.ozsoft.courier;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/**
 * Action that writes the current message to file.
 * 
 * @author Oscar Stigter
 */
public class FileOutAction implements Action {
    
	/** The log. */
	private static final Logger LOG = Logger.getLogger(FileOutAction.class);
    
    /** The directory to write the file to. */
	private final File dir;
	
	/** The XPath expression to generate the filename. */
    private final String fileNameExpr;

    /** The namespace context. */
    private final DefaultNamespaceContext nc;
    
    /**
	 * Constructor.
	 * 
	 * @param path
	 *            The directory path.
	 * @param fileNameExpr
	 *            The XPath expression for the filename.
	 */
    public FileOutAction(String path, String fileNameExpr) throws CourierException {
        this.fileNameExpr = fileNameExpr;

        dir = new File(path);
        if (!dir.isDirectory()) {
            throw new CourierException("Directory not found: " + path);
        }
        if (!dir.canWrite()) {
            throw new CourierException("Directory not writable: " + path);
        }
        
        nc = new DefaultNamespaceContext();
    }
    
    /**
	 * Adds a namespace.
	 * 
	 * @param prefix
	 *            The namespace prefix.
	 * @param uri
	 *            The namespace URI.
	 */
    public void addNamespace(String prefix, String uri) {
        nc.addNamespace(prefix, uri);
        LOG.debug(String.format("Namespace added with prefix '%s' and URI '%s'", prefix, uri));
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.courier.Action#execute(org.ozsoft.courier.Context)
     */
    public void execute(Context context) {
        Node message = context.getMessage();
        String fileName = XPathHelper.evaluate(message, fileNameExpr, nc);
        if (fileName != null) {
            File file = new File(dir, fileName);
            LOG.debug(String.format("Writing message to file '%s'", file));
            try {
                Transformer transformer =
                        TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty("indent", "yes");
                transformer.transform(
                        new DOMSource(message), new StreamResult(file));
            } catch (TransformerException e) {
                LOG.error("Error writing message to file", e);
            }
        }
    }
    
}
