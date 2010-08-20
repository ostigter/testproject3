package org.ozsoft.xmldb.exist;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
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
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.ozsoft.xmldb.Collection;
import org.ozsoft.xmldb.NotAuthorizedException;
import org.ozsoft.xmldb.NotFoundException;
import org.ozsoft.xmldb.Resource;
import org.ozsoft.xmldb.XmldbConnector;
import org.ozsoft.xmldb.XmldbException;

/**
 * Connector for the eXist XML database using the REST interface. <br />
 * <br />
 * 
 * Implemented using the Apache Commons HttpClient library.
 * 
 * @author Oscar Stigter
 */
public class ExistConnector implements XmldbConnector {
    
    private static final int STATUS_ERROR = 400;

    private static final int AUTHORIZATION_REQUIRED = 401;

    private static final int NOT_AUTHORIZED = 403;

    private static final int NOT_FOUND = 404;

    private static final String PARAMETER_STRING_DELIMITER = "\"";

    private static final String PARAMETER_SEPERATOR = ", ";

    private static final int BUFFER_SIZE = 8192;

    private static final Logger LOG = Logger.getLogger(ExistConnector.class);
    
    private final HttpClient httpClient;

    private final String servletUri;

    /**
     * Constructor.
     *
     * @param host
     *            The host running an eXist instance.
     * @param port
     *            The port eXist is running on.
     * @param username
     *            The username of the eXist user account.
     * @param password
     *            The password of the eXist user account.
     */
    public ExistConnector(String host, int port, String username, String password) {
        if (host == null || host.length() == 0) {
            throw new IllegalArgumentException("Null or empty host");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Invalid port");
        }

        httpClient = new HttpClient();
        httpClient.getParams().setAuthenticationPreemptive(true);
        Credentials credentials = new UsernamePasswordCredentials(username, password);
        httpClient.getState().setCredentials(new AuthScope(host, port, AuthScope.ANY_REALM), credentials);

        servletUri = String.format("http://%s:%d/exist/rest", host, port);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ozsoft.xmldb.XmldbConnector#retrieveCollection(java.lang.String)
     */
    @Override
    public Collection retrieveCollection(String uri) throws XmldbException {
        Collection col = null;
        try {
            Element result = retrieveXmlDocument(uri).getRootElement();
            if (result.getName().equals("result")) {
                Element el = result.element("collection");
                if (el != null) {
                    String name = el.attributeValue("name");
                    col = new Collection(name);
                    for (Object child : el.elements()) {
                        el = (Element) child;
                        name = el.attributeValue("name");
                        Resource res = null;
                        if (el.getName().equals("collection")) {
                            res = new Collection(name);
                        } else {
                            res = new Resource(name);
                        }
                        col.addResource(res);
                    }
                }
            }
        } catch (XmldbException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Collection not found: '%s'", uri));
            }
        }
        return col;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.example.exist.XmldbConnector#retrieveResource(java.lang.String)
     */
    @Override
    public byte[] retrieveResource(String uri) throws XmldbException {
        GetMethod getMethod = new GetMethod(servletUri + uri);
        InputStream is = null;
        ByteArrayOutputStream baos = null;        
        byte[] content = null;
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode >= STATUS_ERROR) {
                if (statusCode == NOT_FOUND) {
                    throw new NotFoundException(uri);
                } else if (statusCode == AUTHORIZATION_REQUIRED || statusCode == NOT_AUTHORIZED) {
                    throw new NotAuthorizedException(String.format("Not authorized to access resource '%s'", uri));
                } else {
                    throw new XmldbException(String.format("Could not retrieve resource '%s' (HTTP status code: %d)", uri, statusCode));
                }
            }
            is = getMethod.getResponseBodyAsStream();
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            content = baos.toByteArray();
        } catch (IOException e) {
            String msg = String.format("Could not retrieve resource '%s': %s", uri, e.getMessage());
            LOG.error(msg, e);
            throw new XmldbException(msg, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("Could not close InputStream", e);
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    LOG.error("Could not close ByteArrayOutputStream", e);
                }
            }
        }
        return content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.example.exist.XmldbConnector#retrieveXmlDocument(java.lang.String)
     */
    @Override
    public Document retrieveXmlDocument(String uri) throws XmldbException {
        GetMethod getMethod = new GetMethod(servletUri + uri);
        Document doc = null;
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode >= STATUS_ERROR) {
                if (statusCode == NOT_FOUND) {
                    throw new NotFoundException(uri);
                } else if (statusCode == AUTHORIZATION_REQUIRED || statusCode == NOT_AUTHORIZED) {
                    throw new NotAuthorizedException(String.format("Not authorized to access resource '%s'", uri));
                } else {
                    throw new XmldbException(String.format("Could not retrieve XML document '%s' (HTTP status code: %d)", uri, statusCode));
                }
            }
            InputStream is = getMethod.getResponseBodyAsStream();
            SAXReader reader = new SAXReader();
            doc = reader.read(is);
        } catch (DocumentException e) {
            String msg = String.format("Resource is not a valid XML document '%s': %s", uri, e.getMessage());
            throw new XmldbException(msg, e);
        } catch (IOException e) {
            String msg = String.format("Could not retrieve XML document '%s': %s", uri, e.getMessage());
            LOG.error(msg, e);
            throw new XmldbException(msg, e);
        }
        return doc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.example.exist.XmldbConnector#storeResource(java.lang.String,
     * org.dom4j.Document)
     */
    @Override
    public void storeResource(String uri, Document doc) throws XmldbException {
        String content = doc.asXML();
        storeResource(uri, content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.example.exist.XmldbConnector#storeResource(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void storeResource(String uri, String content) throws XmldbException {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        storeResource(uri, is);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.example.exist.XmldbConnector#storeResource(java.lang.String,
     * java.io.InputStream)
     */
    @Override
    public void storeResource(String uri, InputStream is) throws XmldbException {
        PutMethod putMethod = new PutMethod(servletUri + uri);
        RequestEntity entity = new InputStreamRequestEntity(is);
        ((EntityEnclosingMethod) putMethod).setRequestEntity(entity);
        try {
            int statusCode = httpClient.executeMethod(putMethod);
            if (statusCode >= STATUS_ERROR) {
                if (statusCode == NOT_FOUND) {
                    throw new NotFoundException(uri);
                } else if (statusCode == AUTHORIZATION_REQUIRED || statusCode == NOT_AUTHORIZED) {
                    throw new NotAuthorizedException(String.format("Not authorized to store resource '%s'", uri));
                } else {
                    throw new XmldbException(String.format("Could not store resource '%s' (HTTP status code: %d)", uri, statusCode));
                }
            }
        } catch (IOException e) {
            String msg = String.format("Could not store resource '%s': %s", uri, e.getMessage());
            LOG.error(msg, e);
            throw new XmldbException(msg, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.example.exist.XmldbConnector#deleteResource(java.lang.String)
     */
    @Override
    public void deleteResource(String uri) throws XmldbException {
        DeleteMethod deleteMethod = new DeleteMethod(servletUri + uri);
        try {
            int statusCode = httpClient.executeMethod(deleteMethod);
            if (statusCode >= STATUS_ERROR) {
                if (statusCode == NOT_FOUND) {
                    // Resource does not exist (no problem).
                } else if (statusCode == AUTHORIZATION_REQUIRED || statusCode == NOT_AUTHORIZED) {
                    throw new NotAuthorizedException(String.format("Not authorized to delete resource '%s'", uri));
                } else {
                    throw new XmldbException(String.format("Could not delete resource '%s' (HTTP status code: %d)", uri, statusCode));
                }
            }
        } catch (IOException e) {
            String msg = String.format("Could not delete resource '%s': %s", uri, e.getMessage());
            LOG.error(msg, e);
            throw new XmldbException(msg, e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.xmldb.XmldbConnector#importCollection(java.lang.String, java.io.File)
     */
    @Override
    public void importCollection(String uri, File dir) throws XmldbException {
        if (!dir.isDirectory()) {
            throw new XmldbException("Directory not found: " + dir.getAbsolutePath());
        }
        if (!dir.canRead()) {
            throw new XmldbException("Directory not readable: " + dir.getAbsolutePath());
        }
        
        for (File file : dir.listFiles()) {
            String name = file.getName();
            if (!name.startsWith(".")) {
                String resourceUri = String.format("%s/%s", uri, name);
                if (file.isDirectory()) {
                    importCollection(resourceUri, file);
                } else {
                    importResource(resourceUri, file);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.xmldb.XmldbConnector#importResource(java.lang.String, java.io.File)
     */
    @Override
    public void importResource(String uri, File file) throws XmldbException {
        if (!file.isFile()) {
            throw new XmldbException("File not found: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new XmldbException("File not readable: " + file.getAbsolutePath());
        }
        
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            storeResource(uri, bis);
        } catch (IOException e) {
            String msg = String.format("Could not store resource '%s': %s", uri, e.getMessage());
            LOG.error(msg, e);
            throw new XmldbException(msg, e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LOG.error(String.format("Could not properly close file '%s'", file.getAbsolutePath()));
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.xmldb.XmldbConnector#exportCollection(java.lang.String, java.io.File)
     */
    @Override
    public void exportCollection(String uri, File dir) throws XmldbException {
        // Retrieve collection.
        Collection col = retrieveCollection(uri);
        if (col == null) {
            throw new XmldbException(String.format("Collection not found: '%s'", uri));
        }

        // Create directory if necessary.
        if (!dir.isDirectory()) {
            boolean dirCreated = dir.mkdirs();
            if (!dirCreated) {
                throw new XmldbException(String.format("Could not create directory: '%s'", dir.getAbsolutePath()));
            }
        }
        
        // Export resources.
        for (Resource res : col.getResources()) {
            String name = res.getName();
            String resourceUri = String.format("%s/%s", uri, name);
            File resourceFile = new File(dir, name);
            if (res instanceof Collection) {
                exportCollection(resourceUri, resourceFile);
            } else {
                exportResource(resourceUri, resourceFile);
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.xmldb.XmldbConnector#exportResource(java.lang.String, java.io.File)
     */
    @Override
    public void exportResource(String uri, File file) throws XmldbException {
        // Make sure parent directory exists.
        File dir = file.getParentFile();
        if (dir != null && !dir.isDirectory()) {
            boolean dirCreated = dir.mkdirs();
            if (!dirCreated) {
                throw new XmldbException(String.format("Could not create directory: '%s'", dir.getAbsolutePath()));
            }
        }
        
        // Get (binary) resource content.
        byte[] content = retrieveResource(uri);
        
        // Write file.
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(content);
        } catch (IOException e) {
            LOG.error(String.format("Could not export resource '%s' to file '%s'", uri, file.getAbsolutePath()));
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    LOG.error(String.format("Could not properly close file '%s'", file.getAbsolutePath()));
                }
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * 
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
        if (LOG.isTraceEnabled()) {
            LOG.trace("POST request:\n" + body);
        }

        // Create POST method.
        PostMethod postMethod = new PostMethod(servletUri + "/db");
        ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        RequestEntity entity = new InputStreamRequestEntity(bais, "text/xml; charset=UTF-8");
        ((EntityEnclosingMethod) postMethod).setRequestEntity(entity);

        // Execute method.
        String response = null;
        try {
            // Execute query.
            int statusCode = httpClient.executeMethod(postMethod);

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
            if (LOG.isTraceEnabled()) {
                LOG.trace("POST response:\n" + response);
            }
            
            if (statusCode >= STATUS_ERROR) {
                throw new XmldbException(String.format("Could not execute query '%s' (HTTP status code: %d)", query, statusCode));
            }
        } catch (IOException e) {
            throw new XmldbException("Error while executing query: " + e.getMessage(), e);
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    LOG.warn("Could not close stream", e);
                }
            }
            if (postMethod != null) {
                postMethod.releaseConnection();
            }
        }
        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.example.exist.XmldbConnector#callModule(java.lang.String,
     * java.util.Map)
     */
    @Override
    public String callModule(String uri, Map<String, String> params) throws XmldbException {
        GetMethod getMethod = new GetMethod(servletUri + uri);
        if (params != null && params.size() > 0) {
            NameValuePair[] nameValuePairs = new NameValuePair[params.size()];
            int i = 0;
            for (Entry<String, String> param : params.entrySet()) {
                nameValuePairs[i++] = new NameValuePair(param.getKey(), param.getValue());
            }
            getMethod.setQueryString(nameValuePairs);
        }
        String result = null;
        try {
            // Execute query.
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode >= STATUS_ERROR) {
                if (statusCode == NOT_FOUND) {
                    throw new NotFoundException(uri);
                } else if (statusCode == AUTHORIZATION_REQUIRED || statusCode == NOT_AUTHORIZED) {
                    throw new NotAuthorizedException(String.format("Not authorized to execute module '%s'", uri));
                } else {
                    throw new XmldbException(String.format("Could not retrieve resource '%s' (HTTP status code: %d)", uri, statusCode));
                }
            }

            // Read response body.
            Reader reader = new InputStreamReader(getMethod.getResponseBodyAsStream());
            StringBuilder sb2 = new StringBuilder();
            char[] buffer = new char[BUFFER_SIZE];
            int read = 0;
            while ((read = reader.read(buffer)) > 0) {
                sb2.append(buffer, 0, read);
            }
            reader.close();
            result = sb2.toString();
            LOG.trace("Query result:\n" + result);
        } catch (IOException e) {
            String msg = String.format("Error executing XQuery module '%s'", uri);
            throw new XmldbException(msg, e);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.example.exist.XmldbConnector#callFunction(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public String callFunction(String moduleNamespace, String moduleUri, String functionName, String... params)
            throws XmldbException {
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
        return executeQuery(query);
    }

}
