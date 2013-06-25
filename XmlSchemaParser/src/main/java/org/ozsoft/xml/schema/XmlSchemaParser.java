package org.ozsoft.xml.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

/**
 * W3C XML Schema parser. <br />
 * <br />
 * 
 * This class is the main entry point for the <code>SchemaParser</code>
 * component. <br />
 * <br />
 * 
 * Use one of the static <code>parse</code> methods to aquire a parsed
 * <code>Schema</code> object from a file, input stream or document. <br />
 * <br />
 * 
 * When parsing a schema from an input stream, included schemas with relative
 * locations will be resolved on the local file system relative to the current
 * working directory. <br />
 * <br />
 * 
 * Supports specific custom attributes in schemas, used as CSV/XML conversion
 * hints. <br />
 * <br />
 * 
 * <b>NOTE:</b> The current version does NOT implement the full W3C XML Schema
 * specification, only the most basic use cases.
 * 
 * @author Oscar Stigter
 */
public class XmlSchemaParser {

    /** W3C XML Schema namespace. */
    private static final Namespace XSD_NAMESPACE = Namespace.getNamespace("http://www.w3.org/2001/XMLSchema");

    /** CSV namespace. */
    private static final Namespace CSV_NAMESPACE = Namespace.getNamespace("http://www.ozsoft.org/CSV");

    /** Name of W3C XML Schema root element. */
    private static final String SCHEMA = "schema";

    /** Name of 'xs:include' element. */
    private static final String INCLUDE = "include";

    /** Name of 'schemaLocation' attribute. */
    private static final String SCHEMA_LOCATION = "schemaLocation";

    /** Name of 'xs:element' element. */
    private static final String ELEMENT = "element";

    /** Name of 'name' attribute. */
    private static final String NAME = "name";

    /** Name of 'type' attribute. */
    private static final String TYPE = "type";

    /** Name of 'minOccurs' attribute for 'xs:element' elements. */
    private static final String MIN_OCCURS = "minOccurs";

    /** Name of 'maxOccurs' attribute for 'xs:element' elements. */
    private static final String MAX_OCCURS = "maxOccurs";

    /** Default value for 'minOccurs' attribute. */
    private static final int DEFAULT_MIN_OCCURS = 1;

    /** Default value for 'maxOccurs' attribute. */
    private static final int DEFAULT_MAX_OCCURS = 1;

    /** Unbounded value for minOccurs/maxOccurs attributes. */
    private static final String UNBOUNDED = "unbounded";

    /** Value for 'maxOccurs' representing an unbounded value. */
    private static final int UNBOUNDED_VALUE = -1;

    /** Name of 'xs:complexType' element. */
    private static final String COMPLEX_TYPE = "complexType";

    /** Name of 'xs:sequence' element. */
    private static final String SEQUENCE = "sequence";

    /** Name of 'xs:simpleType' element. */
    private static final String SIMPLE_TYPE = "simpleType";

    /** Name of 'xs:restriction' element. */
    private static final String RESTRICTION = "restriction";

    /** Name of 'base' attribute. */
    private static final String BASE = "base";

    /** Name of 'xs:length' element. */
    private static final String LENGTH = "length";

    /** Name of 'value' attribute. */
    private static final String VALUE = "value";

    /** Name of 'xs:totalDigits' element. */
    private static final String TOTAL_DIGITS = "totalDigits";

    /** Name of 'xs:enumeration' element. */
    private static final String ENUMERATION = "enumeration";

    /** Name of 'csv:messageId' attribute with the message ID. */
    private static final String MESSAGE_ID = "messageId";

    /** Name of 'csv:combinedField' attribute for combined CSV fields. */
    private static final String COMBINED_FIELD = "combinedField";

    /** Log. */
    private static final Logger LOG = Logger.getLogger(XmlSchemaParser.class);

    /**
     * Private constructor to deny instantiation.
     */
    private XmlSchemaParser() {
        // Empty implementation.
    }

    /**
     * Parses a schema from a file.
     * 
     * @param path
     *            The path to the schema file.
     * 
     * @return The schema.
     * 
     * @throws XmlSchemaParseException
     *             If the file could not be parsed as a valid schema.
     */
    public static XmlSchema parse(String path) throws XmlSchemaParseException {
        return parse(new File(path));
    }

