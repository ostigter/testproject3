package org.example.slider;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {
    
    private static final int NO_OF_TICKS = 10;

    public static void main(String[] args) {
        JFrame frame = new JFrame("JSlider test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        final int minBet = 5;
        final int cash = 9876;
        final HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
        int noOfValues = 0;
        int value = minBet;
        while (value < cash && noOfValues < (NO_OF_TICKS - 1)) {
            values.put(noOfValues, value);
            noOfValues++;
            value *= 2;
        }
        values.put(noOfValues, cash);
        
        final JSlider slider = new JSlider(0, noOfValues);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 0, 10);
        frame.getContentPane().add(slider, gbc);

        final JLabel valueLabel = new JLabel(" ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 10);
        frame.getContentPane().add(valueLabel, gbc);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = slider.getValue();
                int amount = values.get(index);
//                System.out.format("%d = $%d\n", index, amount);
                valueLabel.setText(String.format("Bet amount: $%d", amount));
            }
        });

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        slider.setValue(0);
    }

}
