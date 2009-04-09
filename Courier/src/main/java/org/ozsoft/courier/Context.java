package org.ozsoft.courier;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * The message context during the execution of a handler.
 *  
 * @author Oscar Stigter
 */
public class Context {
    
    /** The current message. */
	private Node message;
	
	/** The variables mapped by name. */
    private final Map<String, String> variables;
    
    /**
     * Constructor.
     */
    public Context() {
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
	 * Returns the value of a variable.
	 * 
	 * @param name
	 *            The variable name.
	 * 
	 * @return The variable value.
	 */
    public String getVariable(String name) {
        return variables.get(name);
    }
    
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
    
}
