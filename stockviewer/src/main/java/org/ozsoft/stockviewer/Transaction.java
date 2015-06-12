package org.ozsoft.stockviewer;

import java.util.Calendar;

public abstract class Transaction implements Comparable<Transaction> {

    private final Calendar date;

    private final Stock stock;

    public Transaction(Calendar date, Stock stock) {
        this.date = date;
        this.stock = stock;
    }

    public Calendar getDate() {
        return date;
    }

    public Stock getStock() {
        return stock;
    }

    @Override
    public int compareTo(Transaction other) {
        return date.compareTo(other.getDate());
    }
}
