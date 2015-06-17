package org.ozsoft.portfolio;

public class Transaction implements Comparable<Transaction> {

    private final long date;

    private final TransactionType type;

    private final Stock stock;

    private final int noOfShares;

    private final double price;

    private final double costs;

    public Transaction(long date, TransactionType type, Stock stock, int noOfShares, double price, double costs) {
        this.date = date;
        this.type = type;
        this.stock = stock;
        this.noOfShares = noOfShares;
        this.price = price;
        this.costs = costs;
    }

    public long getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public Stock getStock() {
        return stock;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public double getPrice() {
        return price;
    }

    public double getCosts() {
        return costs;
    }

    @Override
    public int compareTo(Transaction other) {
        return (int) (date - other.getDate());
    }
}
