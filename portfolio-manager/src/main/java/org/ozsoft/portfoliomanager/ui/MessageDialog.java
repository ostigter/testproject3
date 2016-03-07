package org.ozsoft.portfoliomanager.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Message dialog for temporarily showing a single-line message.
 * 
 * @author Oscar Stigter
 */
public class MessageDialog extends JDialog {

    private static final long serialVersionUID = -70419363927208237L;

    private final MainFrame mainFrame;

    private final JLabel messageLabel;

    public MessageDialog(MainFrame mainFrame) {
        super(mainFrame, false);

        this.mainFrame = mainFrame;

        setUndecorated(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new LineBorder(Color.DARK_GRAY));
        messageLabel = new JLabel("", SwingConstants.CENTER);
        panel.add(messageLabel, gbc);
        add(panel, gbc);

        setSize(400, 100);
    }

    public void show(String message) {
        messageLabel.setText(message);
        setLocationRelativeTo(mainFrame);
        setVisible(true);
    }

    public void close() {
        setVisible(false);
    }
}
