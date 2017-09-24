package org.ozsoft.stockbase.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Redirectable HTTP request.
 * 
 * @author Stijn Strickx
 */
public class RedirectableHttpRequest {

    private static final int DEFAULT_REDIRECT_LIMIT = 3;

    private URL url;

    private int protocolRedirectLimit;

    private int connectTimeout = 100000;

    private int readTimeout = 30000;

    public RedirectableHttpRequest(URL request) {
        this(request, DEFAULT_REDIRECT_LIMIT);
    }

    public RedirectableHttpRequest(URL url, int protocolRedirectLimit) {
        this.url = url;
        this.protocolRedirectLimit = protocolRedirectLimit;
    }

    public URLConnection openConnection() throws IOException {
        return openConnection(new HashMap<String, String>());
    }

    public URLConnection openConnection(Map<String, String> requestProperties) throws IOException {
        int redirectCount = 0;
        boolean hasResponse = false;
        HttpURLConnection connection = null;
        URL currentUrl = this.url;
        while (!hasResponse && (redirectCount <= this.protocolRedirectLimit)) {
            connection = (HttpURLConnection) currentUrl.openConnection();
            connection.setConnectTimeout(this.connectTimeout);
            connection.setReadTimeout(this.readTimeout);

            for (String requestProperty : requestProperties.keySet()) {
                connection.addRequestProperty(requestProperty, requestProperties.get(requestProperty));
            }

            // only handle protocol redirects manually...
            connection.setInstanceFollowRedirects(true);

            switch (connection.getResponseCode()) {
                case HttpURLConnection.HTTP_MOVED_PERM:
                case HttpURLConnection.HTTP_MOVED_TEMP:
                    redirectCount++;
                    String location = connection.getHeaderField("Location");
                    currentUrl = new URL(url, location);
                    break;
                default:
                    hasResponse = true;
            }
        }

        if (redirectCount > this.protocolRedirectLimit) {
            throw new IOException("Protocol redirect count exceeded for url: " + this.url.toExternalForm());
        } else if (connection == null) {
            throw new IOException("Unexpected error while opening connection");
        } else {
            return connection;
        }
    }

    public URL getRequest() {
        return url;
    }

    public void setRequest(URL request) {
        this.url = request;
    }

    public int getProtocolRedirectLimit() {
        return protocolRedirectLimit;
    }

    public void setProtocolRedirectLimit(int protocolRedirectLimit) {
        if (protocolRedirectLimit >= 0) {
            this.protocolRedirectLimit = protocolRedirectLimit;
        }
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
