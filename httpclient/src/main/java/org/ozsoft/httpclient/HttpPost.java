package org.ozsoft.httpclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * HTTP POST request.
 * 
 * @author Oscar Stigter
 */
public class HttpPost extends HttpRequest {

    /**
     * Constructs an HTTP POST request.
     * 
     * @param client
     *            The HTTP client.
     * @param url
     *            The URL.
     * @param body
     *            The request body.
     */
    HttpPost(HttpClient client, String url, String body) {
        super(client, url, body);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.httpclient.HttpRequest#execute()
     */
    @Override
    public HttpResponse execute() throws MalformedURLException, IOException {
        client.updateProxySettings();
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        IOUtils.write(body, os);
        os.close();
        int statusCode = con.getResponseCode();
        String statusMessage = con.getResponseMessage();
        String responseBody = null;
        if (statusCode < 400) {
            responseBody = IOUtils.toString(con.getInputStream());
        } else {
            try {
                responseBody = IOUtils.toString(con.getErrorStream()); 
            } catch (IOException e) {
                // No error message available; aafe to ignore.
            }
        }
        con.disconnect();
        return new HttpResponse(statusCode, statusMessage, responseBody);
    }

}
