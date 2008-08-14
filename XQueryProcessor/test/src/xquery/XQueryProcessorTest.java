package xquery;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;

import xquery.util.Util;


/**
 * Test suite for the XQueryProcessor class.
 * 
 * @author Oscar Stigter
 */
public class XQueryProcessorTest {
	
	
	@BeforeClass
	public static void beforeClass() {
		Util.initLog4j();
	}


    @Test
    public void executeQuery() {
        try {
            // Setup XQuery processor.
            XQueryProcessor xqp = new SaxonXQueryProcessor();
            
            String query = "<Result>OK</Result>";
            
            // Call XQuery function.
            String result = xqp.executeQuery(query).toString();
            Assert.assertTrue(result.endsWith(query));
            
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void executeFunction() {
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
            String result = xqp.executeFunction(
                    "http://www.example.com/converter", "convert",
                    doc1, doc2, new String[] {"multiply"}).toString();
            Assert.assertTrue(result.contains("<MergedValue>6</MergedValue>"));
            
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }


}
