package org.ozsoft.portfoliomanager.ui.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
import org.ozsoft.portfoliomanager.domain.Transaction;
import org.ozsoft.portfoliomanager.domain.TransactionType;
import org.ozsoft.portfoliomanager.ui.Dialog;
import org.ozsoft.portfoliomanager.ui.EditTransactionDialog;
import org.ozsoft.portfoliomanager.ui.MainFrame;
import org.ozsoft.portfoliomanager.ui.table.column.DateColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.MoneyColumnRenderer;
import org.ozsoft.portfoliomanager.ui.table.column.SharesColumnRenderer;

public class TransactionsTable extends DataTable {

    private static final long serialVersionUID = -6959086848794121532L;

    private final Configuration config = Configuration.getInstance();

    protected final MainFrame mainFrame;

    private final EditTransactionDialog editTransactionDialog;

    public TransactionsTable(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        editTransactionDialog = new EditTransactionDialog(mainFrame);

        initColumns();

        initContextMenu();

        // Double-click transaction to edit it
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editTransaction();
                }
            }
        });
    }

    protected void initColumns() {
        ColumnRenderer centerColumnRenderer = new DefaultColumnRenderer(SwingConstants.CENTER);
        ColumnRenderer dateColumnRenderer = new DateColumnRenderer();
        ColumnRenderer sharesColumnRenderer = new SharesColumnRenderer();
        ColumnRenderer smallMoneyColumnRenderer = new MoneyColumnRenderer(2);

        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("Date", "Transaction date", dateColumnRenderer));
        columns.add(new Column("Stock name", "Stock name"));
        columns.add(new Column("Symbol", "Stock ticker sybol", centerColumnRenderer));
        columns.add(new Column("Type", "Transaction type", centerColumnRenderer));
        columns.add(new Column("Shares", "Number of shares", sharesColumnRenderer));
        columns.add(new Column("Price", "Price per share", smallMoneyColumnRenderer));
        columns.add(new Column("Costs", "Transaction costs, incl. broker fees, valuta costs, etc.", smallMoneyColumnRenderer));
        columns.add(new Column("Total", "Total transaction value", smallMoneyColumnRenderer));
        setColumns(columns);
    }

    protected void initContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem menuItem = new JMenuItem("Add transaction...");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });
        contextMenu.add(menuItem);

        menuItem = new JMenuItem("Edit transaction...");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTransaction();
            }
        });
        contextMenu.add(menuItem);

        menuItem = new JMenuItem("Delete transaction");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTransaction();
            }
        });
        contextMenu.add(menuItem);

        setComponentPopupMenu(contextMenu);
    }

    @Override
    public final void update() {
        clear();
        for (Transaction t : config.getTransactions()) {
            String symbol = t.getSymbol();
            Stock stock = config.getStock(symbol);
            String stockName = (stock != null) ? stock.getName() : "<ERROR: Stock deleted>";
            TransactionType type = t.getType();
            int noOfShares = (type == TransactionType.SELL) ? -1 * t.getNoOfShares() : t.getNoOfShares();
            double price = t.getPrice();
            double costs = t.getCost();
            double total = noOfShares * price + costs;
            addRow(t.getDate(), stockName, symbol, type, noOfShares, price, costs, total);
        }
        super.update();
    }

    private void addTransaction() {
        if (editTransactionDialog.show() == Dialog.OK) {
            config.addTransaction(editTransactionDialog.getTransaction());
            update();
        }
    }

    private void editTransaction() {
        Transaction transaction = getSelectedTransaction();
        if (transaction != null) {
            if (editTransactionDialog.show(transaction) == Dialog.OK) {
                update();
                mainFrame.updateTables();
            }
        }
    }

    private void deleteTransaction() {
        Transaction transaction = getSelectedTransaction();
        if (transaction != null) {
            if (JOptionPane.showConfirmDialog(null, "Permanently delete transaction?", "Warning", JOptionPane.WARNING_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                config.deleteTransaction(transaction);
                update();
            }
        }
    }

    private Transaction getSelectedTransaction() {
        Transaction transaction = null;
        int rowIndex = getSelectedRow();
        if (rowIndex >= 0) {
            int i = 0;
            for (Transaction tx : config.getTransactions()) {
                if (i == rowIndex) {
                    transaction = tx;
                    break;
                }
                i++;
            }
        }
        return transaction;
    }
}
