package org.ozsoft.portfoliomanager.domain;

public class Transaction implements Comparable<Transaction> {

    private int id;

    private long date;

    private String symbol;

    private TransactionType type;

    private int noOfShares;

    private double price;

    private double cost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(int noOfShares) {
        this.noOfShares = noOfShares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(date).hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Transaction) {
            Transaction tx = (Transaction) other;
            return symbol.equals(tx.getSymbol()) && date == tx.getDate();
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Transaction other) {
        if (date < other.getDate()) {
            return -1;
        } else if (date > other.getDate()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("%d %d %s %s %d $%.2f $%.2f", id, date, symbol, type, noOfShares, price, cost);
    }
}
