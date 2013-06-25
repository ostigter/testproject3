package org.ozsoft.xml.schema;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * W3C XML Schema instance. <br />
 * <br />
 * 
 * Represents a <b>simplified</b> version of a schema with: <br />
 * <ul>
 * <li>toplevel element definitions</li>
 * <li>type definitions (<code>xs:simpleType</code> and
 * <code>xs:complexType</code>)</li>
 * <li>CSV/XML conversion hints</li>
 * </ul>
 * 
 * @author Oscar Stigter
 */
public class XmlSchema {

    /** Toplevel elements mapped by their name. */
    private final Map<String, XmlSchemaElement> elements;

    /** Types mapped by their name. */
    private final Map<String, XmlSchemaType> types;

    /**
     * Constructor.
     */
    XmlSchema() {
        elements = new HashMap<String, XmlSchemaElement>();
        types = new HashMap<String, XmlSchemaType>();
        addDefaultTypes();
    }

    /**
     * Returns all toplevel elements (xs:element).
     * 
     * @return The toplevel elements.
     */
    public Collection<XmlSchemaElement> getElements() {
        return Collections.unmodifiableCollection(elements.values());
    }

    /**
     * Returns a toplevel element (xs:element) by its name.
     * 
     * @param name
     *            The element name.
     * 
     * @return The toplevel element.
     */
    public XmlSchemaElement getElement(String name) {
        return elements.get(name);
    }

    /**
     * Returns all named types.
     * 
     * @return The named types.
     */
    public Collection<XmlSchemaType> getTypes() {
        return Collections.unmodifiableCollection(types.values());
    }

    /**
     * Returns a type by its name.
     * 
     * @param name
     *            The type name.
     * 
     * @return The type.
     */
    public XmlSchemaType getType(String name) {
        return types.get(name);
    }

    /**
     * Adds a toplevel element (xs:element).
     * 
     * @param element
     *            The toplevel element.
     */
    /* package */void addElement(XmlSchemaElement element) {
        elements.put(element.getName(), element);
    }

    /**
     * Adds a type definiton.
     * 
     * @param type
     *            The type definition.
     */
    /* package */void addType(XmlSchemaType type) {
        String name = type.getName();
        if (!types.containsKey(name)) {
            types.put(name, type);
        }
    }

    /**
     * Adds the default W3C XML Schema types.
     */
    private void addDefaultTypes() {
        types.put("string", new SimpleType("string"));
        types.put("int", new SimpleType("int"));
        types.put("long", new SimpleType("long"));
        types.put("boolean", new SimpleType("boolean"));
        types.put("dateTime", new SimpleType("dateTime"));
    }

}
