package org.ozsoft.stockviewer;

import java.math.BigDecimal;
import java.util.Calendar;

public class Stock {

    private final String symbol;

    private final String name;

    private Calendar date;

    private BigDecimal price;

    private BigDecimal previousPrice;

    public Stock(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(BigDecimal previousPrice) {
        this.previousPrice = previousPrice;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Stock) {
            return ((Stock) other).getName().equals(name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, symbol);
    }
}
