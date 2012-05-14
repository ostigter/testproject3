package org.ozsoft.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * HTTP PUT request.
 * 
 * @author Oscar Stigter
 */
public class HttpPut extends HttpRequest {
    
    private final InputStream body;

    /**
     * Constructs an HTTP PUT request.
     * 
     * @param client
     *            The HTTP client.
     * @param url
     *            The URL.
     * @param body
     *            The request body.
     */
    HttpPut(HttpClient client, String url, InputStream body) {
        super(client, url);
        this.body = body;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ozsoft.httpclient.HttpRequest#execute()
     */
    @Override
    public HttpResponse execute() throws MalformedURLException, IOException {
        client.updateProxySettings();
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        IOUtils.copy(body, os);
        os.close();
        int statusCode = con.getResponseCode();
        String statusMessage = con.getResponseMessage();
        String responseBody = getResponseBody(statusCode, con);
        con.disconnect();
        return new HttpResponse(statusCode, statusMessage, responseBody);
    }

}
