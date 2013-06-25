package org.ozsoft.xml.schema;

/**
 * Abstract, named W3C XML Schema item being either an xs:element or an xs:type. <br />
 * <br />
 * 
 * Anonymous, nested items are named "(anonymous)".
 * 
 * @author Oscar Stigter
 */
public abstract class XmlSchemaItem {

    /** Name for anonymous types. */
    private static final String ANONYMOUS = "(anonymous)";

    /** Name. */
    private final String name;

    /**
     * Constructor.
     * 
     * @param name
     *            The name.
     */
    public XmlSchemaItem(String name) {
        if (name == null) {
            this.name = ANONYMOUS;
        } else {
            this.name = name;
        }
    }

    /**
     * Returns the name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }

}
