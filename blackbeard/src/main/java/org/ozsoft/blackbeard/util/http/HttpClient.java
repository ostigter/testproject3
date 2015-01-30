package org.ozsoft.blackbeard.util.http;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Wrapper around Apache's HttpClient (convenience).
 * 
 * @author Oscar Stigter
 * 
 */
public class HttpClient {

    private CloseableHttpClient httpClient;

    public HttpClient() {
        httpClient = HttpClients.createDefault();
    }

    public void setProxy(String host, int port) {
        setProxy(host, port, null, null);
    }

    public void setProxy(String host, int port, String username, String password) {
        if (httpClient != null) {
            close();
        }
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(username, password));
        httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
    }

    public HttpResponse executeGet(String uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        try (CloseableHttpResponse resp = httpClient.execute(httpGet)) {
            int statusCode = resp.getStatusLine().getStatusCode();
            String responseBody = null;
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = resp.getEntity();
                if (entity != null) {
                    responseBody = IOUtils.toString(entity.getContent());
                }
            }
            return new HttpResponse(statusCode, responseBody);
        }
    }

    public void close() {
        if (httpClient != null) {
            IOUtils.closeQuietly(httpClient);
            httpClient = null;
        }
    }
}
