package org.ozsoft.blackbeard.util.http;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Convenience wrapper around Apache HttpComponents' {@code HttpClient}.
 * 
 * @author Oscar Stigter
 */
public class HttpClient {

    private static final int TIMEOUT = 30000;

    private CloseableHttpClient httpClient;

    /**
     * Constructor for a default HTTP client.
     */
    public HttpClient() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).setSocketTimeout(TIMEOUT)
                .build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * Constructor for an HTTP client that uses an HTTP proxy without authentication.
     * 
     * @param host
     *            Proxy host.
     * @param port
     *            Proxy port.
     */
    public HttpClient(String host, int port) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).setSocketTimeout(TIMEOUT)
                .build();
        HttpHost proxy = new HttpHost(host, port);
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setProxy(proxy).build();
    }

    /**
     * Constructor for an HTTP client that uses an HTTP proxy with authentication.
     * 
     * @param host
     *            Proxy host.
     * @param port
     *            Proxy port.
     * @param username
     *            Proxy username.
     * @param password
     *            Proxy password.
     */
    public HttpClient(String host, int port, String username, String password) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).setSocketTimeout(TIMEOUT)
                .build();
        HttpHost proxy = new HttpHost(host, port);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(username, password));
        httpClient = HttpClients.custom().setDefaultRequestConfig(config).setProxy(proxy).setDefaultCredentialsProvider(credsProvider).build();
    }

    /**
     * Executes a GET request.
     * 
     * @param uri
     *            The request URI.
     * 
     * @return The response.
     * 
     * @throws IOException
     *             If the request failed due to an I/O error.
     */
    public HttpResponse executeGet(String uri) throws IOException {
        HttpGet getRequest = new HttpGet(uri);
        int statusCode = -1;
        String body = null;
        try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
            statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                body = IOUtils.toString(entity.getContent());
            }
            EntityUtils.consume(response.getEntity());
        }
        return new HttpResponse(statusCode, body);
    }

    /**
     * Closes the {@code HttpClient}, releasing all resources.
     */
    public void close() {
        if (httpClient != null) {
            IOUtils.closeQuietly(httpClient);
            httpClient = null;
        }
    }
}
