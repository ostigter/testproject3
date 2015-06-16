package org.ozsoft.stockviewer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class Portfolio {

    private final String name;

    private final Set<Transaction> transactions;

    public Portfolio(String name) {
        this.name = name;
        transactions = new TreeSet<Transaction>();
    }

    public String getName() {
        return name;
    }

    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSet(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public int getCurrentNoOfShares(Stock stock) {
        int noOfShares = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getStock().equals(stock)) {
                if (transaction instanceof BuyTransaction) {
                    noOfShares += ((BuyTransaction) transaction).getNoOfShares();
                } else if (transaction instanceof SellTransaction) {
                    noOfShares -= ((SellTransaction) transaction).getNoOfShares();
                }
            }
        }
        return noOfShares;
    }

    public BigDecimal getCurrentInvestment(Stock stock) {
        BigDecimal investment = BigDecimal.ZERO;
        int noOfShares = 0;
        BigDecimal avgPrice = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction.getStock().equals(stock)) {
                if (transaction instanceof BuyTransaction) {
                    BuyTransaction buy = (BuyTransaction) transaction;
                    noOfShares += buy.getNoOfShares();
                    investment = investment.add(buy.getStockPrice().multiply(BigDecimal.valueOf(buy.getNoOfShares()))).add(buy.getTransactionCosts());
                    avgPrice = investment.divide(BigDecimal.valueOf(noOfShares));
                } else if (transaction instanceof SellTransaction) {
                    SellTransaction sell = (SellTransaction) transaction;
                    noOfShares += sell.getNoOfShares();
                    investment = investment.subtract((avgPrice.multiply(BigDecimal.valueOf(sell.getNoOfShares()))));
                }
            }
        }
        return investment;
    }

    public BigDecimal getCurrentValue(Stock stock) {
        return stock.getPrice().multiply(BigDecimal.valueOf(getCurrentNoOfShares(stock)));
    }

    public BigDecimal getCurrentResult(Stock stock) {
        BigDecimal dividends = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction.getStock().equals(stock) && (transaction instanceof DividendTransaction)) {
                DividendTransaction dividend = (DividendTransaction) transaction;
                // FIXME: Determine number of shares.
                dividends = dividends.add(dividend.getDividendPerShare());
            }
        }
        return getCurrentValue(stock).subtract(getCurrentInvestment(stock)).add(dividends);
    }
}
