package org.ozsoft.courier;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/**
 * Action that logs the current message.
 * 
 * @author Oscar Stigter
 */
public class LogAction implements Action {

	/** The log. */
	private static final Logger LOG = Logger.getLogger(LogAction.class);
    
	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.courier.Action#execute(org.ozsoft.courier.Context)
	 */
	public void execute(Context context) throws CourierException {
        Node message = context.getMessage();
        try {
            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            StringWriter sw = new StringWriter();
            transformer.transform(
                    new DOMSource(message), new StreamResult(sw));
            LOG.info("Message content:\n" + sw.toString());
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
