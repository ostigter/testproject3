package org.ozsoft.portfolio;

public class Position implements Comparable<Position> {

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

    public void addTransaction(Transaction tx) {
        switch (tx.getType()) {
            case BUY:
                this.noOfShares += tx.getNoOfShares();
                double investment = (tx.getNoOfShares() * tx.getPrice()) + tx.getCosts();
                currentInvestment += investment;
                overallInvestment += investment;
                break;
            case SELL:
                if (tx.getNoOfShares() > noOfShares) {
                    throw new IllegalArgumentException("Cannot sell more shares than owned");
                }
                double avgPrice = currentInvestment / noOfShares;
                currentInvestment -= (tx.getNoOfShares() * avgPrice);
                overallInvestment += tx.getCosts();
                overallResult += tx.getNoOfShares() * (tx.getPrice() - avgPrice) - tx.getCosts();
                noOfShares -= tx.getNoOfShares();
                break;
            case DIVIDEND:
                overallResult += noOfShares * tx.getPrice();
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type");
        }
    }

    @Override
    public int compareTo(Position other) {
        return stock.compareTo(other.getStock());
    }
}
