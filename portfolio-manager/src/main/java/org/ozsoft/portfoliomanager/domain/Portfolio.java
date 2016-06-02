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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Portfolio {

    private final List<Transaction> transactions;

    private final Map<Stock, Position> positions;

    private double currentCost;

    private double currentValue;

    private double totalCost;

    private double totalValue;

    private double annualIncome;

    private double totalIncome;

    private double realizedResult;

    private double totalReturn;

    public Portfolio() {
        transactions = new ArrayList<Transaction>();
        positions = new TreeMap<Stock, Position>();
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Collection<Position> getPositions() {
        return positions.values();
    }

    public Position getPosition(Stock stock) {
        return positions.get(stock);
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getCurrentResult() {
        return currentValue - currentCost;
    }

    public double getCurrentResultPercentage() {
        if (currentCost > 0.0) {
            return (getCurrentResult() / currentCost) * 100.0;
        } else {
            return 0.0;
        }
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public double getAnnualIncome() {
        return annualIncome;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getYieldOnCost() {
        if (currentCost > 0.0) {
            return (annualIncome / currentCost) * 100.0;
        } else {
            return 0.0;
        }
    }

    public double getRealizedResult() {
        return realizedResult;
    }

    public double getTotalReturn() {
        return totalReturn;
    }

    public double getTotalReturnPercentage() {
        // FIXME: Total result % based on average, all-time cost
        if (totalCost > 0.0) {
            return (getTotalReturn() / totalCost) * 100.0;
        } else {
            return 0.0;
        }
    }

    public void update(Configuration config) {
        clear();

        // Update positions based on transactions.
        for (Transaction transaction : transactions) {
            String symbol = transaction.getSymbol();
            Stock stock = config.getStock(symbol);
            if (stock != null) {
                Position position = positions.get(stock);
                if (position == null) {
                    position = new Position(stock);
                    positions.put(stock, position);
                }
                position.addTransaction(transaction);
            }
        }

        // Update totals based on positions.
        for (Position pos : positions.values()) {
            currentCost += pos.getCurrentCost();
            currentValue += pos.getCurrentValue();
            totalCost += pos.getTotalCost();
            totalValue += pos.getCurrentValue();
            annualIncome += pos.getAnnualIncome();
            totalIncome += pos.getTotalIncome();
            realizedResult += pos.getRealizedResult();
            totalReturn += pos.getTotalReturn();
        }
    }

    public void printResults() {
        List<Transaction> transactions = new ArrayList<Transaction>(this.transactions);
        Calendar firstDay = getDay(transactions.get(0).getDate());
        Calendar lastDay = getDay(new Date().getTime());
        // DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        int month = 1 + firstDay.get(Calendar.MONTH);
        int quarter = (int) Math.ceil(month / 3.0);
        int year = firstDay.get(Calendar.YEAR);

        Results monthlyResult = new Results();
        Results quarterlyResult = new Results();
        Results annualResult = new Results();
        Results totalResult = new Results();

        int daysInMonth = 1;
        double currentCost = 0.0;
        Map<Integer, Double> costPerDay = new TreeMap<Integer, Double>();

        Calendar day = firstDay;
        while (!day.after(lastDay)) {
            monthlyResult.setDay(day);
            quarterlyResult.setDay(day);
            annualResult.setDay(day);
            totalResult.setDay(day);

            Transaction tx = getTransactionOnDay(transactions, day);
            if (tx != null) {
                switch (tx.getType()) {
                    case DIVIDEND:
                        double income = tx.getNoOfShares() * tx.getPrice() - tx.getCost();
                        monthlyResult.addIncome(income);
                        quarterlyResult.addIncome(income);
                        annualResult.addIncome(income);
                        totalResult.addIncome(income);
                        break;
                    case BUY:
                        double costs = tx.getNoOfShares() * tx.getPrice() + tx.getCost();
                        Double dayCosts = costPerDay.get(daysInMonth);
                        if (dayCosts == null) {
                            costPerDay.put(daysInMonth, costs);
                        } else {
                            costPerDay.put(daysInMonth, dayCosts + costs);
                        }
                        currentCost += costs;
                        monthlyResult.addCosts(costs);
                        quarterlyResult.addCosts(costs);
                        annualResult.addCosts(costs);
                        totalResult.addCosts(costs);
                        break;
                    case SELL:
                        // FIXME: Use actual cost base
                        costs = tx.getNoOfShares() * tx.getPrice() + tx.getCost();
                        dayCosts = costPerDay.get(daysInMonth);
                        if (dayCosts == null) {
                            costPerDay.put(daysInMonth, -costs);
                        } else {
                            costPerDay.put(daysInMonth, dayCosts - costs);
                        }
                        currentCost -= costs;
                        break;
                }
            } else {
                day.add(Calendar.DAY_OF_YEAR, 1);
                if (1 + day.get(Calendar.MONTH) != month) {
                    double sum = 0.0;
                    for (Integer dayNr : costPerDay.keySet()) {
                        // System.out.format("### %02d-%02d-%04d: Cost: $%,.0f\n", dayNr, month, year, costPerDay.get(dayNr));
                        sum += costPerDay.get(dayNr);
                    }
                    double avgCost = sum / daysInMonth;
                    System.out.format("%02d-%d: Costbase: $%,.0f, Income: $%,.0f\n", month, year, avgCost, monthlyResult.getIncome());
                    monthlyResult.clear();
                    month = 1 + day.get(Calendar.MONTH);
                    costPerDay.clear();
                    daysInMonth = 1;
                } else {
                    daysInMonth++;
                }
                costPerDay.put(daysInMonth, currentCost);
                if ((int) Math.ceil(month / 3.0) != quarter) {
                    System.out.format("%d-Q%d: Costbase: $%,.0f, Income: $%,.0f\n", year, quarter, currentCost, quarterlyResult.getIncome());
                    quarterlyResult.clear();
                    quarter = (int) Math.ceil(month / 3.0);
                }
                if (day.get(Calendar.YEAR) > year) {
                    System.out.format("%d: Costbase: $%,.0f, Income: $%,.0f\n", year, currentCost, annualResult.getIncome());
                    annualResult.clear();
                    year++;
                }
            }
        }

        System.out.format("%02d-%d: Costbase: $%,.0f, Income: $%,.0f\n", month, year, currentCost, monthlyResult.getIncome());
        System.out.format("%d-Q%d: Costbase: $%,.0f, Income: $%,.0f\n", year, quarter, currentCost, quarterlyResult.getIncome());
        System.out.format("%d: Costbase: $%,.0f, Income: $%,.0f\n", year, currentCost, annualResult.getIncome());
        System.out.format("Overall: Costbase: $%,.0f, Income: $%,.0f\n", currentCost, totalResult.getIncome());
    }

    private void clear() {
        positions.clear();
        currentCost = 0.0;
        currentValue = 0.0;
        totalCost = 0.0;
        totalValue = 0.0;
        annualIncome = 0.0;
        totalIncome = 0.0;
        realizedResult = 0.0;
        totalReturn = 0.0;
    }

    private static Transaction getTransactionOnDay(List<Transaction> transactions, Calendar day) {
        if (!transactions.isEmpty()) {
            if (getDay(transactions.get(0).getDate()).equals(day)) {
                return transactions.remove(0);
            }
        }
        return null;
    }

    private static Calendar getDay(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}
