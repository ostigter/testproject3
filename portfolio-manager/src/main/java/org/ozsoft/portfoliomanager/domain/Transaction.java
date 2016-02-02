package org.ozsoft.portfoliomanager.domain;

public class Transaction implements Comparable<Transaction> {

    private long date;

    private final TransactionType type;

    private final String symbol;

    private final int noOfShares;

    private final double price;

    private final double cost;

    public Transaction(long date, TransactionType type, String symbol, int noOfShares, double price, double cost) {
        this.date = date;
        this.type = type;
        this.symbol = symbol;
        this.noOfShares = noOfShares;
        this.price = price;
        this.cost = cost;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public double getPrice() {
        return price;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public int compareTo(Transaction other) {
        return (int) (date - other.getDate());
    }

    @Override
    public String toString() {
        return String.format("%s: %s %d $%.2f $%.2f", symbol, type, noOfShares, price, cost);
    }
}
