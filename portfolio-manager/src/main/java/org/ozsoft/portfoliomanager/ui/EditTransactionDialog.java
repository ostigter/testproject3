package org.ozsoft.portfoliomanager.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;
import org.ozsoft.portfoliomanager.domain.Transaction;
import org.ozsoft.portfoliomanager.domain.TransactionType;

public class EditTransactionDialog extends Dialog {

    private JDatePicker datePicker;

    private JComboBox<TransactionType> typeComboBox;

    private JTextField sharesText;

    private JTextField priceText;

    private JTextField costsText;

    private JButton okButton;

    private JButton cancelButton;

    private Transaction transaction;

    public EditTransactionDialog(JFrame owner) {
        super(owner);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    protected void initUI() {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Date:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 0, 5);
        dialog.add(label, gbc);

        datePicker = new JDateComponentFactory().createJDatePicker();
        ((JComponent) datePicker).setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 0, 5);
        dialog.add((JComponent) datePicker, gbc);

        label = new JLabel("Type:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 10, 0, 5);
        dialog.add(label, gbc);

        typeComboBox = new JComboBox<TransactionType>();
        typeComboBox.addItem(TransactionType.BUY);
        typeComboBox.addItem(TransactionType.SELL);
        typeComboBox.addItem(TransactionType.DIVIDEND);
        typeComboBox.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 0, 10);
        dialog.add(typeComboBox, gbc);

        label = new JLabel("Shares:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 10, 0, 5);
        dialog.add(label, gbc);

        sharesText = new JTextField();
        sharesText.setPreferredSize(new Dimension(60, 20));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 0, 10);
        dialog.add(sharesText, gbc);

        label = new JLabel("Price:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 10, 0, 5);
        dialog.add(label, gbc);

        priceText = new JTextField();
        priceText.setPreferredSize(new Dimension(60, 20));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 0, 10);
        dialog.add(priceText, gbc);

        label = new JLabel("Costs:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 10, 0, 5);
        dialog.add(label, gbc);

        costsText = new JTextField();
        costsText.setPreferredSize(new Dimension(60, 20));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 0, 10);
        dialog.add(costsText, gbc);

        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(75, 20));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 10, 5, 10);
        dialog.add(okButton, gbc);

        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 20));
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
        gbc.insets = new Insets(5, 5, 10, 10);
        dialog.add(cancelButton, gbc);
    }

    @Override
    public int show() {
        return show(null);
    }

    public int show(Transaction transaction) {
        this.transaction = transaction;

        if (transaction == null) {
            dialog.setTitle("Add Transaction");
            datePicker.getModel().setValue(null);
            typeComboBox.setSelectedItem(TransactionType.DIVIDEND);
            sharesText.setText("");
            priceText.setText("");
            costsText.setText("");
        } else {
            dialog.setTitle("Edit Transaction");
            // datePicker.getModel().setValue(transaction.getDate());
            typeComboBox.setSelectedItem(transaction.getType());
            sharesText.setText(String.valueOf(transaction.getNoOfShares()));
            priceText.setText(String.format("%.2f", transaction.getPrice()));
            costsText.setText(String.format("%.2f", transaction.getCost()));
        }

        okButton.requestFocus();

        return super.show();
    }

    private void apply() {
        ok();
    }
}
