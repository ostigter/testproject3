package org.ozsoft.xml.schema;

/**
 * Exception thrown while parsing a W3C XML Schema.
 * 
 * @author Oscar Stigter
 */
public class XmlSchemaParseException extends Exception {

    /** Serial version UID. */
    private static final long serialVersionUID = -408487850979025919L;

    /**
     * Constructor with a message only.
     * 
     * @param message
     *            The message describing the problem.
     */
    XmlSchemaParseException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and a nested exception as the cause.
     * 
     * @param message
     *            The message describing the problem.
     * @param cause
     *            The nested exception as the cause.
     */
    XmlSchemaParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
