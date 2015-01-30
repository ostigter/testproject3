package org.ozsoft.blackbeard;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {

    private static final String PROXY_HOST = "146.106.91.10";
    private static final int PROXY_PORT = 8080;
    private static final String PROXY_USERNAME = "";
    private static final String PROXY_PASSWORD = "";

    public static void main(String[] args) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(PROXY_HOST, PROXY_PORT), new UsernamePasswordCredentials(PROXY_USERNAME, PROXY_PASSWORD));
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

        try {
            HttpHost target = new HttpHost("www.google.nl");
            HttpHost proxy = new HttpHost(PROXY_HOST, PROXY_PORT);

            HttpGet httpget = new HttpGet("/");
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            httpget.setConfig(config);

            System.out.println("Executing request " + httpget.getRequestLine() + " to " + target + " via " + proxy);

            CloseableHttpResponse response = httpclient.execute(target, httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(response.getEntity());
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            IOUtils.closeQuietly(httpclient);
        }
    }
}
