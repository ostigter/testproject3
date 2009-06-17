package org.ozsoft.soapclient;

import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

/**
 * Simple SOAP web service client with JAX-WS.
 * 
 * A web services must be specified by its endpoint URL, optionally with the
 * namespace, service name and port name.
 * It support an optional SOAP action and HTTP Basic authentication.   
 * 
 * @author Oscar Stigter
 */
public class SoapClient {

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(SoapClient.class);
	
	/** Default service name. */
	private static final String DEFAULT_SERVICE_NAME = "DefaultServiceName";
	
	/** Default port name. */
	private static final String DEFAULT_PORT_NAME = "DefaultPortName";
	
	/** The JAX-WS dispatch. */
	private Dispatch<Source> dispatch;
	
	/**
	 * Constructor with just the web service's endpoint URL.
	 * 
	 * @param endpoint
	 *            The endpoint URL.
	 * 
	 * @throws SoapException
	 *             If the dispatcher could not be created.
	 */
	public SoapClient(String endpoint) throws SoapException {
		this(endpoint, null, DEFAULT_SERVICE_NAME, DEFAULT_PORT_NAME);
	}
	
	/**
	 * Constructor with the web service's endpoint URL, namespace, service name and port name.
	 * 
	 * @param endpoint
	 *            The endpoint URL.
	 * @param namespace
	 *            The namespace.
	 * @param serviceName
	 *            The service name.
	 * @param portName
	 *            The port name.
	 * @throws SoapException
	 *             If the dispatcher could not be created.
	 */
	public SoapClient(String endpoint, String namespace, String serviceName, String portName) throws SoapException {
		LOG.debug("Creating...");
		try {
    		Service service = Service.create(new QName(namespace, serviceName));
    		QName portQName = new QName(namespace, portName);
    		service.addPort(portQName, SOAPBinding.SOAP11HTTP_BINDING, endpoint);
    		dispatch = service.createDispatch(portQName, Source.class, Mode.PAYLOAD);
    		LOG.debug("Created");
		} catch (Exception e) {
			LOG.error(e);
		}
	}
	
	/**
	 * Sets the SOAP action.
	 * 
	 * @param action
	 *            The SOAP action.
	 */
	public void setAction(String action) {
		if (action == null) {
			dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, Boolean.FALSE);
		} else {
			dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
			dispatch.getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, action);
		}
	}
	
	/**
	 * Sets the authentication data.
	 * 
	 * @param username
	 *            The username.
	 * @param password
	 *            The password.
	 */
	public void setAuthentication(String username, String password) {
		dispatch.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
		dispatch.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
	}
	
	/**
	 * Calls the web service.
	 * 
	 * @param payload
	 *            The payload message.
	 *            
	 * @return The response.
	 * 
	 * @throws SoapException
	 *             If the web service could not be invoked.
	 */
	public String call(Source payload) throws SoapException {
		LOG.debug("Invoking web service...");
		String response = "";
		try {
    		Source responseSource = dispatch.invoke(payload);
    		if (responseSource != null) {
	    		StringWriter sw = new StringWriter();
	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty("indent", "yes");
	            transformer.transform(responseSource, new StreamResult(sw));
	            response = sw.toString();
	            sw.close();
    		}
		} catch (Exception e) {
			String msg = "Could not invoke web servce";
			LOG.error(msg, e);
			throw new SoapException(msg, e);
		}
		return response;
	}
	
}
