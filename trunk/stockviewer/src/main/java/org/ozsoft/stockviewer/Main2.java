package org.ozsoft.stockviewer;

import java.math.BigDecimal;
import java.util.Calendar;

public class Main2 {

    public static void main(String[] args) {
        Stock stock = new Stock("PER", "SandRidge Permian Trust");

        // QuoteUpdater quoteUpdater = new QuoteUpdater();
        // quoteUpdater.update(stock);
        // System.out.printf("%s: %s\n", stock, stock.getPrice());

        Portfolio portfolio = new Portfolio("Watchlist");
        portfolio.addTransaction(new BuyTransaction(getDate(1, 6, 2015), stock, 100, BigDecimal.valueOf(20.00), BigDecimal.ZERO));
        portfolio.addTransaction(new BuyTransaction(getDate(2, 7, 2015), stock, 100, BigDecimal.valueOf(10.00), BigDecimal.ZERO));
        portfolio.addTransaction(new DividendTransaction(getDate(4, 8, 2015), stock, BigDecimal.valueOf(1.00)));
        portfolio.addTransaction(new SellTransaction(getDate(6, 8, 2015), stock, 100, BigDecimal.valueOf(25.00), BigDecimal.ZERO));

        for (Transaction transaction : portfolio.getTransactions()) {
            System.out.println(transaction);
        }
    }

    private static Calendar getDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH - 1, month);
        cal.set(Calendar.YEAR, year);
        return cal;
    }
}
