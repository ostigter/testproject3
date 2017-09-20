package org.ozsoft.simplechart;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yy", Locale.US);

    public static void main(String[] args) throws Exception {
        Chart chart = new Chart();
//        chart.setCaption("Johnson & Johnson (JNJ)");
//        chart.setXAxisLabel("Date");
//        chart.setYAxisLabel("Closing price ($)");
//      chart.setDataPoints(getDataPoints());
        chart.setDataPoints(readDataPointsFromCsvFile(new File("JNJ_closings.csv")));
        BufferedImage image = chart.renderImage();

        JFrame frame = new JFrame("Simple chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static List<DataPoint> readDataPointsFromCsvFile(File file) throws IOException {
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = null;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    String[] fields = line.split(",");
                    Date date = null;
                    try {
                        date = DATE_FORMAT.parse(fields[0]);
                        BigDecimal price = new BigDecimal(fields[4]);
                        dataPoints.add(new DataPoint(date, price));
                    } catch (ParseException e) {
                        System.err.println("ERROR: Invalid date: " + fields[0]);
                    }
                }
            }
        }

        return dataPoints;
    }

//    private static List<DataPoint> getDataPoints() {
//        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
//        dataPoints.add(createDataPoint("01-Jan-1970", "101.00"));
//        dataPoints.add(createDataPoint("02-Jan-1970", "102.00"));
//        dataPoints.add(createDataPoint("03-Jan-1970", "103.00"));
//        dataPoints.add(createDataPoint("04-Jan-1970", "104.00"));
//        dataPoints.add(createDataPoint("05-Jan-1970", "105.00"));
//        dataPoints.add(createDataPoint("06-Jan-1970", "106.00"));
//        dataPoints.add(createDataPoint("07-Jan-1970", "107.00"));
//        dataPoints.add(createDataPoint("08-Jan-1970", "108.00"));
//        dataPoints.add(createDataPoint("09-Jan-1970", "109.00"));
//        dataPoints.add(createDataPoint("10-Jan-1970", "110.00"));
//        return dataPoints;
//    }
//
//    private static DataPoint createDataPoint(String date, String value) {
//        try {
//            return new DataPoint(DATE_FORMAT.parse(date), new BigDecimal(value));
//        } catch (ParseException e) {
//            throw new IllegalArgumentException("ERROR: Invalid date: " + date);
//        }
//    }
}
