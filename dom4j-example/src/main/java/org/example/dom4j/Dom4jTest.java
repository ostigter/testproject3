package org.example.dom4j;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jaxen.SimpleNamespaceContext;

/**
 * Example application with dom4j.
 * 
 * @author Oscar Stigter
 */
public class Dom4jTest {
	
	private static final Logger LOG = Logger.getLogger(Dom4jTest.class);
	
	public static void main(String[] args) throws Exception {
		final String GEN_NS = "http://www.example.org/Generic";
		final String TR_NS = "http://www.example.org/TestReport";
		
//		// Choice 1: Read document from file.
//		File file = new File("document.xml");
//		SAXReader reader = new SAXReader();
//		Document doc = reader.read(file);

		// Choice 2: Build document in memory.
		Document doc = DocumentFactory.getInstance().createDocument();
		Element rootElement = doc.addElement("TestReport", TR_NS);
		Element headerElement = rootElement.addElement("Header", GEN_NS);
		headerElement.addElement("Id", GEN_NS).setText("001");
		headerElement.addElement("Type", GEN_NS).setText("TestReport");
		Element bodyElement = rootElement.addElement("Body", TR_NS);
		bodyElement.addElement("Date", TR_NS).setText("2010-01-15T12:00:00.000+01");
		bodyElement.addElement("Result", TR_NS).setText("Passed");

		// Write document to console.
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setNewLineAfterDeclaration(false);
		format.setIndentSize(4);
		StringWriter sw = new StringWriter();
		XMLWriter writer = new XMLWriter(sw, format);
		writer.write(doc);
		String content = sw.toString();
		LOG.info("Document:\n" + content);
		
		// Select node with XPath expression.
		SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
		namespaceContext.addNamespace("gen", GEN_NS);
		namespaceContext.addNamespace("tr", TR_NS);
		XPath xpath = DocumentHelper.createXPath("/tr:TestReport/gen:Header/gen:Id/text()");
		xpath.setNamespaceContext(namespaceContext);
		Node node = xpath.selectSingleNode(doc);
		if (node != null) {
			String id = node.getText();
			LOG.info("Document ID: " + id);
		} else {
			LOG.error("Document ID not found");
		}
	}

}
