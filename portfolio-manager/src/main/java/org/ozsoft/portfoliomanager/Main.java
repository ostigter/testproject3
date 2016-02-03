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
    }
}
