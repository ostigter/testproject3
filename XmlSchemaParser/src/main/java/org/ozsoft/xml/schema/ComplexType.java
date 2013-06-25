package org.ozsoft.xml.schema;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * W3C XML Schema xs:complexType definition. <br />
 * <br />
 * 
 * Represents a simplified version of an xs:complexType, supporting only a
 * xs:sequence of child elements (xs:element).
 * 
 * @author Oscar Stigter
 */
public class ComplexType extends XmlSchemaType {

    /** Child elements */
    private final List<XmlSchemaElement> elements;

    /** True if a combined CSV field, otherwise false. */
    private boolean isCombinedField = false;

    /**
     * Constructor.
     * 
     * @param name
     *            The type's name.
     */
    /* package */ComplexType(String name) {
        super(name);
        elements = new LinkedList<XmlSchemaElement>();
    }

    /**
     * Returns the child xs:element-s.
     * 
     * @return The child xs:element-s.
     */
    public List<XmlSchemaElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    /**
     * Returns true if this complex type represents a combined CSV field,
     * otherwise false.
     * 
     * @return True if a combined CSV field, otherwise false.
     */
    public boolean isCombinedField() {
        return isCombinedField;
    }

    /**
     * Sets whether this complex type is a combined CSV field.
     * 
     * @param isCombinedField
     *            True if a combined CSV field, otherwise false.
     */
    public void setCombinedField(boolean isCombinedField) {
        this.isCombinedField = isCombinedField;
    }

    /**
     * Adds a child xs:element.
     * 
     * @param element
     *            The child xs:element.
     */
    /* package */void addElement(XmlSchemaElement element) {
        elements.add(element);
    }

}