    /**
     * Parses a schema from a file.
     * 
     * @param file
     *            The schema file.
     * 
     * @return The schema.
     * 
     * @throws XmlSchemaParseException
     *             If the file could not be parsed as a valid schema.
     */
    public static XmlSchema parse(File file) throws XmlSchemaParseException {
        // Make sure schema file exists.
        if (!file.isFile()) {
            throw new XmlSchemaParseException("Schema file not found: " + file);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parse schema file: " + file);
        }

        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (IOException e) {
            throw new XmlSchemaParseException("Could not read schema file: " + file, e);
        }

        return parse(is, file.getParentFile());
    }

    /**
     * Parses a schema from an input stream.
     * 
     * @param is
     *            The input stream.
     * @param dir
     *            The working directory (used for schema includes).
     * 
     * @return The schema.
     * 
     * @throws XmlSchemaParseException
     *             If the contents of the input stream could not be parsed as a
     *             valid schema.
     */
    public static XmlSchema parse(InputStream is, File dir) throws XmlSchemaParseException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(is);
        } catch (JDOMException e) {
            throw new XmlSchemaParseException("Could not parse schema (invalid XML document): " + e.getMessage(), e);
        } catch (IOException e) {
            throw new XmlSchemaParseException("Could not read schema", e);
        }

        return parse(doc, dir);
    }

    /**
     * Parses a schema from an input stream.
     * 
     * @param is
     *            The input stream.
     * 
     * @return The schema.
     * 
     * @throws XmlSchemaParseException
     *             If the contents of the input stream could not be parsed as a
     *             valid schema.
     */
    public static XmlSchema parse(InputStream is) throws XmlSchemaParseException {
        File dir = new File(".");
        return parse(is, dir);
    }

    /**
     * Parses a schema from a document. <br />
     * <br />
     * 
     * Recursively processes all toplevel elements (either xs:element,
     * xs:simpleType or xs:complexType). <br />
     * <br />
     * 
     * Elements with a named type reference ('type' attribute) are first
     * associated with an <code>UnresolvedType</code> instance, with is later
     * replaced with the actual named type (assuming it is defined somewhere
     * within the schema).
     * 
     * @param doc
     *            The document.
     * @param dir
     *            The working directory (used for schema includes).
     * 
     * @return The schema.
     * 
     * @throws XmlSchemaParseException
     *             If the document could not be parsed as a valid schema.
     */
    private static XmlSchema parse(Document doc, File dir) throws XmlSchemaParseException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start parsing schema");
        }

        long startTime = System.currentTimeMillis();

        // Make sure we have an actual W3C XML Schema.
        checkSchema(doc);

        // Create a new schema representation.
        XmlSchema schema = new XmlSchema();

        // Recursively process all toplevel elements.
        Element schemaElement = doc.getRootElement();
        List<Element> children = schemaElement.getChildren();
        for (Element child : children) {
            parse((Element) child, schema, dir);
        }

        // Check all toplevel elements and types, replacing all placeholder
        // types.
        for (XmlSchemaElement element : schema.getElements()) {
            replaceTypePlaceholders(element, schema);
        }
        for (XmlSchemaType type : schema.getTypes()) {
            if (type instanceof ComplexType) {
                replaceTypePlaceholders((ComplexType) type, schema);
            }
        }

        if (LOG.isDebugEnabled()) {
            long duration = System.currentTimeMillis() - startTime;
            LOG.debug(String.format("Finished parsing schema in %d ms", duration));
        }

        return schema;
    }

    /**
     * Checks whether a document is a W3C XML Schema, by looking at the root
     * element name and namespace.
     * 
     * @param doc
     *            The document.
     * 
     * @throws XmlSchemaParseException
     *             If the document is not a W3C XML Schema.
     */
    private static void checkSchema(Document doc) throws XmlSchemaParseException {
        Element rootElement = doc.getRootElement();
        String name = rootElement.getName();
        String uri = rootElement.getNamespaceURI();
        if (!name.equals(SCHEMA) || !uri.equals(XSD_NAMESPACE.getURI())) {
            throw new XmlSchemaParseException("Document is not a W3C XML Schema");
        }
    }

    /**
     * Parses an XML element of a schema.
     * 
     * @param element
     *            The element.
     * @param schema
     *            The schema being built.
     * @param dir
     *            The working directory (used for schema includes).
     * 
     * @throws XmlSchemaParseException
     *             If the element could not be parsed.
     */
    private static void parse(Element element, XmlSchema schema, File dir) throws XmlSchemaParseException {
        String name = element.getName();
        if (name.equals(ELEMENT)) {
            XmlSchemaElement schemaElement = parseElement(element, schema);
            // Add toplevel xs:element.
            schema.addElement(schemaElement);
        } else if (name.equals(SIMPLE_TYPE)) {
            parseSimpleType(element, schema);
        } else if (name.equals(COMPLEX_TYPE)) {
            parseComplexType(element, schema);
        } else if (name.equals(INCLUDE)) {
            parseInclude(element, schema, dir);
        } else {
            String msg = String.format("Unsupported toplevel XML element: '%s'", name);
            throw new XmlSchemaParseException(msg);
        }
    }

    /**
     * Parses an XML element as xs:include.
     * 
     * @param element
     *            The XML element.
     * @param schema
     *            The schema being built.
     * @param parentDir
     *            The parent schema's directory.
     * 
     * @throws XmlSchemaParseException
     *             If the element is not a valid xs:include, or the included
     *             schema file could not found.
     */
    private static void parseInclude(Element element, XmlSchema schema, File parentDir) throws XmlSchemaParseException {
        // Get included schema location.
        Attribute schemaLocationAttr = element.getAttribute(SCHEMA_LOCATION);
        if (schemaLocationAttr == null) {
            throw new XmlSchemaParseException("Missing 'schemaLocation' attribute for xs:include element");
        }
        String schemaPath = schemaLocationAttr.getValue();
        // Try an absolute path.
        File schemaFile = new File(schemaPath);
        if (!schemaFile.isFile()) {
            // Otherwise try a path relative to the parent schema's directory.
            schemaFile = new File(parentDir, schemaPath);
            if (!schemaFile.isFile()) {
                throw new XmlSchemaParseException("Could not find included schema file: " + schemaPath);
            }
        }

        // Parse included schema.
        XmlSchema includedSchema = parse(schemaFile);

        // Merge included element and type definitions into parent schema.
        for (XmlSchemaElement schemaElement : includedSchema.getElements()) {
            schema.addElement(schemaElement);
        }
        for (XmlSchemaType schemaType : includedSchema.getTypes()) {
            schema.addType(schemaType);
        }
    }

    /**
     * Parses an XML element as xs:element definition.
     * 
     * @param element
     *            The XML element.
     * @param schema
     *            The schema being built.
     * 
     * @return The xs:element definition.
     * 
     * @throws XmlSchemaParseException
     *             If the XML element is not a valid xs:element definition.
     */
    private static XmlSchemaElement parseElement(Element element, XmlSchema schema) throws XmlSchemaParseException {
        // Get the name.
        Attribute attr = element.getAttribute(NAME);
        if (attr == null) {
            throw new XmlSchemaParseException("Missing 'name' attribute for 'element' element");
        }
        String name = attr.getValue();

        // Get the (optional) minimum number of occurrances.
        int minOccurs = DEFAULT_MIN_OCCURS;
        attr = element.getAttribute(MIN_OCCURS);
        if (attr != null) {
            minOccurs = Integer.parseInt(attr.getValue());
        }

        // Get the (optional) maximum number of occurrances.
        int maxOccurs = DEFAULT_MAX_OCCURS;
        attr = element.getAttribute(MAX_OCCURS);
        if (attr != null) {
            String attrValue = attr.getValue();
            if (attrValue.equals(UNBOUNDED)) {
                maxOccurs = UNBOUNDED_VALUE;
            } else {
                maxOccurs = Integer.parseInt(attrValue);
            }
        }

        // Get the type, either from the 'type' attribute or the nested
        // anonymous type definition.
        XmlSchemaType type = null;
        attr = element.getAttribute(TYPE);
        if (attr != null) {
            // Get the type from the 'type' attribute (without namespace
            // prefix).
            String typeName = attr.getValue();
            int pos = typeName.indexOf(':');
            if (pos != -1) {
                typeName = typeName.substring(pos + 1);
            }
            // Check whether this type has already been parsed earlier.
            type = schema.getType(typeName);
            if (type == null) {
                // If not, use a temporary placeholder.
                type = new UnresolvedType(typeName);
            }
        } else {
            // Parse the type from the nested anonymous type definition.
            Element typeElement = element.getChild(SIMPLE_TYPE, XSD_NAMESPACE);
            if (typeElement != null) {
                type = parseSimpleType(typeElement, schema);
            } else {
                typeElement = element.getChild(COMPLEX_TYPE, XSD_NAMESPACE);
                if (typeElement != null) {
                    type = parseComplexType(typeElement, schema);
                } else {
                    String msg = String.format("No type defined for element '%s'", name);
                    throw new XmlSchemaParseException(msg);
                }
            }
        }

        // Create the schema element.
        XmlSchemaElement schemaElement = new XmlSchemaElement(name, minOccurs, maxOccurs);
        schemaElement.setType(type);

        // Parse any 'csv:messageId' attribute with message ID.
        Attribute attribute = element.getAttribute(MESSAGE_ID, CSV_NAMESPACE);
        if (attribute != null) {
            schemaElement.setMessageId(attribute.getValue());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Parsed xs:element '%s' with type '%s'", name, type));
        }

        return schemaElement;
    }

    /**
     * Parses an XML element as xs:simpleType definition.
     * 
     * @param element
     *            The XML element.
     * @param schema
     *            The schema being built.
     * 
     * @throws XmlSchemaParseException
     *             If the XML element is not a valid xs:simpleType.
     */
    private static SimpleType parseSimpleType(Element element, XmlSchema schema) throws XmlSchemaParseException {
        // Get the (optional) name.
        String name = null;
        Attribute attr = element.getAttribute(NAME);
        if (attr != null) {
            name = attr.getValue();
        }

        SimpleType simpleType = new SimpleType(name);

        // Get type's restriction base.
        Element restrictionElement = element.getChild(RESTRICTION, XSD_NAMESPACE);
        if (restrictionElement == null) {
            throw new XmlSchemaParseException("No 'xs:restriction' element in 'xs:simpleType' element");
        }
        Attribute baseAttribute = restrictionElement.getAttribute(BASE);
        if (baseAttribute == null) {
            throw new XmlSchemaParseException("No 'base' attribute on 'xs:restriction' element");
        }
        String base = baseAttribute.getValue();
        int pos = base.indexOf(':');
        if (pos != -1) {
            base = base.substring(pos + 1);
        }
        XmlSchemaType baseType = schema.getType(base);
        if (baseType == null) {
            baseType = new UnresolvedType(base);
        }
        simpleType.setBaseType(baseType);

        // Check for enumeration values.
        List<Element> enumElements = restrictionElement.getChildren(ENUMERATION, XSD_NAMESPACE);
        if (enumElements.size() > 0) {
            for (Element enumElement : enumElements) {
                // Get (XML) value.
                Attribute xmlValueAttribute = enumElement.getAttribute(VALUE);
                if (xmlValueAttribute == null) {
                    throw new XmlSchemaParseException(String.format("Missing 'value' attribute on 'xs:enumeration' element"));
                }
                // Get (optional) CSV value.
                Attribute csvValueAttribute = enumElement.getAttribute(VALUE, CSV_NAMESPACE);
                if (csvValueAttribute != null) {
                    // Only store value if a CSV value is specified.
                    simpleType.addEnumValue(xmlValueAttribute.getValue(), csvValueAttribute.getValue());
                }
            }
        }

        // Set (optional) length.
        // First try custom 'csv:length' attribute...
        Attribute lengthAttribute = element.getAttribute(LENGTH, CSV_NAMESPACE);
        if (lengthAttribute != null) {
            simpleType.setLength(Integer.valueOf(lengthAttribute.getValue()));
        } else {
            // ...then try 'xs:length' element (for string types)...
            Element lengthElement = restrictionElement.getChild(LENGTH, XSD_NAMESPACE);
            if (lengthElement != null) {
                Attribute valueAttribute = lengthElement.getAttribute(VALUE);
                if (valueAttribute != null) {
                    simpleType.setLength(Integer.valueOf(valueAttribute.getValue()));
                }
            } else {
                // ...finally try 'xs:totalDigits' element (for integer types).
                lengthElement = restrictionElement.getChild(TOTAL_DIGITS, XSD_NAMESPACE);
                if (lengthElement != null) {
                    Attribute valueAttribute = lengthElement.getAttribute(VALUE);
                    if (valueAttribute != null) {
                        simpleType.setLength(Integer.valueOf(valueAttribute.getValue()));
                    }
                }
            }
        }

        // If type is named (not anonymous), store it.
        if (name != null) {
            schema.addType(simpleType);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Parsed xs:simpleType '%s'", simpleType));
        }

        return simpleType;
    }

    /**
     * Parses an XML element as xs:complexType definition.
     * 
     * Recursively processes the xs:element-s inside its xs:sequence.
     * 
     * @param element
     *            The XML element.
     * @param schema
     *            The schema being built.
     * 
     * @throws XmlSchemaParseException
     *             If the XML element is not a valid xs:complexType.
     */
    private static ComplexType parseComplexType(Element element, XmlSchema schema) throws XmlSchemaParseException {
        // Get the (optional) name.
        String name = null;
        Attribute attr = element.getAttribute(NAME);
        if (attr != null) {
            name = attr.getValue();
        }

        // Create the type.
        ComplexType complexType = new ComplexType(name);

        // Check for combined CSV field.
        Attribute attribute = element.getAttribute(COMBINED_FIELD, CSV_NAMESPACE);
        if (attribute != null) {
            complexType.setCombinedField(Boolean.parseBoolean(attribute.getValue()));
        }

        // Recursively process all child elements inside it's xs:sequence.
        Element sequenceElement = element.getChild(SEQUENCE, XSD_NAMESPACE);
        if (sequenceElement == null) {
            String msg = String.format("No xs:sequence element inside xs:complexType");
            throw new XmlSchemaParseException(msg);
        }
        List<Element> childElements = sequenceElement.getChildren(ELEMENT, XSD_NAMESPACE);
        for (Element childElement : childElements) {
            XmlSchemaElement schemaElement = parseElement(childElement, schema);
            complexType.addElement(schemaElement);
        }

        // If type is named (not anonymous), store it.
        if (name != null) {
            schema.addType(complexType);
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Parsed xs:complexType '%s'", complexType));
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parsed anonymous xs:complexType");
            }
        }

        return complexType;
    }

    /**
     * Recursively processes an xs:element definition, replacing each type
     * placeholder with the actual type. <br />
     * <br />
     * 
     * If the xs:element has a xs:complexType as its type, the latter is
     * processed recursively.
     * 
     * @param element
     *            The xs:element.
     * @param schema
     *            The schema.
     * 
     * @throws XmlSchemaParseException
     *             If a type definition could not be found.
     */
    private static void replaceTypePlaceholders(XmlSchemaElement element, XmlSchema schema) throws XmlSchemaParseException {
        XmlSchemaType type = element.getType();
        if (type instanceof UnresolvedType) {
            // Replace placeholder.
            String typeName = type.getName();
            type = schema.getType(typeName);
            if (type != null) {
                element.setType(type);
            } else {
                String msg = String.format("Type definition not found: '%s'", typeName);
                throw new XmlSchemaParseException(msg);
            }
        } else {
            // Recurse into actual type.
            replaceTypePlaceholders(type, schema);
        }
    }

    /**
     * Recursively processes a type definition, replacing each type placeholder
     * with the actual type. <br />
     * <br />
     * 
     * In case of a xs:complexType, recursively processes each of its child
     * elements.
     * 
     * @param type
     *            The type definition.
     * @param schema
     *            The schema.
     * 
     * @throws XmlSchemaParseException
     *             If a type definition could not be found.
     */
    private static void replaceTypePlaceholders(XmlSchemaType type, XmlSchema schema) throws XmlSchemaParseException {
        if (type instanceof ComplexType) {
            ComplexType complexType = (ComplexType) type;
            for (XmlSchemaElement element : complexType.getElements()) {
                replaceTypePlaceholders(element, schema);
            }
        }
    }

    /**
     * Placeholder for a named W3C XML Schema type definition that needs to be
     * replaced with the actual type.
     * 
     * Only used during schema parsing.
     * 
     * @author Oscar Stigter
     */
    private static class UnresolvedType extends XmlSchemaType {

        /**
         * Constructor.
         * 
         * @param name
         *            The name.
         */
        /* package */UnresolvedType(String name) {
            super(name);
        }

    }

}
