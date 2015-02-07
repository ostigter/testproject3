package org.ozsoft.blackbeard.util.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

/**
 * Simple HTTP client based on {@code HttpURLConnection}.
 * 
 * @author Oscar Stigter
 */
public class HttpClient {

    /**
     * Whether to use an HTTP proxy.
     */
    private boolean useProxy = false;

    /**
     * The proxy host.
     */
    private String proxyHost = "";

    /**
     * The proxy port.
     */
    private int proxyPort = 8080;

    /**
     * The username for proxy authentication.
     */
    private String proxyUsername = "";

    /**
     * The password for proxy authentication.
     */
    private String proxyPassword = "";

    /**
     * Constructor.
     */
    public HttpClient() {
        // Empty implementation.
    }

    /**
     * Sets whether to use a proxy.
     * 
     * @param useProxy
     *            True if using a proxy, otherwise false.
     */
    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    /**
     * Sets the proxy's hostname.
     * 
     * @param proxyHost
     *            The proxy's hostname.
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * Sets the proxy's port.
     * 
     * @param proxyPort
     *            The proxy's port.
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * Sets the username used for proxy authentication.
     * 
     * @param username
     *            The username.
     */
    public void setProxyUsername(String username) {
        this.proxyUsername = username;
    }

    /**
     * Sets the password used for proxy authentication.
     * 
     * @param password
     *            The password.
     */
    public void setProxyPassword(String password) {
        this.proxyPassword = password;
    }

    /**
     * Creates and returns an HTTP GET request.
     * 
     * @param url
     *            The URL.
     * 
     * @return The GET request.
     */
    public HttpRequest createGetRequest(String uri) {
        return new HttpGet(this, uri);
    }

    /**
     * Updates the proxy settigns.
     */
    /* package */void updateProxySettings() {
        Properties properties = System.getProperties();
        if (useProxy) {
            properties.put("http.proxyHost", proxyHost);
            properties.put("http.proxyPort", String.valueOf(proxyPort));
            Authenticator.setDefault(new BasicAuthenticator(proxyUsername, proxyPassword));
        } else {
            properties.remove("http.proxyHost");
            properties.remove("http.proxyPort");
        }
        System.setProperties(properties);
    }

    /**
     * HTTP Basic authenticator based on an username and password.
     * 
     * @author Oscar Stigter
     */
    private static class BasicAuthenticator extends Authenticator {

        /** The username. */
        private final String username;

        /** The password. */
        private final String password;

        /**
         * Constructs a HTTP Basic authenticator.
         * 
         * @param username
         *            The username.
         * @param password
         *            The password.
         */
        public BasicAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password.toCharArray());
        }
    }
}
