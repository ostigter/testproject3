package org.ozsoft.portfoliomanager.ui.table;

import java.util.Set;
import java.util.TreeSet;

import org.ozsoft.portfoliomanager.domain.Stock;
import org.ozsoft.portfoliomanager.domain.StockLevel;
import org.ozsoft.portfoliomanager.ui.MainFrame;

public class GoalTable extends StockTable {

    private static final long serialVersionUID = 1406716019847307872L;

    public GoalTable(MainFrame mainFrame) {
        super(mainFrame);
    }

    @Override
    protected Set<Stock> getStocks() {
        Set<Stock> stocks = new TreeSet<Stock>();
        for (Stock stock : getConfig().getStocks()) {
            StockLevel level = stock.getLevel();
            if (level == StockLevel.GOAL || level == StockLevel.OWNED) {
                stocks.add(stock);
            }
        }
        return stocks;
    }
}
