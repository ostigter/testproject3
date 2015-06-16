package org.ozsoft.stockviewer;

import java.io.IOException;

public class Main {

    private static final String QUOTE_URL = "http://download.finance.yahoo.com/d/quotes.csv?s=%s&f=d1t1l1";

    private static final String SYMBOL = "PER";

    private static final long POLL_INTERVAL = 5000L;

    // private static final String PROXY_HOST = "146.106.91.10";
    // private static final int PROXY_PORT = 8080;
    // private static final String PROXY_USERNAME = "";
    // private static final String PROXY_PASSWORD = "";

    private static HttpPageReader httpPageReader;

    private static String lastDate;

    public static void main(String[] args) {
        httpPageReader = new HttpPageReader();
        // httpPageReader.setUseProxy(true);
        // httpPageReader.setProxyHost(PROXY_HOST);
        // httpPageReader.setProxyPort(PROXY_PORT);
        // httpPageReader.setProxyUsername(PROXY_USERNAME);
        // httpPageReader.setProxyPassword(PROXY_PASSWORD);

        while (true) {
            showLatestPrice();

            try {
                Thread.sleep(POLL_INTERVAL);
            } catch (InterruptedException e) {
                // Safe to ignore.
            }
        }
    }

    private static void showLatestPrice() {
        String uri = String.format(QUOTE_URL, SYMBOL);
        try {
            String[] lines = httpPageReader.read(uri);
            if (lines.length < 1) {
                System.err.println("ERROR: Empty quote response");
                System.exit(1);
            }
            String[] fields = lines[0].split(",");
            String date = String.format("%s %s", stripQuotes(fields[0]), stripQuotes(fields[1]));

            double price = Double.parseDouble(fields[2]);

            if (!date.equals(lastDate)) {
                System.out.format("%s $%,.3f\n", date, price);
                lastDate = date;
            }
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
