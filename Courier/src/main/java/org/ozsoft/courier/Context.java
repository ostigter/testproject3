package org.ozsoft.courier;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;

import org.w3c.dom.Node;

/**
 * The message context during the execution of a handler.
 * Contains the message, namespace resolver and variables.
 *  
 * @author Oscar Stigter
 */
public class Context implements XPathVariableResolver {
    
    /** The current message. */
	private Node message;
	
    /** The namespace resolver (handler scope). */
    private final NamespaceResolver namespaceResolver;
    
	/** The variables mapped by name. */
    private final Map<String, String> variables;
    
    /**
     * Constructor.
     */
    public Context(NamespaceResolver namespaceResolver) {
    	this.namespaceResolver = namespaceResolver;
        variables = new HashMap<String, String>();
    }
    
    /**
     * Returns the message.
     * 
     * @return The message.
     */
    public Node getMessage() {
        return message;
    }
    
    /**
	 * Sets the message.
	 * 
	 * @param message
	 *            The message.
	 */
    public void setMessage(Node message) {
        this.message = message;
    }
    
    /**
	 * Returns the namespace resolver.
	 * 
	 * @return The namespace resolver.
	 */
    public NamespaceResolver getNamespaceResolver() {
    	return namespaceResolver;
    }

//    /**
//	 * Returns the value of a variable.
//	 * 
//	 * @param name
//	 *            The variable name.
//	 * 
//	 * @return The variable value.
//	 */
//    public String getVariable(String name) {
//        return variables.get(name);
//    }
    
    /**
	 * Sets a variable.
	 * 
	 * @param name
	 *            The variable name.
	 * @param value
	 *            The variable value.
	 */
    public void setVariable(String name, String value) {
        variables.put(name, value);
    }

    /*
     * (non-Javadoc)
     * @see javax.xml.xpath.XPathVariableResolver#resolveVariable(javax.xml.namespace.QName)
     */
    @Override
	public Object resolveVariable(QName qname) {
		String name = qname.getLocalPart();
		return variables.get(name);
	}
    
}
