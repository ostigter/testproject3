package org.ozsoft.stockbase.sources;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.ozsoft.stockbase.Quote;
import org.ozsoft.stockbase.util.RedirectableHttpRequest;
import org.ozsoft.stockbase.util.Utils;

/**
 * Stock quote downloader using the Alpha Vantage API. <br />
 * <br />
 * 
 * Downloads the current stock price (market hours) and intraday prices. <br />
 * <br />
 * 
 * @author Oscar Stigter
 */
public class AlphaVantageDownloader {

    private static boolean sslValidationDisabled = false;

    private static final String API_KEY = "QO5X";

    private static final String INTRADAY_PRICES_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&apikey=%s&symbol=%s&interval=1min&outputsize=%s&datatype=csv";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    /**
     * Returns a stock's current (last) price.
     * 
     * @param symbol
     *            The stock's symbol.
     * 
     * @return A quote with the stock's current (last) price.
     * 
     * @throws IOException
     *             If the request to the Alpha Vantage API failed.
     */
    public static Quote getCurrentPrice(String symbol) throws IOException {
        if (!sslValidationDisabled) {
            disableSSLValidation();
        }

        String uri = String.format(INTRADAY_PRICES_URL, API_KEY, symbol, "compact");
        RedirectableHttpRequest redirectableRequest = new RedirectableHttpRequest(new URL(uri));
        URLConnection conn = redirectableRequest.openConnection();
        String content = IOUtils.toString(conn.getInputStream());
        Quote quote = null;
        boolean firstLine = true;
        for (String line : content.split("\\n|\\r\\n|\\r")) {
            if (firstLine) {
                firstLine = false;
            } else {
                String[] fields = line.split(",");
                Date date;
                try {
                    date = DATE_FORMAT.parse(fields[0]);
                    BigDecimal price = new BigDecimal(fields[4]);
                    int volume = Utils.getInt(fields[5]);
                    quote = new Quote(date, price, volume);
                    // The first quote is the latest, so we're done.
                    break;
                } catch (ParseException e) {
                    System.err.format("ERROR: Could not parse quote date: '%s'\n", fields[0]);
                }
            }
        }

        return quote;
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
     *             If the request to the Alpha Vantage API failed.
     */
    public static List<Quote> getIntradayPrices(String symbol) throws IOException {
        if (!sslValidationDisabled) {
            disableSSLValidation();
        }

        String uri = String.format(INTRADAY_PRICES_URL, API_KEY, symbol, "full");
        RedirectableHttpRequest redirectableRequest = new RedirectableHttpRequest(new URL(uri));
        URLConnection conn = redirectableRequest.openConnection();
        String content = IOUtils.toString(conn.getInputStream());

        List<Quote> quotes = new ArrayList<Quote>();

        boolean firstLine = true;
        for (String line : content.split("\\n|\\r\\n|\\r")) {
            if (firstLine) {
                firstLine = false;
            } else {
                String[] fields = line.split(",");
                Date date;
                try {
                    date = DATE_FORMAT.parse(fields[0]);
                    BigDecimal price = new BigDecimal(fields[4]);
                    int volume = Utils.getInt(fields[5]);
                    quotes.add(new Quote(date, price, volume));
                } catch (ParseException e) {
                    System.err.format("ERROR: Could not parse quote date: '%s'\n", fields[0]);
                }
            }
        }

        return quotes;
    }

    /**
     * Disables SSL certificate validation for all HTTPS connections. <br />
     * <br />
     * 
     * Installs a global {@code SSLSocketFactory} {@code TrustManager} that accepts all certificates.
     */
    private static void disableSSLValidation() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                // Empty implementation.
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                // Empty implementation.
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            sslValidationDisabled = true;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Could not set default SSL socket factory", e);
        }
    }
}
