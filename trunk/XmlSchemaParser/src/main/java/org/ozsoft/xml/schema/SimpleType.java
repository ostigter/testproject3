package org.ozsoft.xml.schema;

import java.util.HashMap;
import java.util.Map;

/**
 * W3C XML Schema xs:simpleType definition. <br />
 * <br />
 * 
 * A xs:SimpleType may have a fixed length, used for CSV/XML conversion.
 * 
 * @author Oscar Stigter
 */
public class SimpleType extends XmlSchemaType {

    /** Value length. */
    private int length = -1;

    /** The XML values mapped by CSV value. */
    private final Map<String, String> xmlValues;

    /** The CSV values mapped by XML value. */
    private final Map<String, String> csvValues;

    /**
     * Constructor.
     * 
     * @param name
     *            The type name.
     */
    /* package */SimpleType(String name) {
        super(name);
        xmlValues = new HashMap<String, String>();
        csvValues = new HashMap<String, String>();
    }

    /**
     * Returns the value length.
     * 
     * @return The value length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the value length.
     * 
     * @param length
     *            The value length.
     */
    /* package */void setLength(int length) {
        this.length = length;
    }

    /**
     * Indicates whether this type is an enumeration.
     * 
     * @return True if this type is an enumeration, otherwise false.
     */
    public boolean isEnumeration() {
        return !xmlValues.isEmpty();
    }

    /**
     * Adds a value.
     * 
     * @param xmlValue
     *            The XML value.
     * @param csvValue
     *            The CSV value.
     */
    /* package */void addEnumValue(String xmlValue, String csvValue) {
        xmlValues.put(csvValue, xmlValue);
        csvValues.put(xmlValue, csvValue);
    }

    /**
     * Returns the XML value corresponding with a CSV value. <br />
     * <br />
     * 
     * If no CSV value was specified, the XML value is returned.
     * 
     * @param csvValue
     *            The CSV value.
     * 
     * @return The XML value.
     */
    public String getXmlValue(String csvValue) {
        String xmlValue = xmlValues.get(csvValue);
        if (xmlValue == null) {
            xmlValue = csvValue;
        }
        return xmlValue;
    }

    /**
     * Returns the CSV value corresponding with an XML value. <br />
     * <br />
     * 
     * If no specific CSV value was specified, the XML value is returned.
     * 
     * @param xmlValue
     *            The XML value.
     * 
     * @return The CSV value.
     */
    public String getCsvValue(String xmlValue) {
        String csvValue = csvValues.get(xmlValue);
        if (csvValue == null) {
            csvValue = xmlValue;
        }
        return csvValue;
    }

}
