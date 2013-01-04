package org.ozsoft.vault;

import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

public abstract class Dialog extends JDialog {

    private static final long serialVersionUID = -9098494375517981159L;

    private boolean okSelected = false;

    public Dialog(JFrame parent, String title) {
        super(parent, title, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                int keyCode = e.getKeyCode();
//                System.out.println("key: " + keyCode);
//                if (keyCode == KeyEvent.VK_ENTER) {
//                    ok();
//                } else if (keyCode == KeyEvent.VK_ESCAPE) {
//                    cancel();
//                }
//            }
//        });
        initUI();
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public final boolean showDialog() {
        setVisible(true);
        return okSelected;
    }

    protected abstract void initUI();

    protected abstract boolean validationOK();

    protected final void ok() {
        if (validationOK()) {
            okSelected = true;
            dispose();
        }
    }

    protected final void cancel() {
        dispose();
    }

}
