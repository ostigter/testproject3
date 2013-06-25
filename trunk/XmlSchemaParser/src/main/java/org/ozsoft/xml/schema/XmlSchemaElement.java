package org.ozsoft.xml.schema;

/**
 * W3C XML Schema element (xs:element).
 * 
 * @author Oscar Stigter
 */
public class XmlSchemaElement extends XmlSchemaItem {

    /** Value for 'minOccurs' representing an unbounded value. */
    private static final int UNBOUNDED = -1;

    /** Minimum number of occurrances. */
    private final int minOccurs;

    /** Maximum number of occurrances (-1 means unbounded). */
    private final int maxOccurs;

    /** Type. */
    private XmlSchemaType type;

    /** Message ID. */
    private String messageId;

    /**
     * Constructor.
     * 
     * @param name
     *            The name.
     * @param minOccurs
     *            The minimum number of occurrances.
     * @param maxOccurs
     *            The maximum number of occurrances.
     */
    XmlSchemaElement(String name, int minOccurs, int maxOccurs) {
        super(name);
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    /**
     * Returns the minimum number of occurrances.
     * 
     * @return The minimum number of occurrances.
     */
    public int getMinOccurs() {
        return minOccurs;
    }

    /**
     * Returns the maximum number of occurrances.
     * 
     * @return The maximum number of occurrances, or -1 if unbounded.
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * Checks whether this schema element is optional (i.e. has a minimum
     * occurrance of 0).
     * 
     * @return True if optional, otherwise false.
     */
    public boolean isOptional() {
        return minOccurs == 0;
    }

    /**
     * Checks whether this schema element is part of a list of identical
     * elements. (i.e. has a maximum occurrence other than 1).
     * 
     * @return True if part of a list, otherwise false.
     */
    public boolean isList() {
        return maxOccurs == UNBOUNDED;
    }

    /**
     * Returns the type.
     * 
     * @return The type.
     */
    public XmlSchemaType getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            The type.
     */
    /* package */void setType(XmlSchemaType type) {
        this.type = type;
    }

    /**
     * Returns the message ID.
     * 
     * @return The message ID, or null if not defined.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the message ID.
     * 
     * @param messageId
     *            The message ID.
     */
    /* package */void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
