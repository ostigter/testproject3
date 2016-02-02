package org.ozsoft.portfoliomanager.ui;

import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Abstract base class for Swing dialogs.
 * 
 * @author Oscar Stigter
 */
public abstract class Dialog {

    public static final int OK = 0;

    public static final int CANCEL = 1;

    private final JFrame owner;

    protected final JDialog dialog;

    private int result;

    public Dialog(JFrame owner) {
        this.owner = owner;

        dialog = new JDialog(owner, true);
        dialog.setResizable(false);
        dialog.setLayout(new GridBagLayout());

        initUI();
    }

    public int show() {
        result = CANCEL;
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);

        // Block until user has clicked a button.

        return result;
    }

    protected abstract void initUI();

    protected final void ok() {
        close(OK);
    }

    protected final void cancel() {
        close(CANCEL);
    }

    private void close(int result) {
        this.result = result;
        dialog.setVisible(false);
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(owner, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
