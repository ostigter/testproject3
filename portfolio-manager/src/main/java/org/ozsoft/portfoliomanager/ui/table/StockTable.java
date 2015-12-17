package org.ozsoft.portfoliomanager.ui.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import org.ozsoft.datatable.Column;
import org.ozsoft.datatable.ColumnRenderer;
import org.ozsoft.datatable.DataTable;
import org.ozsoft.datatable.DefaultColumnRenderer;
import org.ozsoft.portfoliomanager.domain.Configuration;
import org.ozsoft.portfoliomanager.domain.Stock;
import org.ozsoft.portfoliomanager.domain.StockLevel;
import org.ozsoft.portfoliomanager.ui.Dialog;
import org.ozsoft.portfoliomanager.ui.EditStockDialog;
import org.ozsoft.portfoliomanager.ui.MainFrame;
import org.ozsoft.portfoliomanager.ui.StockPriceFrame;
import org.ozsoft.portfoliomanager.ui.table.column.CRColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.DGColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.DRColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.MoneyColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.PEColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.PercChangeColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.YDGColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.YieldColumnRenderer;

public class StockTable extends DataTable {

    private static final long serialVersionUID = -1991423181259068250L;

    private final Configuration config = Configuration.getInstance();

    protected final MainFrame mainFrame;

    protected final EditStockDialog editStockDialog;

    public StockTable(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        initColumns();

        initContextMenu();

        // Double-click stock to edit it
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewStockPrice();
                }
            }
        });

        editStockDialog = new EditStockDialog(mainFrame);
    }

    protected void initColumns() {
        ColumnRenderer defaultColumnRenderer = new DefaultColumnRenderer();
        ColumnRenderer centeredColumnRenderer = new DefaultColumnRenderer(SwingConstants.CENTER);
        ColumnRenderer smallMoneyColumnRenderer = new MoneyColumnRenderer(2);
        ColumnRenderer percChangeColumnRenderer = new PercChangeColumnRenderer();
        ColumnRenderer peRatioColumnRenderer = new PEColumnRenderer();
        ColumnRenderer divRateColumnRenderer = new DRColumnRenderer();
        ColumnRenderer yieldColumnRenderer = new YieldColumnRenderer();
        ColumnRenderer divGrowthColumnRenderer = new DGColumnRenderer();
        ColumnRenderer yearsColumnRenderer = new YDGColumnRenderer();
        ColumnRenderer ratingColumnRenderer = new CRColumnRenderer();

        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("Name", "Company name", defaultColumnRenderer));
        columns.add(new Column("Symbol", "Ticker symbol", centeredColumnRenderer));
        columns.add(new Column("Price", "Current stock price", smallMoneyColumnRenderer));
        columns.add(new Column("Change", "Change in stock price since last closing", percChangeColumnRenderer));
        columns.add(new Column("P/E", "Current price-to-earnings ratio", peRatioColumnRenderer));
        columns.add(new Column("DR", "Current dividend rate", divRateColumnRenderer));
        columns.add(new Column("Yield", "Current dividend yield", yieldColumnRenderer));
        columns.add(new Column("DG", "5-year annualized dividend growth", divGrowthColumnRenderer));
        columns.add(new Column("YDG", "Consecutive years of dividend growth", yearsColumnRenderer));
        columns.add(new Column("CR", "Current credit rating", ratingColumnRenderer));
        columns.add(new Column("Notes", "Notes about this stock"));

        setColumns(columns);
    }

    protected void initContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem menuItem = new JMenuItem("View stock price...");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStockPrice();
            }
        });
        contextMenu.add(menuItem);

        contextMenu.addSeparator();

        menuItem = new JMenuItem("Move to goal portfolio");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveStockToGoalPortfolio();
            }
        });
        contextMenu.add(menuItem);

        menuItem = new JMenuItem("Move to watch list");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveStockToWatchList();
            }
        });
        contextMenu.add(menuItem);

        menuItem = new JMenuItem("Move to bench");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveStockToBench();
            }
        });
        contextMenu.add(menuItem);

        contextMenu.addSeparator();

        menuItem = new JMenuItem("Edit Stock...");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStock();
            }
        });
        contextMenu.add(menuItem);

        menuItem = new JMenuItem("Delete Stock");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStock();
            }
        });
        contextMenu.add(menuItem);

        setComponentPopupMenu(contextMenu);
    }

    @Override
    public final void update() {
        clear();
        for (Stock s : getStocks()) {
            addRow(s.getName(), s.getSymbol(), s.getPrice(), s.getChangePerc(), s.getPeRatio(), s.getDivRate(), s.getYield(), s.getDivGrowth(),
                    s.getYearsDivGrowth(), s.getCreditRating(), s.getComment());
        }
        super.update();
    }

    protected final Configuration getConfig() {
        return config;
    }

    protected Set<Stock> getStocks() {
        return getConfig().getStocks();
    }

    private void viewStockPrice() {
        Stock stock = getSelectedStock();
        if (stock != null) {
            StockPriceFrame.show(stock);
        }
    }

    private void moveStockToGoalPortfolio() {
        Stock stock = getSelectedStock();
        if (stock != null) {
            if (stock.getLevel() != StockLevel.GOAL) {
                stock.setLevel(StockLevel.GOAL);
                mainFrame.updateTables();
            }
        }
    }

    private void moveStockToWatchList() {
        Stock stock = getSelectedStock();
        if (stock != null) {
            if (stock.getLevel() != StockLevel.WATCH) {
                stock.setLevel(StockLevel.WATCH);
                mainFrame.updateTables();
            }
        }
    }

    private void moveStockToBench() {
        Stock stock = getSelectedStock();
        if (stock != null) {
            if (stock.getLevel() != StockLevel.BENCH) {
                stock.setLevel(StockLevel.BENCH);
                mainFrame.updateTables();
            }
        }
    }

    private void editStock() {
        Stock stock = getSelectedStock();
        if (stock != null) {
            if (editStockDialog.show(stock) == Dialog.OK) {
                update();
            }
        }
    }

    private void deleteStock() {
        Stock stock = getSelectedStock();
        if (stock != null) {
            String msg = String.format("Permanently delete stock with symbol '%s'?", stock.getSymbol());
            if (JOptionPane.showConfirmDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                config.deleteStock(stock);
                update();
            }
        }
    }

    private Stock getSelectedStock() {
        Stock stock = null;

        int rowIndex = getSelectedRow();
        if (rowIndex >= 0) {
            String symbol = (String) getCellValue(rowIndex, 1);
            stock = config.getStock(symbol);
        }

        return stock;
    }
}
