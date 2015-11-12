package org.ozsoft.portfoliomanager.domain;

import org.junit.Assert;
import org.junit.Test;

public class PositionTest {

    private static final double DELTA = 0.01;

    @Test
    public void test() {
        // Stock stock = new Stock("TST", "Test Stock");
        // stock.setPrice(10.00);
        //
        // // Initial (empty) position.
        // Position position = new Position(stock);
        // Assert.assertEquals(0, position.getNoOfShares());
        // Assert.assertEquals(0.00, position.getCurrentCost(), DELTA);
        // Assert.assertEquals(0.00, position.getCurrentValue(), DELTA);
        // Assert.assertEquals(0.00, position.getCurrentResult(), DELTA);
        // Assert.assertEquals(0.00, position.getTotalCost(), DELTA);
        // Assert.assertEquals(0.00, position.getTotalDividend(), DELTA);
        // Assert.assertEquals(0.00, position.getYieldOnCost(), DELTA);
        // Assert.assertEquals(0.00, position.getTotalReturn(), DELTA);
        // Assert.assertEquals(0.00, position.getTotalReturnPercentage(), DELTA);
        //
        // // BUY 100 @ $20 ($5 costs)
        // stock.setPrice(20.00);
        // position.addTransaction(new Transaction(1L, TransactionType.BUY, stock, 100, 20.00, 5.00));
        // Assert.assertEquals(100, position.getNoOfShares());
        // Assert.assertEquals(2005.00, position.getCurrentCost(), DELTA);
        // Assert.assertEquals(2000.00, position.getCurrentValue(), DELTA);
        // Assert.assertEquals(-5.00, position.getCurrentResult(), DELTA);
        // Assert.assertEquals(2005.00, position.getTotalCost(), DELTA);
        // Assert.assertEquals(0.00, position.getTotalDividend(), DELTA);
        // Assert.assertEquals(0.00, position.getYieldOnCost(), DELTA);
        // Assert.assertEquals(-5.00, position.getTotalReturn(), DELTA);
        // Assert.assertEquals(-0.25, position.getTotalReturnPercentage(), DELTA);
        //
        // // DIVIDEND 100 @ $1.00
        // position.addTransaction(new Transaction(2L, TransactionType.DIVIDEND, stock, 100, 1.00, 0.00));
        // Assert.assertEquals(100, position.getNoOfShares());
        // Assert.assertEquals(2005.00, position.getCurrentCost(), DELTA);
        // Assert.assertEquals(2000.00, position.getCurrentValue(), DELTA);
        // Assert.assertEquals(-5.00, position.getCurrentResult(), DELTA);
        // Assert.assertEquals(2005.00, position.getTotalCost(), DELTA);
        // Assert.assertEquals(100.00, position.getTotalDividend(), DELTA);
        // Assert.assertEquals(4.99, position.getYieldOnCost(), DELTA);
        // Assert.assertEquals(+95.00, position.getTotalReturn(), DELTA);
        // Assert.assertEquals(+4.74, position.getTotalReturnPercentage(), DELTA);
        //
        // // Price drops to $10
        // stock.setPrice(10.00);
        // Assert.assertEquals(100, position.getNoOfShares());
        // Assert.assertEquals(2005.00, position.getCurrentCost(), DELTA);
        // Assert.assertEquals(1000.00, position.getCurrentValue(), DELTA);
        // Assert.assertEquals(-1005.00, position.getCurrentResult(), DELTA);
        // Assert.assertEquals(-50.12, position.getCurrentResultPercentage(), DELTA);
        // Assert.assertEquals(2005.00, position.getTotalCost(), DELTA);
        // Assert.assertEquals(100.00, position.getTotalDividend(), DELTA);
        // Assert.assertEquals(4.99, position.getYieldOnCost(), DELTA);
        // Assert.assertEquals(-905.00, position.getTotalReturn(), DELTA);
        // Assert.assertEquals(-45.14, position.getTotalReturnPercentage(), DELTA);
        //
        // // BUY another 100 @ $10 ($5 costs)
        // position.addTransaction(new Transaction(3L, TransactionType.BUY, stock, 100, 10.00, 5.00));
        // Assert.assertEquals(200, position.getNoOfShares());
        // Assert.assertEquals(3010.00, position.getCurrentCost(), DELTA);
        // Assert.assertEquals(2000.00, position.getCurrentValue(), DELTA);
        // Assert.assertEquals(-1010.00, position.getCurrentResult(), DELTA);
        // Assert.assertEquals(-33.55, position.getCurrentResultPercentage(), DELTA);
        // Assert.assertEquals(3010.00, position.getTotalCost(), DELTA);
        // Assert.assertEquals(100.00, position.getTotalDividend(), DELTA);
        // Assert.assertEquals(3.32, position.getYieldOnCost(), DELTA);
        // Assert.assertEquals(-910.00, position.getTotalReturn(), DELTA);
        // Assert.assertEquals(-30.23, position.getTotalReturnPercentage(), DELTA);
        //
        // // Price raises to $20 again
        // stock.setPrice(20.00);
        // Assert.assertEquals(200, position.getNoOfShares());
        // Assert.assertEquals(3010.00, position.getCurrentCost(), DELTA);
        // Assert.assertEquals(4000.00, position.getCurrentValue(), DELTA);
        // Assert.assertEquals(+990.00, position.getCurrentResult(), DELTA);
        // Assert.assertEquals(+32.89, position.getCurrentResultPercentage(), DELTA);
        // Assert.assertEquals(3010.00, position.getTotalCost(), DELTA);
        // Assert.assertEquals(100.00, position.getTotalDividend(), DELTA);
        // Assert.assertEquals(3.32, position.getYieldOnCost(), DELTA);
        // Assert.assertEquals(+1090.00, position.getTotalReturn(), DELTA);
        // Assert.assertEquals(+36.21, position.getTotalReturnPercentage(), DELTA);
        //
        // // DIVIDEND 200 @ $1.25
        // position.addTransaction(new Transaction(3L, TransactionType.DIVIDEND, stock, 200, 1.25, 0.00));
        // Assert.assertEquals(200, position.getNoOfShares());
        // Assert.assertEquals(3010.00, position.getCurrentCost(), DELTA);
        // Assert.assertEquals(4000.00, position.getCurrentValue(), DELTA);
        // Assert.assertEquals(+990.00, position.getCurrentResult(), DELTA);
        // Assert.assertEquals(+32.89, position.getCurrentResultPercentage(), DELTA);
        // Assert.assertEquals(3010.00, position.getTotalCost(), DELTA);
        // Assert.assertEquals(350.00, position.getTotalDividend(), DELTA);
        // Assert.assertEquals(11.63, position.getYieldOnCost(), DELTA);
        // Assert.assertEquals(+1340.00, position.getTotalReturn(), DELTA);
        // Assert.assertEquals(+44.52, position.getTotalReturnPercentage(), DELTA);
        //
        // // SELL 200 @ $20 ($10 costs)
        // position.addTransaction(new Transaction(4L, TransactionType.SELL, stock, 200, 20.00, 10.00));
        // Assert.assertEquals(0, position.getNoOfShares());
        // Assert.assertEquals(0.00, position.getCurrentCost(), DELTA);
        // Assert.assertEquals(0.00, position.getCurrentValue(), DELTA);
        // Assert.assertEquals(0.00, position.getCurrentResult(), DELTA);
        // Assert.assertEquals(0.00, position.getCurrentResultPercentage(), DELTA);
        // Assert.assertEquals(3020.00, position.getTotalCost(), DELTA);
        // Assert.assertEquals(350.00, position.getTotalDividend(), DELTA);
        // Assert.assertEquals(11.59, position.getYieldOnCost(), DELTA);
        // Assert.assertEquals(+1330.00, position.getTotalReturn(), DELTA);
        // Assert.assertEquals(+44.04, position.getTotalReturnPercentage(), DELTA);
    }
}
