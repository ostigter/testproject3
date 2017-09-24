package org.ozsoft.stockbase.sources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.ozsoft.stockbase.util.RedirectableHttpRequest;

/**
 * Yahoo Finance API HTTP cookie and crumb manager. <br />
 * <br />
 * 
 * Based on Stijn Strickx's execellent Yahoo Finance API wrapper (<code>https://github.com/sstrickx/yahoofinance-api</code>).
 * 
 * @author Oscar Stigter
 */
public class YahooFinanceCrumbManager {

    public static final String SCRAPE_URL = "https://finance.yahoo.com/quote/%5EGSPC/options";

    public static final String CRUMB_URL = "https://query1.finance.yahoo.com/v1/test/getcrumb";

    private static String crumb = null;

    private static String cookie = null;

    private static void setCookie() throws IOException {
        URL request = new URL(SCRAPE_URL);
        RedirectableHttpRequest redirectableRequest = new RedirectableHttpRequest(request);
        URLConnection connection = redirectableRequest.openConnection();

        for (String headerKey : connection.getHeaderFields().keySet()) {
            if ("Set-Cookie".equalsIgnoreCase(headerKey)) {
                for (String cookieField : connection.getHeaderFields().get(headerKey)) {
                    for (String cookieValue : cookieField.split(";")) {
                        if (cookieValue.matches("B=.*")) {
                            cookie = cookieValue;
                            // System.out.format("### Set Yahoo Finance API cookie based on HTTP request: '%s'\n", cookie);
                            return;
                        }
                    }
                }
            }
        }
        System.err.println("ERROR: Failed to set Yahoo Finance cookie based on HTTP request");
    }

    private static void setCrumb() throws IOException {
        URL crumbRequest = new URL(CRUMB_URL);
        RedirectableHttpRequest redirectableCrumbRequest = new RedirectableHttpRequest(crumbRequest, 5);

        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Cookie", cookie);

        URLConnection crumbConnection = redirectableCrumbRequest.openConnection(requestProperties);

        InputStreamReader is = new InputStreamReader(crumbConnection.getInputStream());
        BufferedReader br = new BufferedReader(is);
        String crumbResult = br.readLine();

        if (crumbResult != null && !crumbResult.isEmpty()) {
            crumb = crumbResult.trim();
            // System.out.format("### Set Yahoo Finance API crumb from HTTP request: '%s'\n", crumb);
        } else {
            System.err.println("ERROR: Failed to set Yahoo Finance crumb from HTTP request");
        }
    }

    public static void refresh() throws IOException {
        setCookie();
        setCrumb();
    }

    public static String getCrumb() throws IOException {
        if (crumb == null || crumb.isEmpty()) {
            refresh();
        }
        return crumb;
    }

    public static String getCookie() throws IOException {
        if (cookie == null || cookie.isEmpty()) {
            refresh();
        }
        return cookie;
    }
}
