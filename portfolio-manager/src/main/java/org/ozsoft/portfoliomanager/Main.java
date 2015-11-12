package org.ozsoft.portfoliomanager;

import javax.swing.SwingUtilities;

import org.ozsoft.portfoliomanager.ui.MainFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });

        // Configuration config = Configuration.getInstance();
        //
        // // config.addTransaction(new Transaction(1L, TransactionType.BUY, "STWD", 155, 21.75, 1.74));
        // // config.addTransaction(new Transaction(2L, TransactionType.BUY, "STAG", 148, 19.58, 1.16));
        //
        // Portfolio portfolio = new Portfolio();
        // for (Transaction transaction : config.getTransactions()) {
        // portfolio.addTransaction(transaction);
        // }
        // portfolio.update(config);
        // System.out.format("$%,.2f  $%,.2f  $%,.2f\n", portfolio.getCurrentCost(), portfolio.getCurrentValue(),
        // portfolio.getCurrentChange());
        //
        // // Configuration.save();
    }
}
