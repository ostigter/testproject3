package org.ozsoft.xslt;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Example application showing how to do XSLT transformations on XML documents
 * using JAXP.
 * 
 * @author Oscar Stigter
 */
public class Main {

    public static void main(String[] args) {
        Source stylesheet = new StreamSource("test.xsl");
        Source source = new StreamSource("test.xml");
        Result result = new StreamResult(System.out);
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer(stylesheet);
            transformer.setParameter("documentId", "foo-001");
            transformer.setParameter("machineId", "1001");
            transformer.setParameter("lotId", "001");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            System.err.println("Could not compile stylesheet: " + e.getMessage());
        } catch (TransformerException e) {
            System.err.println("Could not transform document: " + e.getMessage());
        }
    }

}
