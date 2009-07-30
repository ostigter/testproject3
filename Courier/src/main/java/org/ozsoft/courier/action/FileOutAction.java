package org.ozsoft.courier.action;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.ozsoft.courier.Context;
import org.ozsoft.courier.CourierException;
import org.ozsoft.courier.XPathHelper;
import org.w3c.dom.Node;

/**
 * Action that writes the current message to a file.
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
        LOG.debug(String.format("Configuring file-out action with path '%s'", path));
        dir = new File(path);
        if (!dir.isDirectory()) {
            throw new CourierException("Directory not found: " + path);
        }
        if (!dir.canWrite()) {
            throw new CourierException("Directory not writable: " + path);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.courier.Action#execute(org.ozsoft.courier.Context)
     */
    public void execute(Context context) {
        Node message = context.getMessage();
        String fileName = XPathHelper.evaluate(message, fileNameExpr, context);
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
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return String.format("FileOutAction(%s)", dir);
    }
    
}
