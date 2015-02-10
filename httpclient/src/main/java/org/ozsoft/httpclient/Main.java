package org.ozsoft.httpclient;

import java.io.IOException;

/**
 * Test driver.
 * 
 * @author Oscar Stigter
 */
public class Main {

    private static final String PROXY_HOST = "146.106.91.10";
    private static final int PROXY_PORT = 8080;
    private static final String PROXY_USERNAME = "";
    private static final String PROXY_PASSWORD = "";

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient(PROXY_HOST, PROXY_PORT, PROXY_USERNAME, PROXY_PASSWORD);
        String uri = "http://www.google.nl";
        try {
            HttpResponse httpResponse = httpClient.executeGet(uri);
            System.out.println(httpResponse.getStatusCode());
            System.out.println(httpResponse.getBody());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            httpClient.close();
        }
    }
}
