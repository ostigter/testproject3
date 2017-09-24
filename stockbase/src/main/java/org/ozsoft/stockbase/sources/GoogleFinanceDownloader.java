package org.ozsoft.stockbase.sources;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ozsoft.stockbase.Quote;
import org.ozsoft.stockbase.util.RedirectableHttpRequest;

/**
 * Stock quote downloader using the Google Finance API. <br />
 * <br />
 * 
 * Downloads the current stock price (market hours) and intraday prices. <br />
 * <br />
 * 
 * <b>WARNING:</b> This API is not officially supported by Google, so it's reliability and durability cannot be guaranteed!
 * 
 * @author Oscar Stigter
 */
public class GoogleFinanceDownloader {

    private static final String INTRADAY_PRICES_URL = "http://www.google.com/finance/getprices?q=%s&i=60&p=10d&f=d,c,v";

    /**
     * Returns a stock's current (last) price.
     * 
     * @param symbol
     *            The stock's symbol.
     * 
     * @return A quote with the stock's current (last) price, or {@code null} if not found.
     * 
     * @throws IOException
     *             If the request to the Google Finance API failed.
     */
    public static Quote getCurrentPrice(String symbol) throws IOException {
        List<Quote> prices = getIntradayPrices(symbol);
        int count = prices.size();
        if (count > 0) {
            // Return the last, most recent price;
            return prices.get(count - 1);
        } else {
            // No prices found.
            return null;
        }
    }

    /**
     * Returns a stock's intraday prices with 1 minute accuracy (interval), from the last 10 days (trading sessions).
     * 
     * @param symbol
     *            The stock's symbol.
     * 
     * @return The quotes with the intraday prices and volumes.
     * 
     * @throws IOException
     *             If the request to the Google Finance API failed.
     */
    public static List<Quote> getIntradayPrices(String symbol) throws IOException {
        String uri = String.format(INTRADAY_PRICES_URL, symbol);
        RedirectableHttpRequest redirectableRequest = new RedirectableHttpRequest(new URL(uri));
        URLConnection conn = redirectableRequest.openConnection();
        String content = IOUtils.toString(conn.getInputStream());

        List<Quote> quotes = new ArrayList<Quote>();

        long startTime = 0L;
        Date date = null;

        for (String line : content.split("\\n|\\r\\n|\\r")) {
            if (line.charAt(0) == 'a') {
                // System.out.println("### First line: '" + line + "'");
                String[] fields = line.split(",");
                startTime = Long.parseLong(fields[0].substring(1)) * 1000L;
                // System.out.println("### Epoch: " + startTime);
                date = new Date(startTime);
                // System.out.println("### Date: " + DATE_FORMAT.format(date));
                BigDecimal price = new BigDecimal(fields[1]);
                // System.out.format("### Price: $ %,f\n", price);
                int volume = Integer.parseInt(fields[2]);
                // System.out.format("### Volume: %,d\n", volume);
                quotes.add(new Quote(date, price, volume));
            } else if (date != null) {
                // System.out.println("### Next line: '" + line + "'");
                String[] fields = line.split(",");
                long timestamp = startTime + Long.parseLong(fields[0]) * 60000L;
                // System.out.println("### Epoch: " + timestamp);
                date = new Date(timestamp);
                // System.out.println("### Date: " + DATE_FORMAT.format(date));
                BigDecimal price = new BigDecimal(fields[1]);
                // System.out.format("### Price: $ %,f\n", price);
                int volume = Integer.parseInt(fields[2]);
                // System.out.format("### Volume: %,d\n", volume);
                quotes.add(new Quote(date, price, volume));
            }
        }

        return quotes;
    }
}
