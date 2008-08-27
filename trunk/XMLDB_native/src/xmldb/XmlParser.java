package xmldb;


import java.io.File;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


public class XmlParser extends DefaultHandler {
    

	private static SAXParser parser;
    
	private Stack<Node> nodes = new Stack<Node>();
	
    private Element element;
    
    private StringBuilder textBuffer = new StringBuilder();
    
    private Text text;
    
    
    public XmlParser() {
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (Exception ex) {
            System.err.println("ERROR: Could not create SAXParserFactory: " + ex);
        }
    }
    

    public void parse(File file, Document doc) throws XmldbException {
    	nodes.clear();
    	nodes.push(doc);
    	try {
    		parser.parse(file, this);
    	} catch (Exception e) {
    		throw new XmldbException("Could not parse document '" + file
    				+ "': " + e.getMessage());
    	}
    }


    @Override // DefaultHandler
    public void startElement(
    		String uri, String localName, String qName, Attributes attr) {
        element = new Element(qName);
        
        int count = attr.getLength();
        for (int i = 0; i < count; i++) {
            element.addAttribute(new Attribute(
            		attr.getQName(i), attr.getValue(i), element.getId()));
        }
        
        nodes.peek().addChild(element.getId());
        nodes.push(element);
        
        textBuffer.delete(0, textBuffer.length());
    }


    @Override // DefaultHandler
    public void endElement(String uri, String localName, String qName) {
        if (textBuffer.length() != 0) {
            String value = textBuffer.toString().trim();
            if (value.length() > 0) {
                text = new Text(textBuffer.toString());
                nodes.peek().addChild(text.getId());
            }
        }
        nodes.pop();
    }


    @Override // DefaultHandler
    public void characters(char[] ch, int start, int length) {
        textBuffer.append(ch, start, length);
    }

}
