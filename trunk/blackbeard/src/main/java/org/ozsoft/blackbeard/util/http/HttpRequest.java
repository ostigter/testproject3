package org.ozsoft.blackbeard.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;

/**
 * Base class for all HTTP requests.
 * 
 * @author Oscar Stigter
 */
public abstract class HttpRequest {

    private static final int HTTP_CLIENT_ERROR = 400;

    /** Timeout used for the HTTP connection. */
    protected static final int TIMEOUT = 10000;

    /** HTTP client. */
    protected final HttpClient client;

    /** The URL. */
    protected String url;

    /* package */HttpRequest(HttpClient client, String url) {
        this.client = client;
        this.url = url;
    }

    /**
     * Returns the request URL.
     * 
     * @return The request URL.
     */
    public String getUrl() {
        return url;
    }

    public abstract HttpResponse execute() throws MalformedURLException, IOException;

    protected String getResponseBody(int statusCode, HttpURLConnection con) {
        String responseBody = null;
        try {
            InputStream is = null;
            if (statusCode < HTTP_CLIENT_ERROR) {
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
