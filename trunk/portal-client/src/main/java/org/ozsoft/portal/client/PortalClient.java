package org.ozsoft.portal.client;

import java.io.IOException;

import org.ozsoft.encryption.EncryptionException;
import org.ozsoft.encryption.Encryptor;
import org.ozsoft.httpclient.HttpClient;
import org.ozsoft.httpclient.HttpRequest;
import org.ozsoft.httpclient.HttpResponse;

/**
 * Portal client.
 * 
 * @author Oscar Stigter
 */
public class PortalClient {
    
    /** Portal server URL. */
    private static final String SERVER_URL = "http://localhost:8080/portal-server/";
    
    /** Shared key used for AES encryption. */
    private static final String ENCRYPTION_KEY = "F5iQ!w6#pYm&wB";
    
    /** HTTP client. */
    private final HttpClient httpClient;
    
    /** Encryptor. */
    private Encryptor encryptor;
    
    /**
     * Application's entry point.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new PortalClient();
    }
    
    /**
     * Constructs the portal client.
     */
    public PortalClient() {
        httpClient = new HttpClient();
//      httpClient.setUseProxy(true);
//      httpClient.setProxyHost("146.106.91.10");
//      httpClient.setProxyPort(8080);
//      httpClient.setProxyUsername("ostigter");
//      httpClient.setProxyPassword("Ost1gt2!");
        
        try {
            encryptor = new Encryptor();
            encryptor.setKey(ENCRYPTION_KEY);
        } catch (EncryptionException e) {
            throw new RuntimeException("ERROR: Could not initialize encryptor", e);
        }
        
        run();
    }
    
    public void run() {
        doPost("STATUS");
        
        doPost("CONNECT m1681 23");
        sleep(1000L);
        doPost("STATUS");
        
        doPost("RECEIVE");
        sleep(1000L);
        
        doPost("SEND otas.0000");
        sleep(1000L);
        doPost("RECEIVE");
        
        doPost("SEND lithos");
        sleep(1000L);
        doPost("RECEIVE");
        
        doPost("DISCONNECT");
        sleep(1000L);
        doPost("STATUS");
    }

    private void doPost(String body) {
        try {
            System.out.format("Execute POST request with body '%s'\n", body);
            body = encryptor.encrypt(body);
            HttpRequest request = httpClient.createHttpPost(SERVER_URL, body);
            try {
                HttpResponse response = request.execute();
                int statusCode = response.getStatusCode();
                if (statusCode != 200) {
                    System.out.format("Response status: %d %s\n", statusCode, response.getStatusMessage());
                }
                try {
                    String responseBody = encryptor.decrypt(response.getBody());
                    if (responseBody.length() > 0) {
                        System.out.format("Response body: '%s'\n", responseBody);
                    }
                } catch (EncryptionException e) {
                    // Security error (invalid encryption key).
                    System.err.println("ERROR: Could not decrypt response body: " + e);
                }
            } catch (IOException e) {
                System.err.println("ERROR: Could not send POST request: " + e);
            }
        } catch (EncryptionException e) {
            // This should never happen.
            System.err.println("ERROR: Could not encrypt request body: " + e);
        }
    }
    
    /**
     * Let's the calling thread sleep for a while.
     * 
     * @param duration
     *            Duration in ms to sleep.
     */
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Ignore.
        }
    }

}
