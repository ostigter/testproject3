package org.ozsoft.xmldb;

public class XmldbException extends Exception {
    
    private static final long serialVersionUID = 8972251689403547077L;

    public XmldbException(String message) {
	super(message);
    }
    
    public XmldbException(String message, Throwable t) {
	super(message, t);
    }

}
