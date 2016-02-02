package org.ozsoft.stockviewer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockUpdater extends Thread {

    private static final String QUOTE_URL = "http://finance.yahoo.com/q?s=%s";

    private static final Pattern PATTERN = Pattern
            .compile("<span id=\"yfs_l84_.*?\">(.*?)</span>.*<span id=\"yfs_c63_.*?\">.*? class=\"(?:neg_arrow|pos_arrow)\" alt=\"(Up|Down)\">.*<span id=\"yfs_p43_.*?\">\\((.*?)%\\)</span>.*<span id=\"yfs_t53_.*?\">(.*) [A-Z]+</span>");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("hh:mma");

    private final Stock stock;

    private final HttpPageReader httpPageReader;

    private boolean isFinished = false;

    private boolean isUpdated = false;

    public StockUpdater(Stock stock, HttpPageReader httpPageReader) {
        this.stock = stock;
        this.httpPageReader = httpPageReader;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Stock getStock() {
        return stock;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    @Override
    public void run() {
        // System.out.format("### StockUpdater for '%s' started\n", stock);

        String uri = String.format(QUOTE_URL, stock.getSymbol());
        try {
            String content = httpPageReader.read(uri);

            Matcher m = PATTERN.matcher(content);
            if (m.find()) {
                double price = Double.parseDouble(m.group(1));
                double change = Double.parseDouble(m.group(3));
                if (m.group(2).equals("Down")) {
                    change *= -1;
                }
                Date date = DATE_FORMAT.parse(m.group(4));

                if (price != stock.getPrice()) {
                    stock.setPrice(price);
                    stock.setChange(change);
                    stock.setDate(date);
                    isUpdated = true;
                }
            }

        } catch (Exception e) {
            System.err.format("ERROR: Could not get update for stock '%s': %s\n", stock, e.getMessage());
            e.printStackTrace(System.err);
        }

        isFinished = true;
    }
}