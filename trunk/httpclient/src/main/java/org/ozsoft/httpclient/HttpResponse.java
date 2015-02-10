package org.ozsoft.httpclient;

/**
 * Convenience wrapper around Apache HttpComponents' {@code HttpResponse}. <br />
 * <br />
 * 
 * The response body is always read and returned as {@code String}. <br />
 * <br />
 * 
 * <b>NOTE:</b> Only use this wrapper when expecting relatively small response bodies, to avoid memory issues.
 * 
 * @author Oscar Stigter
 */
public class HttpResponse {

    private final int statusCode;

    private final String body;

    /* package */HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * Returns the HTTP status code.
     * 
     * @return The HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the response body.
     * 
     * @return The response body.
     */
    public String getBody() {
        return body;
    }
}
