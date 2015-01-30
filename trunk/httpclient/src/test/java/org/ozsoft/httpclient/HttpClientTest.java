package org.ozsoft.httpclient;

import java.io.IOException;

/**
 * Test driver for the HttpClient library.
 * 
 * @author Oscar Stigter
 */
public class HttpClientTest {

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        // client.setUseProxy(true);
        // client.setProxyHost("146.106.91.10");
        // client.setProxyPort(8080);
        // client.setProxyUsername("");
        // client.setProxyPassword("");

        String url = "http://www.google.com/";
        // InputStream body = IOUtils.toInputStream("STATUS");
        // HttpRequest request = client.createOptionsRequest(url);
        // HttpRequest request = client.createHeadRequest(url);
        HttpRequest request = client.createGetRequest(url);
        // HttpRequest request = client.createPostRequest(url, body);
        // HttpRequest request = client.createPutRequest(url, body);
        // HttpRequest request = client.createDeleteRequest(url);
        try {
            HttpResponse response = request.execute();
            System.out.println(response.getStatusCode() + " " + response.getStatusMessage());
            String responseBody = response.getBody();
            if (responseBody != null) {
                System.out.println(responseBody);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
