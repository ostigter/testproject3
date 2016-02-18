package org.ozsoft.stockviewer;

import java.util.Date;

public class Stock implements Comparable<Stock> {

    private final String symbol;

    private Date date;

    private double price;

    private double change;

    private ChangeFlag changeFlag;

    public Stock(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ChangeFlag getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(ChangeFlag changeFlag) {
        this.changeFlag = changeFlag;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Stock) {
            return ((Stock) other).getSymbol().equals(symbol);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public int compareTo(Stock other) {
        return symbol.compareTo(other.getSymbol());
    }
}
