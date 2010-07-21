package org.ozsoft.backup;

import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Abstract base class for Swing dialogs.
 * 
 * @author Oscar Stigter
 */
public abstract class Dialog {
    
    protected final JFrame owner;
    
    protected final JDialog dialog;
    
    public Dialog(String title, JFrame owner) {
        this.owner = owner;
        dialog = new JDialog(owner, title, true);
        dialog.setLayout(new GridBagLayout());
        createUI();
    }
    
    public void show() {
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }
    
    public void hide() {
        dialog.setVisible(false);
    }

    protected abstract void createUI();

}
