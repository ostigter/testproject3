package org.ozsoft.stockbase.sources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ozsoft.stockbase.Quote;
import org.ozsoft.stockbase.util.RedirectableHttpRequest;
import org.ozsoft.stockbase.util.Utils;

/**
 * Stock quote downloader using the Yahoo Finance API. <br />
 * <br />
 * 
 * Downloads historic stock closing prices and stock dividend payouts. <br />
 * <br />
 * 
 * Based on Stijn Strickx's execellent Yahoo Finance API wrapper (<code>https://github.com/sstrickx/yahoofinance-api</code>). <br />
 * <br />
 * 
 * <b>WARNING:</b> This API is no longer supported by Yahoo, so it's reliability and durability cannot be guaranteed!
 * 
 * @author Oscar Stigter
 */
public class YahooFinanceDownloader {

    private static final String HIST_PRICES_URL = "https://query1.finance.yahoo.com/v7/finance/download/";

    public static final String CSV_DELIMITER = ",";

    private static final int PRICE_CSV_FIELD_COUNT = 7;

    private static final int DIVIDEND_CSV_FIELD_COUNT = 2;

    /**
     * Returns a stock's historic closing prices.
     * 
     * @param symbol
     *            The stock's symbol.
     * @param from
     *            The 'from' date.
     * @param to
     *            The 'to' date.
     * 
     * @return The stock's historic closing prices.
     * 
     * @throws IOException
     *             If the request to the Yahoo Finance API failed.
     */
    public static List<Quote> getHistoricPrices(String symbol, Calendar from, Calendar to) throws IOException {
        cleanDates(from, to);

        List<Quote> prices = new ArrayList<Quote>();

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("period1", String.valueOf(from.getTimeInMillis() / 1000));
        params.put("period2", String.valueOf(to.getTimeInMillis() / 1000));
        params.put("interval", YahooFinanceQueryInterval.DAILY.getTag());
        params.put("crumb", YahooFinanceCrumbManager.getCrumb());

        String uri = HIST_PRICES_URL + URLEncoder.encode(symbol, "UTF-8") + "?" + Utils.getURLParameters(params);
        RedirectableHttpRequest redirectableRequest = new RedirectableHttpRequest(new URL(uri), 5);
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Cookie", YahooFinanceCrumbManager.getCookie());
        URLConnection connection = redirectableRequest.openConnection(requestProperties);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        br.readLine(); // skip the first line
        String line = null;
        while ((line = br.readLine()) != null) {
            // System.out.format("### Parsing CSV line: '%s'\n", Utils.unescape(line));
            String[] fields = line.split(CSV_DELIMITER);
            if (fields.length != PRICE_CSV_FIELD_COUNT) {
                System.err.println("ERROR: Invalid number of CSV fields in Yahoo Finance response (historic prices)");
                break;
            }
            Date date = Utils.parseHistDate(fields[0]).getTime();
            BigDecimal price = Utils.getBigDecimal(fields[4]);
            int volume = Utils.getInt(fields[6]);
            prices.add(new Quote(date, price, volume));
        }

        Collections.sort(prices);

        return prices;
    }

    /**
     * Returns a stock's historic dividend payouts.
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
     *             If the request to the Yahoo Finance API failed.
     */
    public static List<Quote> getHistoricDividends(String symbol, Calendar from, Calendar to) throws IOException {
        cleanDates(from, to);

        List<Quote> dividends = new ArrayList<Quote>();

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("period1", String.valueOf(from.getTimeInMillis() / 1000));
        params.put("period2", String.valueOf(to.getTimeInMillis() / 1000));
        params.put("interval", YahooFinanceQueryInterval.DAILY.getTag());
        params.put("events", "div");
        params.put("crumb", YahooFinanceCrumbManager.getCrumb());

        String uri = HIST_PRICES_URL + URLEncoder.encode(symbol, "UTF-8") + "?" + Utils.getURLParameters(params);
        RedirectableHttpRequest redirectableRequest = new RedirectableHttpRequest(new URL(uri), 5);
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Cookie", YahooFinanceCrumbManager.getCookie());
        URLConnection connection = redirectableRequest.openConnection(requestProperties);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        br.readLine(); // skip the first line
        String line = null;
        while ((line = br.readLine()) != null) {
            // System.out.format("### Parsing CSV line: '%s'\n", Utils.unescape(line));
            String[] fields = line.split(CSV_DELIMITER);
            if (fields.length != DIVIDEND_CSV_FIELD_COUNT) {
                System.err.println("ERROR: Invalid number of CSV fields in Yahoo Finance response (historic dividends)");
                break;
            }
            Date date = Utils.parseHistDate(fields[0]).getTime();
            BigDecimal price = Utils.getBigDecimal(fields[1]);
            dividends.add(new Quote(date, price));
        }

        Collections.sort(dividends);

        return dividends;
    }

    private static void cleanDates(Calendar from, Calendar to) {
        if (from.after(to)) {
            throw new IllegalArgumentException("ERROR: Invalid 'from' and 'to' dates");
        }

        Utils.cleanCalendar(from);
        Utils.cleanCalendar(to);
    }
}
