package org.ozsoft.xmldb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Parser extends DefaultHandler {
    
    private final Stack<Element> elements = new Stack<Element>();
    
    private Element element;
    
    private final StringBuilder textBuffer = new StringBuilder();
    
    private Text text;
    
    public Document parse(File file) {
        Document doc = null;
        try {
            doc = parse(new FileInputStream(file));
        } catch (IOException e) {
            //TODO
        }
        return doc;
    }
    
    public Document parse(InputStream is) {
        Document doc = new Document(null);
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser parser = spf.newSAXParser();
            parser.parse(is, this);
//            doc.setRootElement(elements.pop());
        } catch (ParserConfigurationException e) {
            //TODO
        } catch (SAXException e) {
            //TODO
        } catch (IOException e) {
            //TODO
        }
        return doc;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr) {
        element = new Element(qName);
        
        int count = attr.getLength();
        for (int i = 0; i < count; i++) {
            element.addAttribute(attr.getQName(i), attr.getValue(i));
        }
        
        if (elements.size() > 0) {
            elements.peek().addElement(element);
        }
        elements.push(element);
        
        textBuffer.delete(0, textBuffer.length());
    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        if (textBuffer.length() != 0) {
            String value = textBuffer.toString().trim();
            if (value.length() > 0) {
                text = new Text(textBuffer.toString());
                elements.peek().addText(text);
            }
        }
        elements.pop();
    }


    @Override
    public void characters(char[] ch, int start, int length) {
        textBuffer.append(ch, start, length);
    }

}
