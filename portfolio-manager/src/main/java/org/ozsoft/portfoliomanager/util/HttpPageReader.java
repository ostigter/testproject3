package org.ozsoft.portfoliomanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

/**
 * Retrieves HTTP pages from an URL, with support for a HTTP proxy with optional authentication.
 * 
 * @author Oscar Stigter
 */
public class HttpPageReader {

    private static final int CONNECT_TIMEOUT = 5000; // 5 sec.
    private static final int READ_TIMEOUT = 300000; // 5 min.
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);

    private boolean useProxy = false;
    private String proxyHost = "";
    private int proxyPort = 8080;
    private String proxyUsername = "";
    private String proxyPassword = "";

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    /**
     * Reads an HTTP page from an URL.
     * 
     * @param uri
     *            The URL.
     * 
     * @return The response content body.
     */
    public String read(String uri) throws IOException {
        updateProxySettings();
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        return IOUtils.toString(connection.getInputStream());
    }

    public long getFileLastModified(String uri) throws IOException {
        long timestamp = -1L;

        updateProxySettings();
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setRequestMethod("HEAD");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        int statusCode = connection.getResponseCode();
        if (statusCode == HttpURLConnection.HTTP_OK) {
            String dateString = connection.getHeaderField("Last-Modified");
            if (dateString != null && dateString.length() > 16) {
                dateString = dateString.substring(5, 16);
                try {
                    timestamp = DATE_FORMAT.parse(dateString).getTime();
                } catch (ParseException e) {
                    System.err.format("ERROR: Invalid Last-Modified date '%s' from HEAD request to '%s'\n", dateString, url);
                }
            } else {
                System.err.format("ERROR: No Last-Modified header from HEAD request to '%s'\n", url);
            }
        } else {
            System.err.format("ERROR: Failed HEAD request to '%s' (HTTP status code: %d)\n", url, statusCode);
        }

        return timestamp;
    }

    public InputStream downloadFile(String uri) throws IOException {
        updateProxySettings();
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        return connection.getInputStream();
    }

    private void updateProxySettings() {
        if (useProxy) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", String.valueOf(proxyPort));
            Authenticator.setDefault(new SimpleAuthenticator(proxyUsername, proxyPassword));
        }
    }

    /**
     * Simple HTTP authenticator with a username and password.
     * 
     * @author Oscar Stigter
     */
    private static class SimpleAuthenticator extends Authenticator {

        private final String username;

        private final String password;

        public SimpleAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password.toCharArray());
        }
    }
}
