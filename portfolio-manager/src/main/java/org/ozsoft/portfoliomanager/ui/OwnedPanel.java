package org.ozsoft.portfoliomanager.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.ozsoft.datatable.DataTable;
import org.ozsoft.portfoliomanager.ui.table.OwnedTable;

public class OwnedPanel extends JPanel {

    private static final long serialVersionUID = -6819380411937781494L;

    private final JButton transactionsButton;

    // private final JButton incomeButton;

    private final DataTable ownedTable;

    private final TransactionsFrame transactionsFrame;

    public OwnedPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        transactionsFrame = new TransactionsFrame(mainFrame);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        transactionsButton = new JButton("Transactions");
        transactionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transactionsFrame.setVisible(true);
            }
        });
        buttonPanel.add(transactionsButton);

        // incomeButton = new JButton("Income");
        // buttonPanel.add(incomeButton);

        add(buttonPanel, BorderLayout.NORTH);

        ownedTable = new OwnedTable(mainFrame);
        add(new JScrollPane(ownedTable), BorderLayout.CENTER);
    }

    public void update() {
        ownedTable.update();
    }
}
