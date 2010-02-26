package org.ozsoft.xmldb.xpath.function;

import org.ozsoft.xmldb.Attribute;
import org.ozsoft.xmldb.Document;
import org.ozsoft.xmldb.Element;
import org.ozsoft.xmldb.Node;
import org.ozsoft.xmldb.Text;
import org.ozsoft.xmldb.xpath.XPathException;

public class NodeTypeFunction implements Function {

	@Override
	public Object invoke(Object... arguments) throws XPathException {
		if (arguments.length != 1) {
			throw new XPathException("Invalid number of arguments (expected 1 node");
		}
		
		Object obj = arguments[0];
		if (!(obj instanceof Node)) {
			throw new XPathException("Invalid argument type (expected a node)");
		}
		
		String name = null;
		if (obj instanceof Document) {
			name = "document";
		} else if (obj instanceof Element) {
			name = "element";
		} else if (obj instanceof Attribute) {
			name = "attribute";
		} else if (obj instanceof Text) {
			name = "text";
		} else {
			throw new IllegalStateException("Invalid node type");
		}
		return name;
	}

}
