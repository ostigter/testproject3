package org.ozsoft.fondsbeheer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;

/**
 * Retrieves HTTP pages from an URL, with support for a HTTP proxy with
 * optional authentication.
 * 
 * @author Oscar Stigter
 */
public class HttpPageReader {

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
    public HttpPageReader() {
    	// Empty implementation.
    }
    
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
     * @param urlString
     *            The URL.
     * 
     * @return The content of the page in separate lines.
     */
    public String[] read(String urlString) {
        ArrayList<String> lines = new ArrayList<String>();
        
        if (useProxy) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", String.valueOf(proxyPort));
            Authenticator.setDefault(new SimpleAuthenticator(proxyUsername, proxyPassword));
        }
        
        BufferedReader reader = null;
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(decodeXml(line));
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("ERROR: " + e);
                }
            }
        }
        
        return lines.toArray(new String[0]);
    }
    
    private static String decodeXml(String s) {
        s = s.replaceAll("&amp;",  "&");
        s = s.replaceAll("&lt;",   "<");
        s = s.replaceAll("&gt;",   ">");
        s = s.replaceAll("&quot;", "\"");
        s = s.replaceAll("&apos;", "'");
        s = s.replaceAll("&nbsp;", " ");
        s = s.replaceAll("&Aacute;",   "A");
        s = s.replaceAll("&aacute;",   "a");
        s = s.replaceAll("&Eacute;",   "E");
        s = s.replaceAll("&eacute;",   "e");
        s = s.replaceAll("&Auml;",     "A");
        s = s.replaceAll("&auml;",     "a");
        s = s.replaceAll("&Ouml;",     "O");
        s = s.replaceAll("&ouml;",     "o");
        s = s.replaceAll("&oslash;",   "o");
        s = s.replaceAll("&Uuml;",     "U");
        s = s.replaceAll("&uuml;",     "u");
        s = s.replaceAll("&euro;",     "EUR");
        return s;
    }
    

    //------------------------------------------------------------------------
    //  Inner class
    //------------------------------------------------------------------------

    /**
     * Simple HTTP authenticator with a username and password.
     * 
     * @author Oscar Stigter
     */
    private static class SimpleAuthenticator extends Authenticator {
    	
       private String username;
       
       private String password;

       public SimpleAuthenticator(String username, String password) {
          this.username = username;
          this.password = password;
       }

       protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password.toCharArray());
       }
       
    } // SimpleAuthenticator

} // HttpPageReader
