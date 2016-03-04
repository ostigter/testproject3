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

        // Set<Transaction> transactions = new TreeSet<Transaction>();
        //
        // Transaction tx = new Transaction();
        // tx.setDate(1000L);
        // tx.setSymbol("A");
        // transactions.add(tx);
        //
        // tx = new Transaction();
        // tx.setDate(1010L);
        // tx.setSymbol("A");
        // transactions.add(tx);
        //
        // tx = new Transaction();
        // tx.setDate(1005L);
        // tx.setSymbol("B");
        // transactions.add(tx);
        //
        // for (Transaction t : transactions) {
        // System.out.format("%s %s\n", t.getDate(), t.getSymbol());
        // }
    }
}
