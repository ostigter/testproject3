package streamvalidator;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * Example implementation of a streaming SAX validator with schema
 * augmentation.
 *
 * @author Oscar Stigter
 */
public class StreamValidator {
    
    
    private static final String SAX_VALIDATION_FEATURE =
            "http://xml.org/sax/features/validation";
    
    private final SchemaFactory schemaFactory;
    
    private final SAXParserFactory parserFactory;
    
    private final Transformer transformer;
    
    private Schema schema;
    
    private XMLReader reader;
    
    
    public StreamValidator() {
        // Create schema factory.
        try {
            schemaFactory = SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI);
        } catch (Exception e) {
            throw new RuntimeException(
                    "ERROR: Could not get SchemaFactory instance: "
                    + e.getMessage());
        }

        // Create parser factory.
        parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        parserFactory.setValidating(true);
        
        // Create transformer.
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            transformer = tf.newTransformer();
        } catch (Exception e) {
            throw new RuntimeException(
                    "ERROR: Could not create transformer: " + e.getMessage());
        }
    }
    
    
    public void setSchemaRoot(String path) {
        // Compile schema's.
        long startTime = System.currentTimeMillis();
        List<File> schemaFiles = getSchemaFiles(path);
        Source[] schemaSources = new Source[schemaFiles.size()];
        for (int i = 0; i < schemaSources.length; i++) {
            schemaSources[i] = new StreamSource(schemaFiles.get(i));
        }
        try {
            schema = schemaFactory.newSchema(schemaSources);
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("Compiled " + schemaSources.length +
                         " schema's in " + duration + " ms.");
        } catch (SAXException e) {
            throw new RuntimeException(
                    "Could not compile schema's: " + e.getMessage());
        }
        
        // Create XML reader.
        try {
            parserFactory.setSchema(schema);
            reader = parserFactory.newSAXParser().getXMLReader();
            reader.setFeature(SAX_VALIDATION_FEATURE, false);
            reader.setErrorHandler(new MyErrorHandler());
        } catch (Exception e) {
            throw new RuntimeException(
                    "ERROR: Could not create Transformer: " + e.getMessage());
        }
    }


    public void validate(InputStream is, OutputStream os) {
        Source source = new SAXSource(reader, new InputSource(is));
        Result result = new StreamResult(os);
        
        transformer.reset();
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(
                    "Transformation error: " + e.getMessage());
        }
    }
    

    private List<File> getSchemaFiles(String path) {
        List<File> files = new ArrayList<File>();
        
        File dir = new File(path);
        if (dir.isDirectory()) {
            collectSchemaFiles(dir, files);
        }
        
        return files;
    }


    private void collectSchemaFiles(File file, List<File> result) {
        if (file.isDirectory()) {
            if (!file.getName().equals("SCCS")) {  // Skip SCCS directories
                for (File f : file.listFiles()) {
                    collectSchemaFiles(f, result);
                }
            }
        } else {
            if (file.getName().toLowerCase().endsWith(".xsd")) {
                result.add(file);
            }
        }
    }


}
