package org.ozsoft.xml.schema;

/**
 * Abstract W3C XML Schema type definition (xs:type). <br />
 * <br />
 * 
 * Anonymous, nested types are named "anonymous". <br />
 * <br />
 * 
 * @author Oscar Stigter
 */
public abstract class XmlSchemaType extends XmlSchemaItem {

    /** Base type. */
    private XmlSchemaType baseType;

    /**
     * Constructor.
     * 
     * @param name
     *            The name.
     */
    XmlSchemaType(String name) {
        super(name);
    }

    /**
     * Get the base type.
     * 
     * @return The base type.
     */
    public XmlSchemaType getBaseType() {
        return baseType;
    }

    /**
     * Returns the root type.
     * 
     * @return The root type.
     */
    public XmlRootType getRootType() {
        XmlSchemaType baseType = this;
        while (baseType.getBaseType() != null) {
            baseType = baseType.getBaseType();
        }
        String baseTypeName = baseType.getName();
        if (baseTypeName.equals("string")) {
            return XmlRootType.STRING;
        } else if (baseTypeName.equals("int") || baseTypeName.toLowerCase().contains("integer")) {
            return XmlRootType.INTEGER;
        } else if (baseTypeName.equals("boolean")) {
            return XmlRootType.BOOLEAN;
        } else if (baseTypeName.equals("dateTime")) {
            return XmlRootType.DATE_TIME;
        } else {
            throw new IllegalStateException(String.format("Unknown root type: '%s'", baseTypeName));
        }
    }

    /**
     * Sets the base type.
     * 
     * @param baseType
     *            The base type.
     */
    /* package */void setBaseType(XmlSchemaType baseType) {
        this.baseType = baseType;
    }

}
