package org.ozsoft.portfolio;

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
        for (Transaction tx : transactions) {
            if (tx.getStock().equals(stock)) {
                if (tx.getType() == TransactionType.BUY) {
                    noOfShares += tx.getNoOfShares();
                } else if (tx.getType() == TransactionType.SELL) {
                    noOfShares -= tx.getNoOfShares();
                }
            }
        }
        return noOfShares;
    }

    public double getCurrentInvestment(Stock stock) {
        double investment = 0.0;
        int noOfShares = 0;
        for (Transaction tx : transactions) {
            if (tx.getStock().equals(stock)) {
                if (tx.getType() == TransactionType.BUY) {
                    investment += tx.getNoOfShares() * tx.getPrice() + tx.getCosts();
                    noOfShares += tx.getNoOfShares();
                } else if (tx.getType() == TransactionType.SELL) {
                    investment -= tx.getNoOfShares() * (investment / noOfShares) - tx.getCosts();
                    noOfShares -= tx.getNoOfShares();
                }
            }
        }
        return investment;
    }

    public double getCurrentValue(Stock stock) {
        return getCurrentNoOfShares(stock) * stock.getPrice();
    }

    public double getCurrentChange(Stock stock) {
        return getCurrentValue(stock) - getCurrentInvestment(stock);
    }

    public double getCurrentChangePercentage(Stock stock) {
        return getCurrentChange(stock) / getCurrentInvestment(stock) * 100.0;
    }
}
