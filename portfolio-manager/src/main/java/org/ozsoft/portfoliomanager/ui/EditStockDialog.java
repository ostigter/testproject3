package org.ozsoft.portfoliomanager.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.ozsoft.portfoliomanager.domain.CreditRating;
import org.ozsoft.portfoliomanager.domain.Stock;

public class EditStockDialog extends Dialog {

    private JTextField nameField;

    private JTextField symbolField;

    private JComboBox<String> creditRatingBox;

    private JTextField commentField;

    private JButton applyButton;

    private JButton cancelButton;

    private Stock stock;

    public EditStockDialog(JFrame parent) {
        super(parent);
    }

    @Override
    protected void initUI() {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Symbol:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 10, 5, 5);
        dialog.add(label, gbc);

        symbolField = new JTextField();
        symbolField.setPreferredSize(new Dimension(60, 20));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 10);
        dialog.add(symbolField, gbc);

        label = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 5, 5);
        dialog.add(label, gbc);

        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 5, 10);
        dialog.add(nameField, gbc);

        label = new JLabel("Credit Rating:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 10, 5, 5);
        dialog.add(label, gbc);

        creditRatingBox = new JComboBox<String>();
        creditRatingBox.setPreferredSize(new Dimension(60, 20));
        for (CreditRating cr : CreditRating.values()) {
            creditRatingBox.addItem(cr.getText());
        }
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 10);
        dialog.add(creditRatingBox, gbc);

        label = new JLabel("Notes:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 10, 5, 5);
        dialog.add(label, gbc);

        commentField = new JTextField();
        commentField.setPreferredSize(new Dimension(300, 20));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 10);
        dialog.add(commentField, gbc);

        applyButton = new JButton("OK");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 10, 5);
        dialog.add(applyButton, gbc);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 10, 10);
        dialog.add(cancelButton, gbc);
    }

    @Override
    public int show() {
        stock = null;

        dialog.setTitle("Add Stock");
        nameField.setText("");
        symbolField.setText("");
        creditRatingBox.setSelectedItem("N/A");
        commentField.setText("");

        nameField.setEditable(true);
        symbolField.setEditable(true);

        commentField.requestFocus();

        return super.show();
    }

    public int show(Stock stock) {
        this.stock = stock;

        dialog.setTitle("Edit Stock");
        nameField.setText(stock.getName());
        symbolField.setText(stock.getSymbol());
        creditRatingBox.setSelectedItem(stock.getCreditRating().getText());
        commentField.setText(stock.getComment());

        symbolField.setEditable(false);

        commentField.requestFocus();

        return super.show();
    }

    public Stock getStock() {
        return stock;
    }

    private void apply() {
        // Validate user input.
        String name = nameField.getText();
        if (name.isEmpty()) {
            showError("Please enter the stock's name.");
            return;
        }
        String symbol = symbolField.getText();
        if (symbol.isEmpty()) {
            showError("Please enter the stock's symbol.");
            return;
        }
        CreditRating creditRating = CreditRating.NA;
        if (creditRatingBox.getSelectedIndex() > -1) {
            creditRating = CreditRating.parse((String) creditRatingBox.getSelectedItem());
        } else {
            showError("Please select the stock's credit rating.");
            return;
        }

        // Add stock.
        if (stock == null) {
            stock = new Stock(symbol, name);
        }
        stock.setName(name);
        stock.setCreditRating(creditRating);
        stock.setComment(commentField.getText());

        ok();
    }
}
