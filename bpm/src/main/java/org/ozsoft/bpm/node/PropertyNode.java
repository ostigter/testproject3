package org.ozsoft.bpm.node;

import org.apache.log4j.Logger;
import org.ozsoft.bpm.ProcessContext;
import org.ozsoft.bpm.exception.BpmExpressionException;
import org.ozsoft.bpm.expression.Expression;

/**
 * Node to set a property in the process context.
 * 
 * @author Oscar Stigter
 */
public class PropertyNode extends Node {
	
	private static final Logger LOG = Logger.getLogger(PropertyNode.class);
	
	private final String property;
	
	private final Expression expression;

	public PropertyNode(String name, String property, Expression expression) {
		super(name);
		this.property = property;
		this.expression = expression;
	}

	@Override
	public void performAction(ProcessContext context) {
		try {
			Object value = expression.evaluate(context);
			context.setProperty(property, value);
		} catch (BpmExpressionException e) {
			LOG.error("Could not evaluate expression", e);
		}
	}

}
