package org.ozsoft.stockviewer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    private static final long POLL_INTERVAL = 10000L;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("hh:mma");

    private static final String PROXY_HOST = "146.106.91.10";
    private static final int PROXY_PORT = 8080;
    private static final String PROXY_USERNAME = "";
    private static final String PROXY_PASSWORD = "";

    private final HttpPageReader httpPageReader;

    private final Set<Stock> stocks;

    private Date lastDate = new Date(0L);

    public static void main(String[] args) {
        new Main(args).run();
    }

    public Main(String[] args) {
        stocks = new TreeSet<Stock>();

        parseArguments(args);

        httpPageReader = new HttpPageReader();
        httpPageReader.setUseProxy(false);
        httpPageReader.setProxyHost(PROXY_HOST);
        httpPageReader.setProxyPort(PROXY_PORT);
        httpPageReader.setProxyUsername(PROXY_USERNAME);
        httpPageReader.setProxyPassword(PROXY_PASSWORD);
    }

    private void parseArguments(String[] args) {
        for (String symbol : args) {
            stocks.add(new Stock(symbol));
        }

        if (stocks.isEmpty()) {
            System.out.println("Usage:\n    java -jar stock-viewer.jar SYMBOL_1 [SYMBOL_2] ...");
            System.exit(1);
        }
    }

    private void run() {
        while (true) {
            showLatestPrices();

            try {
                Thread.sleep(POLL_INTERVAL);
            } catch (InterruptedException e) {
                // Safe to ignore.
            }
        }
    }

    private void showLatestPrices() {
        // Update all stocks in parallel.
        Map<Stock, StockUpdater> updaters = new HashMap<Stock, StockUpdater>();
        for (Stock stock : stocks) {
            StockUpdater updater = new StockUpdater(stock, httpPageReader);
            updater.start();
            updaters.put(stock, updater);
        }

        // Wait for all stocks to be updated.
        boolean isUpdated = false;
        for (StockUpdater updater : updaters.values()) {
            try {
                updater.join();
                isUpdated |= updater.isUpdated();
                Date stockDate = updater.getStock().getDate();
                if (stockDate.after(lastDate)) {
                    lastDate = stockDate;
                }
            } catch (InterruptedException e) {
                // Safe to ignore.
            }
        }

        // Show stock prices if any stock was updated.
        if (isUpdated) {
            System.out.format("\n%s\n", DATE_FORMAT.format(lastDate).toLowerCase());
            for (Stock stock : stocks) {
                System.out.format("%-5s  %6.2f  (%+.2f%%)  %s\n", stock, stock.getPrice(), stock.getChange(), stock.getChangeFlag().getMarker());
            }
        }
    }
}
