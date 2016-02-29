package org.ozsoft.stockviewer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockUpdater extends Thread {

    private static final String QUOTE_URL = "http://finance.yahoo.com/q?s=%s";

    private static final Pattern PATTERN = Pattern
            .compile("<span id=\"yfs_l84_.*?\">(.*?)</span>.*<span id=\"yfs_c63_.*?\">.*? class=\"(?:neg_arrow|pos_arrow)\" alt=\"(Up|Down)\">.*<span id=\"yfs_p43_.*?\">\\((.*?)%\\)</span>.*<span id=\"yfs_t53_.*?\">(.*) [A-Z]+</span>(?s).*>P/E <.*? class=\"yfnc_tabledata1\">(.*?)</td>.*>Div &amp; Yield:</th><td class=\"yfnc_tabledata1\">(.*?) \\(");

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
        stock.setChangeFlag(ChangeFlag.UNCHANGED);
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
                String dateText = m.group(4);
                int p = dateText.indexOf(',');
                if (p >= 0) {
                    dateText = dateText.substring(p + 1).trim();
                }
                // System.out.format("### Date: '%s'\n", dateText);
                Date date = new SimpleDateFormat("h:mma").parse(dateText);
                String peRatioText = m.group(5);
                double peRatio = peRatioText.equals("N/A") ? 0.0 : Double.parseDouble(peRatioText);
                // System.out.format("### P/E: %.2f\n", peRatio);
                double divRate = Double.parseDouble(m.group(6));
                // System.out.format("### Div.rate = %.4f\n", divRate);

                if (price != stock.getPrice()) {
                    if (stock.getPrice() == 0.0) {
                        if (change > 0.0) {
                            stock.setChangeFlag(ChangeFlag.UP);
                        } else if (change < 0.0) {
                            stock.setChangeFlag(ChangeFlag.DOWN);
                        }
                    } else if (price > stock.getPrice()) {
                        stock.setChangeFlag(ChangeFlag.UP);
                    } else if (price < stock.getPrice()) {
                        stock.setChangeFlag(ChangeFlag.DOWN);
                    }
                    stock.setDate(date);
                    stock.setPrice(price);
                    stock.setChange(change);
                    stock.setPeRatio(peRatio);
                    stock.setDivRate(divRate);
                    isUpdated = true;
                }
            }

        } catch (Exception e) {
            System.err.format("ERROR: Could not get update for stock '%s': %s\n", stock, e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }

        isFinished = true;
    }
}
