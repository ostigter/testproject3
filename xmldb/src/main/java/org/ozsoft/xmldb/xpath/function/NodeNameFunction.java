package org.ozsoft.xmldb.xpath.function;

import org.ozsoft.xmldb.Node;
import org.ozsoft.xmldb.xpath.XPathException;

public class NodeNameFunction implements Function {

	@Override
	public Object invoke(Object... arguments) throws XPathException {
		if (arguments.length != 1) {
			throw new XPathException("Invalid number of arguments (expected 1 node");
		}
		
		Object obj = arguments[0];
		if (!(obj instanceof Node)) {
			throw new XPathException("Invalid argument type (expected a node)");
		}
		
		return ((Node) obj).getName();
	}

}
