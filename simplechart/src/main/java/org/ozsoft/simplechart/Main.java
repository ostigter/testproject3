package org.ozsoft.simplechart;

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

public class Main {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("d-MMM-yy");

    public static void main(String[] args) throws Exception {
        Chart chart = new Chart();
        chart.setCaption("Johnson & Johnson (JNJ)");
        chart.setXAxisLabel("Date");
        chart.setYAxisLabel("Closing price ($)");
        chart.setDataPoints(readDataPointsFromCsvFile(new File("JNJ_closings.csv")));
        chart.renderImage();
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
}
