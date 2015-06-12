package org.ozsoft.stockviewer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class QuoteUpdater {

    private static final String QUOTE_URL = "http://download.finance.yahoo.com/d/quotes.csv?s=%s&f=d1t1l1";

    private static final String PROXY_HOST = "146.106.91.10";
    private static final int PROXY_PORT = 8080;
    private static final String PROXY_USERNAME = "";
    private static final String PROXY_PASSWORD = "";

    // private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy h:mma");

    private final HttpPageReader httpPageReader;

    public QuoteUpdater() {
        httpPageReader = new HttpPageReader();
        httpPageReader.setUseProxy(true);
        httpPageReader.setProxyHost(PROXY_HOST);
        httpPageReader.setProxyPort(PROXY_PORT);
        httpPageReader.setProxyUsername(PROXY_USERNAME);
        httpPageReader.setProxyPassword(PROXY_PASSWORD);
    }

    public void update(Stock stock) {
        String symbol = stock.getSymbol();
        String uri = String.format(QUOTE_URL, symbol);
        try {
            String[] lines = httpPageReader.read(uri);
            if (lines.length > 0) {
                String[] fields = lines[0].split(",");
                String dateString = stripQuotes(fields[0]) + " " + stripQuotes(fields[1]);
                try {
                    Calendar date = Calendar.getInstance();
                    date.setTime(DATE_FORMAT.parse(dateString));
                    if (date.after(stock.getDate())) {
                        stock.setDate(date);
                        stock.setPreviousPrice(stock.getPrice());
                        stock.setPrice(new BigDecimal(fields[2]));
                    }
                } catch (ParseException e) {
                    System.err.format("ERROR: Could not parse date '%s'\n", dateString);
                    e.printStackTrace(System.err);
                }
            } else {
                System.err.format("ERROR: Empty quote response for '%s'\n", symbol);
            }
        } catch (IOException e) {
            System.err.format("ERROR: Could not update quote for '%s': %s\n", symbol, e.getMessage());
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
