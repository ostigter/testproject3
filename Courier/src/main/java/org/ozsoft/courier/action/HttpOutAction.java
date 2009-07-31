package org.ozsoft.courier.action;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.log4j.Logger;
import org.ozsoft.courier.Context;
import org.ozsoft.courier.CourierException;
import org.w3c.dom.Node;

/**
 * Action that sends the current message to a web service using the HTTP protocol.
 * 
 * @author Oscar Stigter
 */
public class HttpOutAction implements Action {
    
	/** The log. */
	private static final Logger LOG = Logger.getLogger(HttpOutAction.class);
    
	/** The transformation factory. */
	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
	
    /** The endpoint URL. */
	private final String url;
	
    /**
	 * Constructor.
	 * 
	 * @param url
	 *            The endpoint URL.
	 */
    public HttpOutAction(String url) throws CourierException {
        this.url = url;
        LOG.debug(String.format("Configuring http-out action with url '%s'", url));
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.courier.Action#execute(org.ozsoft.courier.Context)
     */
    public void execute(Context context) {
        LOG.debug(String.format("Sending message with HTTP POST to URL '%s'", url));
        String message = nodeToString(context.getMessage()); 
		try {
			HttpClient client = new HttpClient();
			HttpMethod postMethod = new PostMethod(url);
			ByteArrayInputStream bais = new ByteArrayInputStream(message.getBytes());
			RequestEntity entity = new InputStreamRequestEntity(bais, "text/xml; charset=utf-8");
			((EntityEnclosingMethod) postMethod).setRequestEntity(entity);
			int statusCode = client.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
		        LOG.error(String.format("HTTP error: %s\nHTTP response:\n%s",
		        		postMethod.getStatusLine(), postMethod.getResponseBodyAsString()));
			}
			bais.close();
			postMethod.releaseConnection();
		} catch (Exception e) {
			LOG.error("Error invoking service: " + e.getMessage(), e);
		}
    }
    
    /**
	 * Returns the string representation of a node.
	 * 
	 * @param node
	 *            The node.
	 * 
	 * @return The string representation.
	 */
    private String nodeToString(Node node) {
    	String s = null;
		try {
			Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
			transformer.setOutputProperty("indent", "yes");
			StringWriter sw = new StringWriter();
			transformer.transform(new DOMSource(node), new StreamResult(sw));
			s = sw.toString();
			sw.close();
		} catch (Exception e) {
		    LOG.error("Could not transform message", e);
		}
		return s;
    }
    
}
