package org.ozsoft.portfolio;

import org.junit.Assert;
import org.junit.Test;

public class PortfolioTest {

    private static final double DELTA = 0.01;

    @Test
    public void test() {
        // Create some stock.
        Stock stock = new Stock("TST", "Test Stock");

        // Create new (empty) portfolio.
        Portfolio portfolio = new Portfolio("Test");
        Position position = portfolio.getPosition(stock);
        Assert.assertNull(position);

        // Add some transactions.
        portfolio.addTransaction(new Transaction(1L, TransactionType.BUY, stock, 100, 20.00, 5.00));
        portfolio.addTransaction(new Transaction(2L, TransactionType.DIVIDEND, stock, 100, 1.00, 0.00));
        portfolio.addTransaction(new Transaction(3L, TransactionType.BUY, stock, 100, 10.00, 5.00));
        portfolio.addTransaction(new Transaction(4L, TransactionType.DIVIDEND, stock, 200, 1.25, 0.00));
        portfolio.addTransaction(new Transaction(5L, TransactionType.SELL, stock, 200, 20.00, 10.00));
        portfolio.updatePositions();

        // Check position.
        position = portfolio.getPosition(stock);
        Assert.assertNotNull(position);
        Assert.assertEquals(0, position.getNoOfShares());
        Assert.assertEquals(0.00, position.getCurrentInvestment(), DELTA);
        Assert.assertEquals(0.00, position.getCurrentValue(), DELTA);
        Assert.assertEquals(0.00, position.getCurrentResult(), DELTA);
        Assert.assertEquals(0.00, position.getCurrentResultPercentage(), DELTA);
        Assert.assertEquals(3020.00, position.getOverallInvestment(), DELTA);
        Assert.assertEquals(+1330.00, position.getOverallResult(), DELTA);
        Assert.assertEquals(+44.04, position.getOverallResultPercentage(), DELTA);
    }
}
