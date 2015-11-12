package org.ozsoft.portfoliomanager.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    private double totalIncome;

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

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalReturn() {
        return getCurrentResult() + totalReturn;
    }

    public double getTotalReturnPercentage() {
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
            totalIncome += pos.getTotalIncome();
            totalReturn += pos.getTotalReturn();
        }
    }

    private void clear() {
        positions.clear();
        currentCost = 0.0;
        currentValue = 0.0;
        totalCost = 0.0;
        totalValue = 0.0;
        totalIncome = 0.0;
        totalReturn = 0.0;
    }
}
