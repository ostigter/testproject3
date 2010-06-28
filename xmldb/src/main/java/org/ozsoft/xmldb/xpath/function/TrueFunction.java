package org.ozsoft.xmldb.xpath.function;

import org.ozsoft.xmldb.xpath.XPathException;

public class TrueFunction implements Function {

	@Override
	public Object invoke(Object... arguments) throws XPathException {
		if (arguments.length != 0) {
			throw new XPathException("Invalid number of arguments (expected none)");
		}
		
		return true;
	}

}
