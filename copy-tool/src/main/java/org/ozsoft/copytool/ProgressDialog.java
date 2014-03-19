package org.ozsoft.copytool;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog {

    private static final long serialVersionUID = 8767732567015504941L;

    private final JProgressBar progressBar;

    public ProgressDialog(JFrame parent) {
        super(parent, "Copy Tool", false);

        progressBar = new JProgressBar();
        add(progressBar);

        pack();
        setLocationRelativeTo(parent);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }
}
