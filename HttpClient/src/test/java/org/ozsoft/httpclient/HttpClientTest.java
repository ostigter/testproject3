package org.ozsoft.httpclient;

import java.io.IOException;

import org.ozsoft.httpclient.HttpClient;
import org.ozsoft.httpclient.HttpRequest;
import org.ozsoft.httpclient.HttpResponse;

/**
 * Test driver for the HttpClient library.
 * 
 * @author Oscar Stigter
 */
public class HttpClientTest {

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
//        client.setUseProxy(true);
//        client.setProxyHost("146.106.91.10");
//        client.setProxyPort(8080);
//        client.setProxyUsername("");
//        client.setProxyPassword("");
        
        String url = "http://localhost:8080/portal-server/";
        HttpRequest request = client.createGetRequest(url);
//        HttpRequest request = client.createPostRequest(url, "STATUS");
        try {
            HttpResponse response = request.execute();
            System.out.println(response.getStatusCode() + " " + response.getStatusMessage());
            String responseBody = response.getBody();
            if (responseBody != null) {
                System.out.println(responseBody);
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e);
        }
    }
    
}
