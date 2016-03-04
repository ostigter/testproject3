package org.ozsoft.portfoliomanager.ui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JScrollPane;

import org.ozsoft.portfoliomanager.ui.table.TransactionsTable;

public class TransactionsFrame extends JDialog {

    private static final long serialVersionUID = -4942077142767084610L;

    private final TransactionsTable transactionsTable;

    public TransactionsFrame(MainFrame mainFrame) {
        super(mainFrame, "Edit Transactions", true);

        transactionsTable = new TransactionsTable(mainFrame);
        add(new JScrollPane(transactionsTable), BorderLayout.CENTER);
        transactionsTable.update();

        setSize(800, 600);
        setLocationRelativeTo(mainFrame);
    }
}
