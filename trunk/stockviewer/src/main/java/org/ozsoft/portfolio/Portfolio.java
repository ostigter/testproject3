package org.ozsoft.portfolio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Portfolio {

    private final String name;

    private final List<Transaction> transactions;

    private final Map<Stock, Position> positions;

    public Portfolio(String name) {
        this.name = name;
        transactions = new ArrayList<Transaction>();
        positions = new TreeMap<Stock, Position>();
    }

    public String getName() {
        return name;
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

    public void updatePositions() {
        positions.clear();
        for (Transaction transaction : transactions) {
            Stock stock = transaction.getStock();
            Position position = positions.get(stock);
            if (position == null) {
                position = new Position(stock);
                positions.put(stock, position);
            }
            position.addTransaction(transaction);
        }
    }
}
