package org.ozsoft.courier.action;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ozsoft.courier.Context;
import org.ozsoft.courier.CourierException;
import org.w3c.dom.Node;

/**
 * Action that logs the current message.
 * 
 * @author Oscar Stigter
 */
public class LogAction implements Action {

	/** The log. */
	private static final Logger LOG = Logger.getLogger(LogAction.class);
	
	/** The log level. */
	private final Level level;
	
	/**
	 * Constructor.
	 * 
	 * @param level
	 *            The log level.
	 */
	public LogAction(Level level) {
		this.level = level;
	}
    
	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.courier.Action#execute(org.ozsoft.courier.Context)
	 */
	public void execute(Context context) throws CourierException {
		Level currentLevel = LOG.getEffectiveLevel();
		if (level.isGreaterOrEqual(currentLevel)) {
	        Node message = context.getMessage();
	        try {
	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty("indent", "yes");
	            StringWriter sw = new StringWriter();
	            transformer.transform(new DOMSource(message), new StreamResult(sw));
	            LOG.log(level, "Message content:\n" + sw.toString());
	            sw.close();
	        } catch (TransformerException e) {
	            String msg = "Error transforming message";
	            LOG.error(msg, e);
	            throw new CourierException(msg, e);
	        } catch (IOException e) {
	            // Should never happen.
	            String msg = "Error closing StringWriter";
	            LOG.error(msg, e);
	            throw new CourierException(msg, e);
	        }
		}
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return String.format("LogAction(%s)", level);
    }
    
}
