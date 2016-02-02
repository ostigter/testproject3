package org.ozsoft.portfoliomanager.ui.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import org.ozsoft.datatable.Column;
import org.ozsoft.datatable.ColumnRenderer;
import org.ozsoft.datatable.DataTable;
import org.ozsoft.datatable.DefaultColumnRenderer;
import org.ozsoft.portfoliomanager.domain.Configuration;
import org.ozsoft.portfoliomanager.domain.Portfolio;
import org.ozsoft.portfoliomanager.domain.Position;
import org.ozsoft.portfoliomanager.domain.Stock;
import org.ozsoft.portfoliomanager.domain.Transaction;
import org.ozsoft.portfoliomanager.ui.Dialog;
import org.ozsoft.portfoliomanager.ui.EditStockDialog;
import org.ozsoft.portfoliomanager.ui.MainFrame;
import org.ozsoft.portfoliomanager.ui.StockPriceFrame;
import org.ozsoft.portfoliomanager.ui.table.column.CRColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.DGColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.DRColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.MoneyColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.PercChangeColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.PercentageColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.ResultColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.YDGColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.YieldColumnRenderer;

public class OwnedTable extends DataTable {

    private static final long serialVersionUID = -7051733783546691662L;

    private final Configuration config = Configuration.getInstance();

    protected final MainFrame mainFrame;

    protected final EditStockDialog editStockDialog;

    public OwnedTable(MainFrame mainFrame) {
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
        ColumnRenderer numberColumnRenderer = new DefaultColumnRenderer(SwingConstants.RIGHT);
        ColumnRenderer centeredColumnRenderer = new DefaultColumnRenderer(SwingConstants.CENTER);
        ColumnRenderer percChangeColumnRenderer = new PercChangeColumnRenderer();
        ColumnRenderer divRateColumnRenderer = new DRColumnRenderer();
        ColumnRenderer yieldColumnRenderer = new YieldColumnRenderer();
        ColumnRenderer divGrowthColumnRenderer = new DGColumnRenderer();
        ColumnRenderer yearsColumnRenderer = new YDGColumnRenderer();
        ColumnRenderer ratingColumnRenderer = new CRColumnRenderer();
        ColumnRenderer smallMoneyColumnRenderer = new MoneyColumnRenderer(2);
        ColumnRenderer bigMoneyColumnRenderer = new MoneyColumnRenderer(0);
        ColumnRenderer resultColumnRenderer = new ResultColumnRenderer(0);
        ColumnRenderer percColumnRenderer = new PercentageColumnRenderer();

        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("Symbol", "Ticker symbol", centeredColumnRenderer));
        columns.add(new Column("Price", "Current stock price", smallMoneyColumnRenderer));
        columns.add(new Column("Change", "Change in stock price since last closing", percChangeColumnRenderer));
        columns.add(new Column("DR", "Current dividend rate", divRateColumnRenderer));
        columns.add(new Column("CY", "Current dividend yield", yieldColumnRenderer));
        columns.add(new Column("DGR", "5-year annualized dividend growth rate", divGrowthColumnRenderer));
        columns.add(new Column("YDG", "Consecutive years of dividend growth", yearsColumnRenderer));
        columns.add(new Column("CR", "Current credit rating", ratingColumnRenderer));
        columns.add(new Column("Shares", "Current number of shares owned", numberColumnRenderer));
        columns.add(new Column("Cost", "Current cost basis", bigMoneyColumnRenderer));
        columns.add(new Column("CPS", "Current cost basis per share", smallMoneyColumnRenderer));
        columns.add(new Column("Value", "Current market value", bigMoneyColumnRenderer));
        columns.add(new Column("Weight", "Relative weight in portfolio based on cost", percColumnRenderer));
        columns.add(new Column("Result", "Current result (value minus cost)", resultColumnRenderer));
        columns.add(new Column("Result %", "Current result as percentage of cost", percChangeColumnRenderer));
        columns.add(new Column("AI", "Annual income based on current yield", bigMoneyColumnRenderer));
        columns.add(new Column("YOC", "Annual yield on cost", percColumnRenderer));
        columns.add(new Column("TI", "Overall total income received", resultColumnRenderer));
        columns.add(new Column("RR", "Total realized result from sales", resultColumnRenderer));
        columns.add(new Column("TR", "Total return (result plus income)", resultColumnRenderer));
        columns.add(new Column("TR %", "Total return as percentage of cost", percChangeColumnRenderer));
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

        menuItem = new JMenuItem("Edit Stock...");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStock();
            }
        });
        contextMenu.add(menuItem);

        setComponentPopupMenu(contextMenu);
    }

    @Override
    public final void update() {
        clear();

        // Build portfolio from transactions.
        Configuration config = Configuration.getInstance();
        Portfolio portfolio = new Portfolio();
        for (Transaction transaction : config.getTransactions()) {
            portfolio.addTransaction(transaction);
        }
        portfolio.update(config);

        // Populate table with positions.
        for (Position p : portfolio.getPositions()) {
            Stock s = p.getStock();
            double weight = (p.getCurrentCost() / portfolio.getCurrentCost()) * 100.0;
            addRow(s.getSymbol(), s.getPrice(), s.getChangePerc(), s.getDivRate(), s.getYield(), s.getDivGrowth(), s.getYearsDivGrowth(),
                    s.getCreditRating(), p.getNoOfShares(), p.getCurrentCost(), p.getCostPerShare(), p.getCurrentValue(), weight,
                    p.getCurrentResult(), p.getCurrentResultPercentage(), p.getAnnualIncome(), p.getYieldOnCost(), p.getTotalIncome(),
                    p.getRealizedResult(), p.getTotalReturn(), p.getTotalReturnPercentage(), s.getComment());
        }

        setFooterRow(null, null, null, null, null, null, null, null, null, portfolio.getCurrentCost(), null, portfolio.getCurrentValue(), null,
                portfolio.getCurrentResult(), portfolio.getCurrentResultPercentage(), portfolio.getAnnualIncome(), portfolio.getYieldOnCost(),
                portfolio.getTotalIncome(), portfolio.getRealizedResult(), portfolio.getTotalReturn(), portfolio.getTotalReturnPercentage(), null);

        super.update();
    }

    private void viewStockPrice() {
        Stock stock = getSelectedStock();
        if (stock != null) {
            StockPriceFrame.show(stock);
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

    private Stock getSelectedStock() {
        Stock stock = null;

        int rowIndex = getSelectedRow();
        if (rowIndex >= 0) {
            String symbol = (String) getCellValue(rowIndex, 0);
            stock = config.getStock(symbol);
        }

        return stock;
    }
}