package org.ozsoft.charts;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Main {

    public static void main(String[] args) {
        TimeSeries series1 = new TimeSeries("Series 1", Month.class);
        series1.add(new Month(7, 2008), 32.0);
        series1.add(new Month(8, 2008), 44.0);
        series1.add(new Month(9, 2008), 65.0);
        series1.add(new Month(10, 2008), 57.0);
        series1.add(new Month(11, 2008), 46.0);
        series1.add(new Month(12, 2008), 38.0);
        series1.add(new Month(1, 2009), 20.0);
        series1.add(new Month(2, 2009), 30.0);
        series1.add(new Month(3, 2009), 25.0);
        series1.add(new Month(4, 2009), 32.0);
        series1.add(new Month(5, 2009), 56.5);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series1);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                null, null, null, dataset, false, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();
        double maxValue = rangeAxis.getRange().getUpperBound();
        maxValue = Math.ceil(maxValue / 10.0) * 10.0;
        rangeAxis.setRange(0.0, maxValue);
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy", Locale.US));
        ChartPanel chartPanel = new ChartPanel(chart);

        JFrame frame = new JFrame("JFreeChart test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}
