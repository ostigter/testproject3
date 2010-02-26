package org.ozsoft.xmldb.test;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.xmldb.Attribute;
import org.ozsoft.xmldb.Document;
import org.ozsoft.xmldb.Element;
import org.ozsoft.xmldb.Node;
import org.ozsoft.xmldb.xpath.BooleanLiteral;
import org.ozsoft.xmldb.xpath.ChildAttributeSelection;
import org.ozsoft.xmldb.xpath.ChildElementSelection;
import org.ozsoft.xmldb.xpath.Expression;
import org.ozsoft.xmldb.xpath.IntegerLiteral;
import org.ozsoft.xmldb.xpath.NodeSelection;
import org.ozsoft.xmldb.xpath.StringLiteral;
import org.ozsoft.xmldb.xpath.XPathException;
import org.ozsoft.xmldb.xpath.function.Function;
import org.ozsoft.xmldb.xpath.function.NodeNameFunction;
import org.ozsoft.xmldb.xpath.function.NodeTypeFunction;

public class XPathTest {
	
	@Test
	@SuppressWarnings("unchecked")
	public void xpath() {
		Expression expr = null;
		
		expr = new BooleanLiteral(true);
		Assert.assertEquals(true, expr.evaluate(null));
		expr = new BooleanLiteral(false);
		Assert.assertEquals(false, expr.evaluate(null));

		expr = new IntegerLiteral(1);
		Assert.assertEquals(1, expr.evaluate(null));
		expr = new IntegerLiteral(-1);
		Assert.assertEquals(-1, expr.evaluate(null));

		expr = new StringLiteral("");
		Assert.assertEquals("", expr.evaluate(null));
		expr = new StringLiteral("abc");
		Assert.assertEquals("abc", expr.evaluate(null));
		
		Document doc = new Document("foo-001.xml");
		Element documentElement = doc.addRootElement("document");
		Element headerElement = documentElement.addElement("header");
		Element idElement = headerElement.addElement("id", "foo-001");
		idElement.addAttribute("type", "string");
		headerElement.addElement("type", "Foo");
		
		expr = new ChildElementSelection("document");
		Object result = expr.evaluate(doc);
		Assert.assertNotNull(result);
		Assert.assertTrue(result instanceof List);
		List<Element> elements = (List<Element>) result;
		Assert.assertNotNull(elements);
		Assert.assertEquals(1, elements.size());
		documentElement = elements.get(0); 
		Assert.assertEquals("document", documentElement.getName());
		
		expr = new ChildElementSelection("type");
		result = expr.evaluate(headerElement);
		Assert.assertNotNull(result);
		Assert.assertTrue(result instanceof List);
		elements = (List<Element>) result;
		Assert.assertNotNull(elements);
		Assert.assertEquals(1, elements.size());
		headerElement = elements.get(0); 
		Assert.assertEquals("type", headerElement.getName());
		Assert.assertEquals("Foo", headerElement.getText());
		Attribute attr = idElement.getAttribute("type");
		Assert.assertNotNull(attr);
		Assert.assertEquals("string", attr.getValue());
		
		NodeSelection nodeExpr = new NodeSelection();
		nodeExpr.add(new ChildElementSelection("document"));
		nodeExpr.add(new ChildElementSelection("header"));
		nodeExpr.add(new ChildElementSelection("id"));
		nodeExpr.add(new ChildAttributeSelection("type"));
		result = nodeExpr.evaluate(doc);
		Assert.assertNotNull(result);
		Assert.assertTrue(result instanceof List);
		List<Node> nodes = (List<Node>) result;
		Assert.assertNotNull(nodes);
		Assert.assertEquals(1, nodes.size());
		Attribute typeAttribute = (Attribute) nodes.get(0);
		Assert.assertEquals("type", typeAttribute.getName());
		Assert.assertEquals("string", typeAttribute.getValue());
		
		Function function = new NodeNameFunction();
		try {
			Assert.assertEquals("foo-001.xml", function.invoke(doc));
			Assert.assertEquals("id", function.invoke(idElement));
			Assert.assertEquals("type", function.invoke(typeAttribute));
		} catch (XPathException e) {
			Assert.fail(e.getMessage());
		}

		function = new NodeTypeFunction();
		try {
			Assert.assertEquals("document", function.invoke(doc));
			Assert.assertEquals("element", function.invoke(idElement));
			Assert.assertEquals("attribute", function.invoke(typeAttribute));
			Assert.assertEquals("text", function.invoke(idElement.getChild(0)));
		} catch (XPathException e) {
			Assert.fail(e.getMessage());
		}
	}

}
