package org.ozsoft.portfoliomanager.domain;

public class Position implements Comparable<Position> {

    private final Stock stock;

    private int noOfShares = 0;

    private double currentCost = 0.0;

    private double totalCost = 0.0;

    private double totalIncome = 0.0;

    private double realizedResult = 0.0;

    private double totalReturn = 0.0;

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

    public double getCurrentCost() {
        return currentCost;
    }

    public double getCurrentValue() {
        return noOfShares * stock.getPrice();
    }

    public double getCurrentResult() {
        return getCurrentValue() - getCurrentCost();
    }

    public double getCurrentResultPercentage() {
        double currentInvestment = getCurrentCost();
        if (currentInvestment > 0.0) {
            return (getCurrentResult() / currentInvestment) * 100.0;
        } else {
            return 0.00;
        }
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getCostPerShare() {
        double currentInvestment = getCurrentCost();
        if (currentInvestment > 0.0) {
            return currentInvestment / noOfShares;
        } else {
            return 0.00;
        }
    }

    public double getAnnualIncome() {
        return noOfShares * stock.getDivRate();
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getYieldOnCost() {
        if (totalCost > 0.0) {
            return (getAnnualIncome() / totalCost) * 100.0;
        } else {
            return 0.0;
        }
    }

    public double getRealizedResult() {
        return realizedResult;
    }

    public double getTotalReturn() {
        return getCurrentResult() + totalReturn;
    }

    public double getTotalReturnPercentage() {
        if (totalCost > 0.00) {
            return (getTotalReturn() / totalCost) * 100.0;
        } else {
            return 0.00;
        }
    }

    public void addTransaction(Transaction tx) {
        switch (tx.getType()) {
        case BUY:
            this.noOfShares += tx.getNoOfShares();
            double cost = (tx.getNoOfShares() * tx.getPrice()) + tx.getCost();
            currentCost += cost;
            totalCost += cost;
            break;
        case SELL:
            if (tx.getNoOfShares() > noOfShares) {
                throw new IllegalArgumentException("Cannot sell more shares than owned");
            }
            double avgPrice = currentCost / noOfShares;
            double value = tx.getNoOfShares() * avgPrice;
            currentCost -= value;
            totalCost += tx.getCost();
            double profit = tx.getNoOfShares() * (tx.getPrice() - avgPrice) - tx.getCost();
            realizedResult += profit;
            totalReturn += profit;
            noOfShares -= tx.getNoOfShares();
            break;
        case DIVIDEND:
            double income = tx.getNoOfShares() * tx.getPrice();
            totalIncome += income;
            totalReturn += income;
            break;
        default:
            throw new IllegalArgumentException("Invalid transaction type");
        }
    }

    @Override
    public int compareTo(Position other) {
        return stock.compareTo(other.getStock());
    }

    @Override
    public String toString() {
        return stock.toString();
    }
}
