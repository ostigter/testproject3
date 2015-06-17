package org.ozsoft.portfolio;

import org.junit.Assert;
import org.junit.Test;

public class PortfolioTest {

    private static final double DELTA = 0.01;

    @Test
    public void test() {
        Stock stock = new Stock("TST", "Test Stock");
        stock.setDate(0L);
        stock.setPrice(20.00);

        Portfolio portfolio = new Portfolio("Test");
        Assert.assertEquals(0, portfolio.getCurrentNoOfShares(stock));

        portfolio.addTransaction(new Transaction(1L, TransactionType.BUY, stock, 100, 20.00, 2.00));
        Assert.assertEquals(100, portfolio.getCurrentNoOfShares(stock));
        Assert.assertEquals(2002.00, portfolio.getCurrentInvestment(stock), DELTA);
        Assert.assertEquals(2000.00, portfolio.getCurrentValue(stock), DELTA);
        Assert.assertEquals(-2.00, portfolio.getCurrentChange(stock), DELTA);
        Assert.assertEquals(-0.10, portfolio.getCurrentChangePercentage(stock), DELTA);

        stock.setDate(2L);
        stock.setPrice(10.00);
        portfolio.addTransaction(new Transaction(2L, TransactionType.BUY, stock, 100, 10.00, 1.00));
        Assert.assertEquals(200, portfolio.getCurrentNoOfShares(stock));
        Assert.assertEquals(3003.00, portfolio.getCurrentInvestment(stock), DELTA);
        Assert.assertEquals(2000.00, portfolio.getCurrentValue(stock), DELTA);
        Assert.assertEquals(-1003.00, portfolio.getCurrentChange(stock), DELTA);
        Assert.assertEquals(-33.40, portfolio.getCurrentChangePercentage(stock), DELTA);

        stock.setDate(3L);
        stock.setPrice(16.00);
        portfolio.addTransaction(new Transaction(3L, TransactionType.SELL, stock, 100, 16.00, 1.60));
        Assert.assertEquals(100, portfolio.getCurrentNoOfShares(stock));
        Assert.assertEquals(1503.10, portfolio.getCurrentInvestment(stock), DELTA);
        Assert.assertEquals(1600.00, portfolio.getCurrentValue(stock), DELTA);
        Assert.assertEquals(+96.90, portfolio.getCurrentChange(stock), DELTA);
        Assert.assertEquals(+6.45, portfolio.getCurrentChangePercentage(stock), DELTA);
    }
}
