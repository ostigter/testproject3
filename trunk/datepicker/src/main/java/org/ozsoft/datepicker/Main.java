package org.ozsoft.datepicker;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFrame;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

public class Main {
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        JFrame frame = new JFrame("DatePicker test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
        final JDatePicker datePicker = JDateComponentFactory.createJDatePicker();
        datePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object value = datePicker.getModel().getValue();
                if (value != null) {
                    Date date = ((Calendar) value).getTime();
                    System.out.println("Date selected: " + DATE_FORMAT.format(date));
                } else {
                    System.out.println("Date cleared");
                }
            }
        });
        frame.getContentPane().add((JComponent) datePicker);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
