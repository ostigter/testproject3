package org.example.slider;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("JSlider test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JSlider slider = new JSlider(0, 100);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) {
                    System.out.println("Value: " + slider.getValue());
                }
            }
        });
        frame.getContentPane().add(slider);
        
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
