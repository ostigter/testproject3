package org.ozsoft.httpclient;

import java.io.IOException;
import java.net.MalformedURLException;

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
    
    /** The request body. */
    protected String body;
    
    /* package */ HttpRequest(HttpClient client, String url) {
        this(client, url, null);
    }
    
    /* package */ HttpRequest(HttpClient client, String url, String body) {
        this.client = client;
        this.url = url;
        this.body = body;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public abstract HttpResponse execute() throws MalformedURLException, IOException;

}
