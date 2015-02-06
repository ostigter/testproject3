package org.ozsoft.httpclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHead extends HttpRequest {

    /* package */ HttpHead(HttpClient client, String url) {
        super(client, url);
    }
    
    @Override
    public HttpResponse execute() throws MalformedURLException, IOException {
        client.updateProxySettings();
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("HEAD");
        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);
        int statusCode = con.getResponseCode();
        String statusMessage = con.getResponseMessage();
        String responseBody = getResponseBody(statusCode, con);
        con.disconnect();
        return new HttpResponse(statusCode, statusMessage, responseBody);
    }

}
