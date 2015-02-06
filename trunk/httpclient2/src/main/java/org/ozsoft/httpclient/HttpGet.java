package org.ozsoft.httpclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * HTTP GET request.
 * 
 * @author Oscar Stigter
 */
public class HttpGet extends HttpRequest {

    /**
     * Constructs an HTTP GET request.
     * 
     * @param client
     *            The HTTP client.
     * @param url
     *            The URL.
     */
    /* package */HttpGet(HttpClient client, String url) {
        super(client, url);
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
        con.setRequestMethod("GET");
        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);
        int statusCode = con.getResponseCode();
        String statusMessage = con.getResponseMessage();
        String responseBody = getResponseBody(statusCode, con);
        con.disconnect();
        return new HttpResponse(statusCode, statusMessage, responseBody);
    }

}
