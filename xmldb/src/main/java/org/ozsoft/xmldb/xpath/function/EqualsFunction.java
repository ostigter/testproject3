package org.ozsoft.xmldb.xpath.function;

import org.ozsoft.xmldb.xpath.XPathException;

public class EqualsFunction implements Function {

	@Override
	public Object invoke(Object... arguments) throws XPathException {
		if (arguments.length != 2) {
			throw new XPathException("Invalid number of arguments (expected 2)");
		}
		
		return arguments[0].equals(arguments[1]);
	}

}
