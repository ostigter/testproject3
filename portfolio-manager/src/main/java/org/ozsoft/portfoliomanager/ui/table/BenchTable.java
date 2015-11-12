package org.ozsoft.portfoliomanager.ui.table;

import java.util.Set;
import java.util.TreeSet;

import org.ozsoft.portfoliomanager.domain.Stock;
import org.ozsoft.portfoliomanager.domain.StockLevel;
import org.ozsoft.portfoliomanager.ui.MainFrame;

public class BenchTable extends StockTable {

    private static final long serialVersionUID = 1693684402003141159L;

    public BenchTable(MainFrame mainFrame) {
        super(mainFrame);
    }

    @Override
    protected Set<Stock> getStocks() {
        Set<Stock> stocks = new TreeSet<Stock>();
        for (Stock stock : getConfig().getStocks()) {
            if (StockLevel.BENCH == stock.getLevel()) {
                stocks.add(stock);
            }
        }
        return stocks;
    }
}
