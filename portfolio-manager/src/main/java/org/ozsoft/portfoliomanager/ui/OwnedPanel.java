package org.ozsoft.portfoliomanager.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.ozsoft.portfoliomanager.domain.Configuration;
import org.ozsoft.portfoliomanager.services.UpdateService;
import org.ozsoft.portfoliomanager.ui.table.OwnedTable;

/**
 * Panel with the portfolio of owned stocks and transactions.
 * 
 * @author Oscar Stigter.
 */
public class OwnedPanel extends JPanel {

    private static final long serialVersionUID = -6819380411937781494L;

    private final MainFrame mainFrame;

    private final OwnedTable ownedTable;

    private final TransactionsFrame editTransactionsFrame;

    private final Configuration config = Configuration.getInstance();

    private final UpdateService updateService = new UpdateService();

    /**
     * Constructor.
     * 
     * @param mainFrame
     *            The application's main window.
     */
    public OwnedPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        editTransactionsFrame = new TransactionsFrame(mainFrame);

        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton button = new JButton("Update");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStockPrices();
            }
        });
        buttonPanel.add(button);

        button = new JButton("Edit Transactions");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTransactionsFrame.setVisible(true);
            }
        });
        buttonPanel.add(button);

        add(buttonPanel, BorderLayout.NORTH);

        ownedTable = new OwnedTable(mainFrame);
        add(new JScrollPane(ownedTable), BorderLayout.CENTER);
    }

    /**
     * Updates the panel.
     */
    public void update() {
        ownedTable.update();
    }

    /**
     * Updates the prices of the owned stocks.
     */
    private void updateStockPrices() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateService.updatePrices(config.getOwnedStocks());
                mainFrame.updateTables();
            }
        });
    }
}
