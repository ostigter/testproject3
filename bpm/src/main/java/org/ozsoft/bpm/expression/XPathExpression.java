package org.ozsoft.bpm.expression;

import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.XPathContext;

import org.ozsoft.bpm.ProcessContext;
import org.ozsoft.bpm.exception.BpmExpressionException;

public class XPathExpression implements Expression {
	
	private final String contextProperty;
	
	private final String expression;
	
	private final XPathContext xpathContext;
	
	public XPathExpression(String contextProperty, String expression, XPathContext xpathContext) {
		this.contextProperty = contextProperty;
		this.expression = expression;
		this.xpathContext = xpathContext;
	}

	@Override
	public Object evaluate(ProcessContext context) throws BpmExpressionException {
		Object value = context.getProperty(contextProperty);
		if (value == null) {
			String msg = String.format("Context property '%s' not set", contextProperty);
			throw new IllegalStateException(msg);
		}
		if (!(value instanceof Document)) {
			String msg = String.format("Context property '%s' not a Document", contextProperty);
			throw new BpmExpressionException(msg);
		}
		
		Document doc = (Document) value;
		Nodes nodes = doc.query(expression, xpathContext);
		if (nodes.size() == 0) {
			String msg = String.format("Expression '%s' evaluated to an empty result", expression);
			throw new BpmExpressionException(msg);
		}
		if (nodes.size() > 1) {
			String msg = String.format("Expression '%s' evaluated to multiple results", expression);
			throw new BpmExpressionException(msg);
		} 
		Object result = nodes.get(0);
		return result;
	}

}
