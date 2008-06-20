package streamvalidator;


import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class MyErrorHandler implements ErrorHandler {


    public void error(SAXParseException e) throws SAXException {
        System.out.println("SAX error: " + e.getMessage());
    }

    
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("SAX fatal error: " + e.getMessage());
    }


    public void warning(SAXParseException e) throws SAXException {
        System.out.println("SAX parser warning: " + e.getMessage());
    }


}
