package org.ozsoft.httpclient;

/**
 * HTTP response.
 * 
 * @author Oscar Stigter
 */
public class HttpResponse {
    
    private int statusCode;
    
    private String statusMessage;
    
    private String body;
    
    /* package */ HttpResponse(int statusCode, String statusMessage, String body) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.body = body;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public String getBody() {
        return body;
    }

}
