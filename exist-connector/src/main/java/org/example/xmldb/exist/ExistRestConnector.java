package org.example.xmldb.exist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.example.xmldb.XmldbConnector;
import org.example.xmldb.XmldbException;

/**
 * eXist connector using the REST API.
 * 
 * @author Oscar Stigter
 */
public class ExistRestConnector implements XmldbConnector {
    
    private static final String PARAMETER_STRING_DELIMITER = "\"";

    private static final String PARAMETER_SEPERATOR = ", ";

    private static final int BUFFER_SIZE = 8192;
    
    private static final Logger LOGGER = Logger.getLogger(ExistRestConnector.class);
    
    private final String servletUri;
    
    /**
     * Constructor.
     * 
     * @param host
     *            The host.
     * @param port
     *            The port.
     */
    public ExistRestConnector(String host, int port) {
	if (host == null || host.length() == 0) {
	    throw new IllegalArgumentException("Null or empty host");
	}
	if (port < 1 || port > 65535) {
	    throw new IllegalArgumentException("Invalid port");
	}
	servletUri = String.format("http://%s:%d/exist/rest", host, port);
    }
    
    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#retrieveResource(java.lang.String)
     */
    @Override
    public String retrieveResource(String uri) throws XmldbException {
	HttpClient httpClient = new HttpClient();
	GetMethod getMethod = new GetMethod(servletUri + uri);
	String content = null;
	try {
	    int statusCode = httpClient.executeMethod(getMethod);
	    LOGGER.trace("HTTP status: " + statusCode);
	    content = getMethod.getResponseBodyAsString();
	} catch (IOException e) {
	    LOGGER.error("Could not retrieve document", e);
	}
	return content;
    }
    
    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#retrieveXmlDocument(java.lang.String)
     */
    @Override
    public Document retrieveXmlDocument(String uri) throws XmldbException {
	String content = retrieveResource(uri);
	Document doc = null;
	try {
	    doc = DocumentHelper.parseText(content);
	} catch (DocumentException e) {
	    throw new XmldbException("Could not create XML document from conent", e);
	}
	return doc;
	
    }
    
    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#storeResource(java.lang.String, java.io.File)
     */
    @Override
    public void storeResource(String uri, File file) throws XmldbException {
	try {
	    InputStream is = new FileInputStream(file);
	    storeResource(uri, is);
	} catch (IOException e) {
	    LOGGER.error("Could not read file", e);
	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#storeResource(java.lang.String, org.dom4j.Document)
     */
    @Override
    public void storeResource(String uri, Document doc) throws XmldbException {
	String content = doc.asXML();
	storeResource(uri, content);
    }
    
    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#storeResource(java.lang.String, java.lang.String)
     */
    @Override
    public void storeResource(String uri, String content) throws XmldbException {
	InputStream is = new ByteArrayInputStream(content.getBytes());
	storeResource(uri, is);
    }

    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#storeResource(java.lang.String, java.io.InputStream)
     */
    @Override
    public void storeResource(String uri, InputStream is) throws XmldbException {
	HttpClient httpClient = new HttpClient();
	PutMethod putMethod = new PutMethod(servletUri + uri);
	RequestEntity entity = new InputStreamRequestEntity(is);
	((EntityEnclosingMethod) putMethod).setRequestEntity(entity);
	try {
	    int statusCode = httpClient.executeMethod(putMethod);
	    LOGGER.trace("HTTP status: " + statusCode);
	} catch (Exception e) {
	    LOGGER.error("Could not store resource", e);
	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#deleteResource(java.lang.String)
     */
    @Override
    public void deleteResource(String uri) throws XmldbException {
	HttpClient httpClient = new HttpClient();
	DeleteMethod deleteMethod = new DeleteMethod(servletUri + uri);
	try {
	    int statusCode = httpClient.executeMethod(deleteMethod);
	    LOGGER.trace("HTTP status: " + statusCode);
	} catch (Exception e) {
	    LOGGER.error("Could not delete resource", e);
	}
    }

    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#executeQuery(java.lang.String)
     */
    @Override
    public String executeQuery(String query) throws XmldbException {
	// Build request body.
	StringBuilder sb = new StringBuilder();
	sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	sb.append("<query xmlns=\"http://exist.sourceforge.net/NS/exist\">\n");
	sb.append("  <text><![CDATA[\n");
	sb.append(query).append("\n");
	sb.append("  ]]></text>\n");
	sb.append("  <properties>\n");
	sb.append("    <property name=\"indent\" value=\"yes\"/>\n");
	sb.append("  </properties>\n");
	sb.append("</query>\n");
	String body = sb.toString();
	LOGGER.trace("POST request:\n" + body);
	
	// Create POST method.
	HttpClient httpClient = new HttpClient();
	PostMethod postMethod = new PostMethod(servletUri + "/db");
	ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
	RequestEntity entity = new InputStreamRequestEntity(bais, "text/xml; charset=UTF-8");
	((EntityEnclosingMethod) postMethod).setRequestEntity(entity);

	// Execute method.
	String response = null;
	try {
	    int statusCode = httpClient.executeMethod(postMethod);
	    LOGGER.trace("HTTP status: " + statusCode);

	    // Read response body.
	    Reader reader = new InputStreamReader(postMethod.getResponseBodyAsStream());
	    StringBuilder sb2 = new StringBuilder();
	    char[] buffer = new char[BUFFER_SIZE];
	    int read = 0;
	    while ((read = reader.read(buffer)) > 0) {
		sb2.append(buffer, 0, read);
	    }
	    reader.close();
	    response = sb2.toString();
//	    LOGGER.debug("POST response:\n" + response);
	} catch (Exception e) {
	    LOGGER.trace("Error while querying database", e);
	} finally {
	    if (bais != null) {
		try {
		    bais.close();
		} catch (IOException e) {
		    LOGGER.error("Could not close stream", e);
		}
	    }
	    postMethod.releaseConnection();
	}
	return response;
    }
    
    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#callModule(java.lang.String, java.util.Map)
     */
    @Override
    public String callModule(String uri, Map<String, String> params) throws XmldbException {
	HttpClient httpClient = new HttpClient();
	GetMethod getMethod = new GetMethod(servletUri + uri);
	if (params != null && params.size() > 0) {
        	NameValuePair[] nameValuePairs = new NameValuePair[params.size()];
        	int i = 0;
        	for (Entry<String, String> param : params.entrySet()) {
        	    nameValuePairs[i++] = new NameValuePair(param.getKey(), param.getValue()); 
        	}
        	getMethod.setQueryString(nameValuePairs);
	}
	String content = null;
	try {
	    int statusCode = httpClient.executeMethod(getMethod);
	    LOGGER.trace("HTTP status: " + statusCode);
	    content = getMethod.getResponseBodyAsString();
	} catch (IOException e) {
	    LOGGER.error("Could not retrieve document", e);
	}
	return content;
    }
    
    /*
     * (non-Javadoc)
     * @see org.example.exist.XmldbConnector#callFunction(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public String callFunction(String moduleNamespace, String moduleUri,
	    String functionName, String... params) throws XmldbException {
	StringBuilder sb = new StringBuilder();
	final int paramCount = params.length;
	for (int i = 0; i < paramCount; i++) {
	    String parameter = params[i];
	    sb.append(PARAMETER_STRING_DELIMITER);
	    sb.append(parameter);
	    sb.append(PARAMETER_STRING_DELIMITER);
	    if (i < paramCount - 1) {
		sb.append(PARAMETER_SEPERATOR);
	    }
	}
	String paramString = sb.toString();
	String query = String.format(
		"import module namespace tns=\"%s\" at \"xmldb:exist://%s\"; tns:%s(%s)",
		moduleNamespace, moduleUri, functionName, paramString);
	LOGGER.trace("Query:\n" + query);
	return executeQuery(query);
    }
    
}
