package org.jfree.chart.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * This demo shows two charts that use weekly data. A custom tick unit collection is defined to control the domain axis formatting.
 *
 */
public class TimeSeriesDemo13 extends ApplicationFrame {

    private static final long serialVersionUID = 6882215232837350695L;

    /**
     * Creates a new demo instance.
     *
     * @param title
     *            the frame title.
     */
    public TimeSeriesDemo13(final String title) {
        super(title);

        final XYDataset dataset1 = createDataset(20);
        final JFreeChart chart1 = createChart(dataset1);
        final ChartPanel chartPanel = new ChartPanel(chart1);

        final JPanel content = new JPanel(new BorderLayout());
        content.setPreferredSize(new java.awt.Dimension(800, 600));
        content.add(chartPanel);
        setContentPane(content);
    }

    /**
     * Creates a chart.
     *
     * @param dataset
     *            a dataset.
     *
     * @return A chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart("Weekly Data", "Date", "Value", dataset, true, true, false);

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        final TickUnits standardUnits = new TickUnits();
        standardUnits.add(new DateTickUnit(DateTickUnit.DAY, 1, new SimpleDateFormat("MMM dd ''yy")));
//        standardUnits.add(new DateTickUnit(DateTickUnit.MONTH, 3, new SimpleDateFormat("MMM ''yy")));
//        standardUnits.add(new DateTickUnit(DateTickUnit.YEAR, 1, new SimpleDateFormat("''yy")));
        axis.setStandardTickUnits(standardUnits);

        return chart;

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    *
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************

    /**
     * Creates a dataset containing random values at weekly intervals.
     *
     * @param items
     *            the number of items in the dataset.
     *
     * @return the dataset.
     */
    private XYDataset createDataset(final int items) {

        final TimeSeries timeSeries = new TimeSeries("Random Data");
        RegularTimePeriod timePeriod = new Day();
        double value = 100.0;
        for (int i = 0; i < items; i++) {
            timeSeries.add(timePeriod, value);
            value = value * (1.0 + ((Math.random() - 0.499) / 100.0));
            timePeriod = timePeriod.next();
        }

        return new TimeSeriesCollection(timeSeries);

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args
     *            ignored.
     */
    public static void main(final String[] args) {
        final TimeSeriesDemo13 demo = new TimeSeriesDemo13("Time Series Demo 13");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
}
