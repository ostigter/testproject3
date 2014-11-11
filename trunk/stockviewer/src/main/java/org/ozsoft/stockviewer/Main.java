package org.ozsoft.stockviewer;

import java.io.IOException;

public class Main {

    private static final String QUOTE_URL = "http://download.finance.yahoo.com/d/quotes.csv?s=%s&f=d1t1l1";

    private static final String SYMBOL = "IVV";

    // private static final String PROXY_HOST = "146.106.91.10";
    // private static final int PROXY_PORT = 8080;
    // private static final String PROXY_USERNAME = "";
    // private static final String PROXY_PASSWORD = "";

    public static void main(String[] args) {
        HttpPageReader httpPageReader = new HttpPageReader();
        // httpPageReader.setUseProxy(true);
        // httpPageReader.setProxyHost(PROXY_HOST);
        // httpPageReader.setProxyPort(PROXY_PORT);
        // httpPageReader.setProxyUsername(PROXY_USERNAME);
        // httpPageReader.setProxyPassword(PROXY_PASSWORD);

        String uri = String.format(QUOTE_URL, SYMBOL);
        try {
            String[] lines = httpPageReader.read(uri);
            if (lines.length < 1) {
                System.err.println("ERROR: Empty quote response");
                System.exit(1);
            }
            String[] fields = lines[0].split(",");
            String date = stripQuotes(fields[0]);
            String time = stripQuotes(fields[1]);
            double price = Double.parseDouble(fields[2]);

            System.out.println("Date:  " + date);
            System.out.println("Time:  " + time);
            System.out.format("Price: %,.3f\n", price);
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static String stripQuotes(String text) {
        int length = (text != null) ? text.length() : 0;
        if (length > 0 && text.charAt(0) == '\"' && text.charAt(length - 1) == '\"') {
            return text.substring(1, length - 1);
        } else {
            return text;
        }
    }
}
