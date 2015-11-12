package org.ozsoft.portfoliomanager.domain;

public class Stock implements Comparable<Stock> {

    private final String symbol;

    private String name;

    private double price;

    private double previousPrice = -1.0;

    private double peRatio = -1.0;

    private double divRate;

    private double divGrowth = -1.0;

    private int yearsDivGrowth = -1;

    private CreditRating creditRating = CreditRating.NA;

    private String comment;

    private StockLevel level = StockLevel.WATCH;

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

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(double previousPrice) {
        this.previousPrice = previousPrice;
    }

    public double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    public double getDivRate() {
        return divRate;
    }

    public void setDivRate(double divRate) {
        this.divRate = divRate;
    }

    public double getYield() {
        if (price > 0.0) {
            double yield = (divRate / price) * 100.0;
            if (yield < 0.0) {
                yield = 0.0;
            }
            return (divRate / price) * 100.0;
        } else {
            return 0.0;
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public StockLevel getLevel() {
        return level;
    }

    public void setLevel(StockLevel level) {
        this.level = level;
    }

    public int getYearsDivGrowth() {
        return yearsDivGrowth;
    }

    public void setYearsDivGrowth(int yearsDivGrowth) {
        this.yearsDivGrowth = yearsDivGrowth;
    }

    public double getDivGrowth() {
        return divGrowth;
    }

    public void setDivGrowth(double divGrowth) {
        this.divGrowth = divGrowth;
    }

    public CreditRating getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(CreditRating creditRating) {
        this.creditRating = creditRating;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if ((other != null) && (other instanceof Stock)) {
            return ((Stock) other).getSymbol().equals(symbol);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, symbol);
    }

    @Override
    public int compareTo(Stock other) {
        return name.compareTo(other.getName());
    }
}
