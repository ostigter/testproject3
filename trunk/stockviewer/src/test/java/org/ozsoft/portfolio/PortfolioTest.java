package org.ozsoft.portfolio;

import org.junit.Assert;
import org.junit.Test;

public class PortfolioTest {

    private static final double DELTA = 0.01;

    @Test
    public void test() {
        Stock stock = new Stock("TST", "Test Stock");
        stock.setPrice(20.00);

        Portfolio portfolio = new Portfolio("Test");

        // Buy 100 @ $20.00 ($2.00 costs)
        portfolio.addTransaction(new Transaction(1L, TransactionType.BUY, stock, 100, 20.00, 2.00));
        portfolio.updatePositions();
        Position position = portfolio.getPosition(stock);
        Assert.assertNotNull("Position not found", stock);
        Assert.assertEquals(100, position.getNoOfShares());
        Assert.assertEquals(2002.00, position.getCurrentInvestment(), DELTA);
        Assert.assertEquals(2000.00, position.getCurrentValue(), DELTA);
        Assert.assertEquals(-2.00, position.getCurrentResult(), DELTA);
        Assert.assertEquals(-0.10, position.getCurrentResultPercentage(), DELTA);
        Assert.assertEquals(2002.00, position.getOverallInvestment(), DELTA);
        Assert.assertEquals(0.00, position.getOverallResult(), DELTA);
        Assert.assertEquals(0.00, position.getOverallResultPercentage(), DELTA);

        // Buy 100 @ $10.00 ($2.00 costs)
        stock.setPrice(10.00);
        portfolio.addTransaction(new Transaction(2L, TransactionType.BUY, stock, 100, 10.00, 2.00));
        portfolio.updatePositions();
        position = portfolio.getPosition(stock);
        Assert.assertEquals(200, position.getNoOfShares());
        Assert.assertEquals(3004.00, position.getCurrentInvestment(), DELTA);
        Assert.assertEquals(2000.00, position.getCurrentValue(), DELTA);
        Assert.assertEquals(-1004.00, position.getCurrentResult(), DELTA);
        Assert.assertEquals(-33.42, position.getCurrentResultPercentage(), DELTA);
        Assert.assertEquals(3004.00, position.getOverallInvestment(), DELTA);
        Assert.assertEquals(0.00, position.getOverallResult(), DELTA);
        Assert.assertEquals(0.00, position.getOverallResultPercentage(), DELTA);

        // Sell 200 @ $20.00 ($2.00 costs)
        stock.setPrice(20.00);
        portfolio.addTransaction(new Transaction(3L, TransactionType.SELL, stock, 200, 20.00, 2.00));
        portfolio.updatePositions();
        position = portfolio.getPosition(stock);
        Assert.assertEquals(0, position.getNoOfShares());
        Assert.assertEquals(0.00, position.getCurrentInvestment(), DELTA);
        Assert.assertEquals(0.00, position.getCurrentValue(), DELTA);
        Assert.assertEquals(0.00, position.getCurrentResult(), DELTA);
        Assert.assertEquals(0.00, position.getCurrentResultPercentage(), DELTA);
        Assert.assertEquals(3006.00, position.getOverallInvestment(), DELTA);
        Assert.assertEquals(+994.00, position.getOverallResult(), DELTA);
        Assert.assertEquals(+33.07, position.getOverallResultPercentage(), DELTA);
    }
}
