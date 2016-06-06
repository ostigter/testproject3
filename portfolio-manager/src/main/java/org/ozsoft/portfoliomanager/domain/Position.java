// This file is part of the 'portfolio-manager' (Portfolio Manager)
// project, an open source stock portfolio manager application
// written in Java.
//
// Copyright 2015 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.portfoliomanager.domain;

public class Position implements Comparable<Position> {

    private static final double MIN_COST = 0.50;

    private final Configuration config = Configuration.getInstance();

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
        double annualIncome = noOfShares * stock.getDivRate();
        if (config.isSubtractDividendTax()) {
            annualIncome *= (1.0 - Configuration.getDividendTaxRate());
        }
        return annualIncome;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getYieldOnCost() {
        if (currentCost > 0.0) {
            return (getAnnualIncome() / currentCost) * 100.0;
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
                if (currentCost < MIN_COST) {
                    // Round very low cost down to 0 to avoid rounding errors.
                    currentCost = 0.0;
                }
                totalCost += tx.getCost();
                double profit = tx.getNoOfShares() * (tx.getPrice() - avgPrice) - tx.getCost();
                realizedResult += profit;
                totalReturn += profit;
                noOfShares -= tx.getNoOfShares();
                break;
            case DIVIDEND:
                double income = tx.getNoOfShares() * tx.getPrice();
                if (config.isSubtractDividendTax()) {
                    income *= (1.0 - Configuration.getDividendTaxRate());
                }
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
