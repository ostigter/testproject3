package org.ozsoft.stockviewer;

import java.math.BigDecimal;
import java.util.Calendar;

public class BuyTransaction extends Transaction {

    private final int noOfShares;

    private final BigDecimal stockPrice;

    private final BigDecimal transactionCosts;

    public BuyTransaction(Calendar date, Stock stock, int noOfShares, BigDecimal stockPrice, BigDecimal transactionCosts) {
        super(date, stock);
        this.noOfShares = noOfShares;
        this.stockPrice = stockPrice;
        this.transactionCosts = transactionCosts;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    public BigDecimal getTransactionCosts() {
        return transactionCosts;
    }

    @Override
    public String toString() {
        return String.format("Buy %d %s @ %s", noOfShares, getStock().getSymbol(), stockPrice);
    }
}
