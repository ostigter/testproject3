package org.ozsoft.stockbase;

import java.util.Locale;

public class Main {

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);

        long startTime, duration;

        startTime = System.currentTimeMillis();
        System.out.println(StockBase.getCurrentPrice("AAPL"));
        duration = System.currentTimeMillis() - startTime;
        System.out.format("Duration: %,d ms\n", duration);

        // startTime = System.currentTimeMillis();
        // Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        // List<Quote> quotes = StockBase.getIntradayPrices("AAPL");
        // duration = System.currentTimeMillis() - startTime;
        // for (Quote quote : quotes) {
        // // System.out.println(quote);
        // cal.setTime(quote.getDate());
        // String date = Utils.formatCalendar(cal);
        // String volume = Utils.formatVolume(quote.getVolume());
        // System.out.format("%s: $ %,.2f (%s)\n", date, quote.getPrice(), volume);
        // }
        // System.out.format("Duration: %,d ms\n", duration);

        // for (Quote quote : StockBase.getHistoricPrices("JNJ")) {
        // System.out.println(quote);
        // }

        // for (Quote quote : StockBase.getHistoricDividends("JNJ")) {
        // System.out.println(quote);
        // }
    }
}
