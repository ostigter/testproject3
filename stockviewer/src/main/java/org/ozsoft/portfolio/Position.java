package org.ozsoft.portfolio;

public class Position {

    private final Stock stock;

    private int noOfShares = 0;

    private double currentInvestment = 0.00;

    private double overallInvestment = 0.00;

    private double overallResult = 0.00;

    public Position(Stock stock) {
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(int noOfShares) {
        this.noOfShares = noOfShares;
    }

    public double getCurrentInvestment() {
        return currentInvestment;
    }

    public double getCurrentValue() {
        return noOfShares * stock.getPrice();
    }

    public double getCurrentResult() {
        return getCurrentValue() - getCurrentInvestment();
    }

    public double getCurrentResultPercentage() {
        double currentInvestment = getCurrentInvestment();
        if (currentInvestment > 0.00) {
            return (getCurrentResult() / currentInvestment) * 100.0;
        } else {
            return 0.00;
        }
    }

    public double getOverallInvestment() {
        return overallInvestment;
    }

    public double getOverallResult() {
        return overallResult;
    }

    public double getOverallResultPercentage() {
        double overallInvestement = getOverallInvestment();
        if (overallInvestement > 0.00) {
            return (getOverallResult() / overallInvestment) * 100.0;
        } else {
            return 0.00;
        }
    }

    public void buy(int noOfShares, double price, double costs) {
        this.noOfShares += noOfShares;
        currentInvestment += (noOfShares * price) + costs;
        overallInvestment += (noOfShares * price) + costs;
    }

    public void sell(int noOfShares, double price, double costs) {
        if (noOfShares > this.noOfShares) {
            throw new IllegalArgumentException("Cannot sell more shares than owned");
        }

        double avgPrice = currentInvestment / this.noOfShares;
        currentInvestment -= (noOfShares * avgPrice);
        overallResult += noOfShares * (price - avgPrice) - costs;
        this.noOfShares -= noOfShares;
    }

    public void receiveDividend(double price) {
        overallResult += noOfShares * price;
    }
}
