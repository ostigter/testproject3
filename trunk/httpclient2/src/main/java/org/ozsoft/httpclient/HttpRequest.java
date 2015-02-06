package org.ozsoft.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;

/**
 * HTTP request.
 * 
 * @author Oscar Stigter
 */
public abstract class HttpRequest {
    
    /** Timeout used for the HTTP connection. */
    protected static final int TIMEOUT = 5000;
    
    /** HTTP client. */
    protected final HttpClient client;
    
    /** The URL. */
    protected String url;
    
    /* package */ HttpRequest(HttpClient client, String url) {
        this.client = client;
        this.url = url;
    }
    
    public String getUrl() {
        return url;
    }
    
    public abstract HttpResponse execute() throws MalformedURLException, IOException;
    
    protected String getResponseBody(int statusCode, HttpURLConnection con) {
        String responseBody = null;
        try {
            InputStream is = null;
            if (statusCode < 400) {
                is = con.getInputStream();
            } else {
                is = con.getErrorStream();
            }
            if (is != null) {
                responseBody = IOUtils.toString(is);
            }
        } catch (IOException e) {
            System.err.println("ERROR: Could not read HTTP response body: " + e);
        }
        return responseBody;
    }

}
