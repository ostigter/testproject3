package xquery;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.InputSource;


/**
 * Test suite for the XQueryProcessor class.
 * 
 * @author Oscar Stigter
 */
public class XQueryProcessorTest {


    @Test
    public void main() {
        try {
            // Setup input documents.
            InputStream is1 = new FileInputStream(
                    new File("test/dat/db/data/doc_001.xml"));
            Source doc1 = new SAXSource(new InputSource(is1));
            InputStream is2 = new FileInputStream(
                    new File("test/dat/db/data/doc_002.xml"));
            Source doc2 = new SAXSource(new InputSource(is2));
            
            // Setup XQuery processor.
            XQueryProcessor xqp = new SaxonXQueryProcessor();
            xqp.addModuleLocation("test/dat/db/modules");
            
            // Call XQuery function.
            OutputStream result = xqp.executeFunction(
                    "http://www.example.com/converter", "convert",
                    doc1, doc2, new String[] {"multiply"});
            
            // Print query result.
            System.out.print(result);
            
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }


}
