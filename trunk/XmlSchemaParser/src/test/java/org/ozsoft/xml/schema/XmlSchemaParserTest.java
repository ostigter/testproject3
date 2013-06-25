package org.ozsoft.xml.schema;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test suite for the <code>SchemaParser</code> component.
 * 
 * @author Oscar Stigter
 */
public class XmlSchemaParserTest {

    /**
     * Tests the parsing of a test schema.
     * 
     * @throws XmlSchemaParseException
     *             If the schema is invalid.
     */
    @Test
    public void testSchema() throws XmlSchemaParseException {
        InputStream is = getClass().getResourceAsStream("/schemas/test.xsd");
        XmlSchema schema = XmlSchemaParser.parse(is);
        Assert.assertEquals(8, schema.getElements().size());
        Assert.assertNotNull(schema);

        // Test message type M5.
        XmlSchemaElement element = schema.getElement("M7");
        Assert.assertNotNull(element);
        Assert.assertEquals("M7", element.getName());
        Assert.assertEquals("1007", element.getMessageId());
        Assert.assertEquals(1, element.getMinOccurs());
        Assert.assertEquals(1, element.getMaxOccurs());
        XmlSchemaType type = element.getType();
        Assert.assertNotNull(type);
        Assert.assertTrue(type instanceof ComplexType);
        ComplexType complexType = (ComplexType) type;
        List<XmlSchemaElement> elements = complexType.getElements();
        Assert.assertEquals(2, elements.size());
        element = elements.get(0);
        Assert.assertEquals("S1", element.getName());
        Assert.assertEquals(0, element.getMinOccurs());
        Assert.assertEquals(1, element.getMaxOccurs());
        element = elements.get(1);
        Assert.assertEquals("C1", element.getName());
        Assert.assertEquals(1, element.getMinOccurs());
        Assert.assertEquals(1, element.getMaxOccurs());

        SimpleType simpleType = (SimpleType) schema.getType("S1");
        Assert.assertNotNull(simpleType);
        Assert.assertEquals("S1", simpleType.getName());
        Assert.assertEquals(3, simpleType.getLength());
        XmlSchemaType baseType = simpleType.getBaseType();
        Assert.assertNotNull(baseType);
        Assert.assertEquals("string", baseType.getName());
        Assert.assertNull(baseType.getBaseType());
        Assert.assertFalse(simpleType.isEnumeration());

        simpleType = (SimpleType) schema.getType("S1b");
        Assert.assertNotNull(simpleType);
        Assert.assertEquals("S1b", simpleType.getName());
        Assert.assertEquals(10, simpleType.getLength());
        baseType = simpleType.getBaseType();
        Assert.assertNotNull(baseType);
        Assert.assertEquals("S1", baseType.getName());
        baseType = baseType.getBaseType();
        Assert.assertNotNull(baseType);
        Assert.assertEquals("string", baseType.getName());
        Assert.assertNull(baseType.getBaseType());
        Assert.assertFalse(simpleType.isEnumeration());

        simpleType = (SimpleType) schema.getType("S2");
        Assert.assertNotNull(type);
        Assert.assertEquals("S2", simpleType.getName());
        Assert.assertEquals(4, simpleType.getLength());
        baseType = simpleType.getBaseType();
        Assert.assertNotNull(baseType);
        Assert.assertEquals("int", baseType.getName());
        Assert.assertFalse(simpleType.isEnumeration());

        simpleType = (SimpleType) schema.getType("S2b");
        Assert.assertNotNull(type);
        Assert.assertEquals("S2b", simpleType.getName());
        Assert.assertEquals(10, simpleType.getLength());
        baseType = simpleType.getBaseType();
        Assert.assertNotNull(baseType);
        Assert.assertEquals("unsignedLong", baseType.getName());
        Assert.assertFalse(simpleType.isEnumeration());

        type = schema.getType("S3");
        Assert.assertNotNull(type);
        Assert.assertTrue(type instanceof SimpleType);
        simpleType = (SimpleType) type;
        Assert.assertEquals("string", simpleType.getBaseType().getName());
        Assert.assertEquals(-1, simpleType.getLength());
        Assert.assertTrue(simpleType.isEnumeration());
        Assert.assertEquals("2", simpleType.getCsvValue("ERROR"));
        Assert.assertEquals("INFO", simpleType.getXmlValue("4"));
    }

}
