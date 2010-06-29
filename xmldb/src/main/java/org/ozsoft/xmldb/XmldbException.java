package org.ozsoft.xmldb;

public class XmldbException extends Exception {
    
    private static final long serialVersionUID = 8781413252241931719L;

    public XmldbException(String message) {
        super(message);
    }

    public XmldbException(String message, Throwable cause) {
        super(message, cause);
    }

}
