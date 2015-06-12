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
        // TODO
        return noOfShares;
    }

    public BigDecimal getCurrentInvestment(Stock stock) {
        BigDecimal investment = BigDecimal.ZERO;
        // TODO
        return investment;
    }

    public BigDecimal getCurrentValue(Stock stock) {
        BigDecimal value = BigDecimal.ZERO;
        // TODO
        return value;
    }

    public BigDecimal getCurrentResult(Stock stock) {
        BigDecimal value = BigDecimal.ZERO;
        // TODO
        return value;
    }
}
