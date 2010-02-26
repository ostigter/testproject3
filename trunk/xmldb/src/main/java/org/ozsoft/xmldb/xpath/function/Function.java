package org.ozsoft.xmldb.xpath.function;

import org.ozsoft.xmldb.xpath.XPathException;

public interface Function {
	
	Object invoke(Object... arguments) throws XPathException;

}
