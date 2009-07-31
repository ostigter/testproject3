package org.ozsoft.courier.action;

import org.apache.log4j.Logger;
import org.ozsoft.courier.Context;
import org.ozsoft.courier.CourierException;
import org.ozsoft.courier.XPathHelper;
import org.w3c.dom.Node;

/**
 * Action that sets a variable stored in the message context.
 * 
 * @author Oscar Stigter
 */
public class VariableAction implements Action {

	/** The log. */
	private static final Logger LOG = Logger.getLogger(VariableAction.class);
	
	/** The variable name. */
	private final String name;
	
	/** The expression to extract the variable value from the message. */
	private final String expression;
	
	public VariableAction(String name, String expression) throws CourierException {
		this.name = name;
		this.expression = expression;
	}
    
	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.courier.Action#execute(org.ozsoft.courier.Context)
	 */
	public void execute(Context context) throws CourierException {
        Node message = context.getMessage();
        String value = XPathHelper.evaluate(message, expression, context);
        if (value != null) {
	        context.setVariable(name, value);
	        LOG.debug(String.format("Variable '%s' value set to '%s'", name, value));
        } else {
        	LOG.error(String.format("Null value for variable '%s'", name));
        }
    }

}
