package org.ozsoft.stockviewer;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

public class PortfolioTest {

    @Test
    public void test() {
        Portfolio portfolio = new Portfolio("Test");

        Stock stock = new Stock("TST", "Test Stock");
        BigDecimal stockPrice = new BigDecimal("20.00");
        stock.setPrice(stockPrice);

        Assert.assertEquals(0, portfolio.getCurrentNoOfShares(stock));
        Assert.assertEquals(0, portfolio.getCurrentInvestment(stock).intValue());
        Assert.assertEquals(0, portfolio.getCurrentValue(stock).intValue());
        Assert.assertEquals(0, portfolio.getCurrentResult(stock).intValue());

        portfolio.addTransaction(new BuyTransaction(getDate(1, 6, 2015), stock, 100, stockPrice, BigDecimal.ZERO));
        Assert.assertEquals(100, portfolio.getCurrentNoOfShares(stock));
        Assert.assertEquals(new BigDecimal("2000.00"), portfolio.getCurrentInvestment(stock));
        Assert.assertEquals(new BigDecimal("2000.00"), portfolio.getCurrentValue(stock));
        Assert.assertEquals(new BigDecimal("0.00"), portfolio.getCurrentResult(stock));

        stockPrice = new BigDecimal("10.00");
        stock.setPrice(stockPrice);
        portfolio.addTransaction(new BuyTransaction(getDate(2, 7, 2015), stock, 100, stockPrice, BigDecimal.ZERO));
        Assert.assertEquals(200, portfolio.getCurrentNoOfShares(stock));
        Assert.assertEquals(new BigDecimal("3000.00"), portfolio.getCurrentInvestment(stock));
        Assert.assertEquals(new BigDecimal("2000.00"), portfolio.getCurrentValue(stock));
        Assert.assertEquals(new BigDecimal("-1000.00"), portfolio.getCurrentResult(stock));

        portfolio.addTransaction(new DividendTransaction(getDate(4, 8, 2015), stock, new BigDecimal("1.00")));
        Assert.assertEquals(200, portfolio.getCurrentNoOfShares(stock));
        Assert.assertEquals(new BigDecimal("3000.00"), portfolio.getCurrentInvestment(stock));
        Assert.assertEquals(new BigDecimal("2000.00"), portfolio.getCurrentValue(stock));
        Assert.assertEquals(new BigDecimal("-800.00"), portfolio.getCurrentResult(stock));

        portfolio.addTransaction(new SellTransaction(getDate(6, 8, 2015), stock, 100, new BigDecimal("25.00"), BigDecimal.ZERO));
    }

    private static Calendar getDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH - 1, month);
        cal.set(Calendar.YEAR, year);
        return cal;
    }
}
