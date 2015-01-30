package org.ozsoft.blackbeard.util.http;

/**
 * Wrapper around Apache's HttpResponse (convenience). <br />
 * 
 * The response body is always returned as a <code>String</code>, therefore this wrapper should not be used in case
 * 
 * @author Oscar Stigter
 */
public class HttpResponse {

    private final int statusCode;

    private final String responseBody;

    public HttpResponse(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
