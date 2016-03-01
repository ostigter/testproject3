package org.ozsoft.portfoliomanager.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ozsoft.portfoliomanager.domain.Stock;
import org.ozsoft.portfoliomanager.util.HttpPageReader;

/**
 * Thread that updates the share price of a single stock. <br />
 * <br />
 * 
 * Grabs the latest, real-time share price from the stock's Yahoo! Finance stock summary page (HTML).
 * 
 * @author Oscar Stigter
 */
public class StockUpdater extends Thread {

    private static final String QUOTE_URL = "http://finance.yahoo.com/q?s=%s";

    private static final Pattern PATTERN = Pattern
            .compile("<span id=\"yfs_l84_.*?\">(.*?)</span>.*<span id=\"yfs_c63_.*?\">.*? class=\"(?:neg_arrow|pos_arrow)\" alt=\"(Up|Down)\">.*<span id=\"yfs_p43_.*?\">\\((.*?)%\\)</span>(?s).*>P/E <.*? class=\"yfnc_tabledata1\">(.*?)</td>.*>Div &amp; Yield:</th><td class=\"yfnc_tabledata1\">(.*?) \\(");

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
        String uri = String.format(QUOTE_URL, stock.getSymbol());
        try {
            String content = httpPageReader.read(uri);

            Matcher m = PATTERN.matcher(content);
            if (m.find()) {
                double price = Double.parseDouble(m.group(1));
                double changePerc = Double.parseDouble(m.group(3));
                if (m.group(2).equals("Down")) {
                    changePerc *= -1;
                }
                String peRatioText = m.group(4);
                double peRatio = peRatioText.equals("N/A") ? 0.0 : Double.parseDouble(peRatioText);
                String divRateString = m.group(5);
                double divRate = divRateString.equals("N/A") ? 0.0 : Double.parseDouble(divRateString);

                if (price != stock.getPrice()) {
                    stock.setPrice(price);
                    stock.setChangePerc(changePerc);
                    stock.setPeRatio(peRatio);
                    stock.setDivRate(divRate);
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
