package xmlvalidator;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Utility class to validate XML files against their schema.
 * 
 * Uses the standard JAXP API.
 * 
 * @author  Oscar Stigter
 */
public class XmlValidator {
    
    /** Schema factory for WC3 XML schema's with namespaces. */ 
    private static final SchemaFactory schemaFactory =
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    
    /** SAX parser. */
    private final SAXParser parser;
    
    /** Cached schema validators mapped by target namespace. */
    private final Map<String, Validator> validators;
    
    /** Schema root directory. */
    private File schemaRootDir;
    
    /**
     * Constructor.
     */
    public XmlValidator() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        try {
            parser = spf.newSAXParser();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not instantiate SAX parser: " + e);
        }
        
        validators = new HashMap<String, Validator>();
    }

    /**
     * Sets the schema root directory.
     * 
     * Updates the cached schema validators.
     * 
     * @param  path  the path to the schema root directory
     */
    public void setSchemaRootDir(String path) {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory() && dir.canRead()) {
            schemaRootDir = dir;
            updateSchemas();
        } else {
            throw new IllegalArgumentException(
                    "Invalid schema root directory: " + path);
        }
    }
    
    /**
     * Validates an XML file against a specific schema file.
     * 
     * @param   xmlFile     the path to the XML file
     * @param   schemaFile  the path to the schema file
     * 
     * @throws SAXException  If the XML file is invalid 
     * @throws IOException   If the XML file could not be read
     */
    public void validate(File xmlFile, File schemaFile)
            throws SAXException, IOException {
//        System.out.println("Validating file '" + xmlFile + "'...");
        Validator validator = getValidator(schemaFile);
        validator.validate(new StreamSource(xmlFile));
    }
    
    /**
     * Validates an XML file against a cached schema.
     * 
     * @param   file  the XML file
     * 
     * @throws  SAXException  If the XML file is invalid
     * @throws  IOException   If the XML file cannot be read
     */
    public void validate(File file) throws SAXException, IOException {
//        System.out.println("Validating '" + file + "'...");
        String namespace = getNamespaceFromXmlFile(file);
        if (namespace != null && namespace.length() > 0) {
            Validator validator = validators.get(namespace);
            if (validator != null) {
                validator.validate(new StreamSource(file));
            } else {
                System.out.println(String.format(
                        "%s: No schema found for namespace: %s",
                        file, namespace));
            }
        } else {
            System.out.println(String.format(
                    "WARNING: %s: WARNING: No namespace declaration found.", file));
        }
    }
    
    private void updateSchemas() {
        validators.clear();
//        System.out.println("Collecting schema's in '"
//                + schemaRootDir.getAbsolutePath() + "'...");
//        long startTime = System.currentTimeMillis();
        collectSchemas(schemaRootDir);
//        long endTime = System.currentTimeMillis();
//        System.out.println("Found and compiled " + validators.size() + " schema's in " + (endTime - startTime) + " ms.");
        System.out.println("Found " + validators.size() + " schema's.");
    }
    
    /**
     * Returns a schema validator generated from a schema file.
     * 
     * @param   schemaFile  the path to the schema file
     * 
     * @return  the schema validator
     * 
     * @throws  IllegalArgumentException  if the schema file cannot be found
     *                                    or could not be parsed
     */
    private Validator getValidator(File file) {
        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                Schema schema = schemaFactory.newSchema(file);
                return schema.newValidator();
            } catch (SAXException e) {
                throw new IllegalArgumentException(
                        "Could not parse schema file '" + file + "': " + e);
            }
        } else {
            throw new IllegalArgumentException(
                    "Could not find or read schema file: " + file);
        }
    }

    /**
     * Finds schema files in the specified directory and subdirectories, and
     * stores schema validators based on the schema's found.
     *  
     * @param  dir  the directory
     */
    private void collectSchemas(File dir) {
        // First process schema files in this directory...
        for (File file : dir.listFiles()) {
            if (file.isFile() &&
                    file.getName().toLowerCase().endsWith(".xsd")) {
                try {
                    String namespace = getTargetNamespaceFromSchema(file);
                    Schema schema = schemaFactory.newSchema(file);
                    Validator validator = schema.newValidator();
                    validators.put(namespace, validator);
//                    System.out.println("Schema found: " + namespace);
                } catch (Exception e) {
                    System.err.println("ERROR: Invalid schema file: "
                            + file + ": " + e.getMessage());
                }
            }
        }
        
        // ...then recurse into subdirectories.
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                collectSchemas(file);
            }
        }
    }
    
    /**
     * Returns the target namespace from a schema file.
     * 
     * @param   file  the schema file
     * 
     * @return  the target namespace
     */
    public String getTargetNamespaceFromSchema(File file)
            throws SAXException, IOException {
        String namespace = null;
        
        SchemaContentHandler handler = new SchemaContentHandler();
        try {
            parser.parse(file, handler);
        } catch (ParsingDoneException e) {
            // We have found what we were looking for.
            namespace = handler.getNamespace();
        }
        
        return namespace;
    }

    /**
     * Returns the root element namespace of an XML file, or null if it has
     * no namespace.
     *  
     * @param  file  the XML file
     * 
     * @return  the root element namespace, or null if no namespace
     */
    public String getNamespaceFromXmlFile(File file)
            throws SAXException, IOException {
        String namespace = null;
        
        DocumentContentHandler handler = new DocumentContentHandler();
        try {
            parser.parse(file, handler);
        } catch (ParsingDoneException e) {
            // We have found what we were looking for.
            namespace = handler.getNamespace();
        }
        
        return namespace;
    }

    
    
    //------------------------------------------------------------------------
    //  Inner classes.
    //------------------------------------------------------------------------
    
    
    /**
     * SAX content handler to retrieve the target namespace from a schema file. 
     */
    private class SchemaContentHandler extends DefaultHandler {
        
        /** Target namespace found. */
        private String namespace = null;
        
        /**
         * Returns the target namespace.
         * 
         * @return  the target namespace
         */
        public String getNamespace() {
            return namespace;
        }
        
        /**
         * Processes the start of an XML element.
         * 
         * @param  uri        the element's namespace URI
         * @param  localName  the element's local name
         * @param  qName      the element's qualified name
         * @param  attr       the element's attributes
         */
        @Override // DefaultHandler
        public void startElement(String uri, String localName, String qName,
                Attributes attr) throws SAXException {
            if (localName.equals("schema")) {
                namespace = attr.getValue("targetNamespace");
                throw new ParsingDoneException();
            }
        }
        
    }
    
    
    //------------------------------------------------------------------------
    
    
    /**
     * SAX content handler to retrieve the root elememt namespace. 
     */
    private class DocumentContentHandler extends DefaultHandler {
        
        /** Target namespace found. */
        private String namespace = null;
        
        /**
         * Returns the target namespace.
         * 
         * @return  the target namespace
         */
        public String getNamespace() {
            return namespace;
        }
        
        /**
         * Returns the root element namespace of an XML file, or null if it has
         * no namespace.
         *  
         * @param  file  the XML file
         * 
         * @return  the root element namespace, or null if no namespace
         */
        @Override // DefaultHandler
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            namespace = uri;
            throw new ParsingDoneException();
        }
        
    }
    
    
    //------------------------------------------------------------------------
    
    
    /**
     * Exception used to abort a SAX parser when all required data has been
     * collected.
     *
     * This trick should boost performance when parsing large documents,
     * especially when the interesting data is located at the start of the document.
     */
    private class ParsingDoneException extends SAXException {
        
        /** Serial version UID. */
        private static final long serialVersionUID = -1L;
        
    }
    
}
