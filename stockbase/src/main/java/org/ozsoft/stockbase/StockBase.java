package org.ozsoft.stockbase;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.ozsoft.stockbase.sources.AlphaVantageDownloader;
import org.ozsoft.stockbase.sources.GoogleFinanceDownloader;
import org.ozsoft.stockbase.sources.YahooFinanceDownloader;

/**
 * Stock quote API, supporting the following queries:
 * <ul>
 * <li>a stock's current, real-time price</li>
 * <li>a stock's recent intra-day prices</li>
 * <li>a stock's historic, adjusted closing prices</li>
 * <li>a stock's historic dividend payouts</li>
 * </ul>
 * 
 * @author Oscar Stigter
 */
public class StockBase {

    public static final Calendar DEFAULT_FROM;

    public static final Calendar DEFAULT_TO;

    static {
        DEFAULT_FROM = Calendar.getInstance();
        DEFAULT_FROM.set(Calendar.DAY_OF_MONTH, 1);
        DEFAULT_FROM.set(Calendar.MONTH, 1);
        DEFAULT_FROM.set(Calendar.YEAR, 1900);

        DEFAULT_TO = Calendar.getInstance();
    }

    /**
     * Returns a stock's current price.
     * 
     * @param symbol
     *            The stock symbol.
     * 
     * @return A quote wih the stock's current price.
     * 
     * @throws IOException
     *             If the request to the underlying API failed.
     */
    public static Quote getCurrentPrice(String symbol) throws IOException {
        return GoogleFinanceDownloader.getCurrentPrice(symbol);
        // return AlphaVantageDownloader.getCurrentPrice(symbol);
    }

    public static List<Quote> getIntradayPrices(String symbol) throws IOException {
        // return GoogleFinanceDownloader.getIntradayPrices(symbol);
        return AlphaVantageDownloader.getIntradayPrices(symbol);
    }

    /**
     * Returns all of a stock's historic closing prices (with volume).
     * 
     * @param symbol
     *            The stock's symbol.
     * 
     * @return The stock's historic closing prices.
     * 
     * @throws IOException
     *             If the request to the underlying API failed.
     */
    public static List<Quote> getHistoricPrices(String symbol) throws IOException {
        return YahooFinanceDownloader.getHistoricPrices(symbol, DEFAULT_FROM, DEFAULT_TO);
    }

    /**
     * Returns all of a stock's historic dividend payouts.
     * 
     * @param symbol
     *            The stock's symbol.
     * 
     * @return The stock's historic dividend payouts.
     * 
     * @throws IOException
     *             If the request to the underlying API failed.
     */
    public static List<Quote> getHistoricDividends(String symbol) throws IOException {
        return getHistoricDividends(symbol, DEFAULT_FROM, DEFAULT_TO);
    }

    /**
     * Returns a stock's historic dividend payouts from a specific date.
     * 
     * @param symbol
     *            The stock's symbol.
     * @param from
     *            The 'from' date.
     * 
     * @return The stock's historic dividend payouts.
     * 
     * @throws IOException
     *             If the request to the underlying API failed.
     */
    public static List<Quote> getHistoricDividends(String symbol, Calendar from) throws IOException {
        return YahooFinanceDownloader.getHistoricDividends(symbol, from, DEFAULT_TO);
    }

    /**
     * Returns a stock's historic dividend payouts for a specific date range.
     * 
     * @param symbol
     *            The stock's symbol.
     * @param from
     *            The 'from' date.
     * @param to
     *            The 'to' date.
     * 
     * @return The stock's historic dividend payouts.
     * 
     * @throws IOException
     *             If the request to the underlying API failed.
     */
    public static List<Quote> getHistoricDividends(String symbol, Calendar from, Calendar to) throws IOException {
        return YahooFinanceDownloader.getHistoricDividends(symbol, from, to);
    }
}
