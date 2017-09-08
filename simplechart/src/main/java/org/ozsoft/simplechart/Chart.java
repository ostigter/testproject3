package org.ozsoft.simplechart;

import java.awt.Image;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Chart {

    private static final int WIDTH = 600;

    private static final int HEIGHT = 400;

    private String caption;

    private String xAxisLabel;

    private String yAxisLabel;

    private final List<DataPoint> dataPoints;

    public Chart() {
        dataPoints = new ArrayList<DataPoint>();
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setXAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public void setYAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public void clearDataPoints() {
        dataPoints.clear();
    }

    public void addDataPoint(DataPoint dataPoint) {
        dataPoints.add(dataPoint);
    }

    public void addDataPoint(Date date, BigDecimal value) {
        dataPoints.add(new DataPoint(date, value));
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        clearDataPoints();
        this.dataPoints.addAll(dataPoints);
    }

    public Image renderImage() {
        int pointCount = dataPoints.size();
        if (pointCount < 2) {
            throw new IllegalStateException("Not enough data points to render graph (need at least 2)");
        }

        Collections.sort(dataPoints);

        Date oldestDate = dataPoints.get(0).getDate();
        Date newestDate = dataPoints.get(pointCount - 1).getDate();
        long timeDiff = newestDate.getTime() - oldestDate.getTime();
        System.out.format("Time diff: %,d ms\n", timeDiff);
        TimeScale timeScale = TimeScale.parse(timeDiff);
        int timeWidthInUnits = (int) Math.round(timeDiff / (double) timeScale.getMilliseconds());
        System.out.format("%d %s\n", timeWidthInUnits, timeScale);

        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        for (DataPoint dp : dataPoints) {
            double value = dp.getValue().doubleValue();
            if (value < minValue) {
                minValue = value;
            }
            if (value > maxValue) {
                maxValue = value;
            }
        }
        double range = maxValue - minValue;
        double unroundedTickSize = range / 10.0;
        double pow10x = Math.pow(10, Math.ceil(Math.log10(unroundedTickSize) - 1.0));
        double tickSize = Math.ceil(unroundedTickSize / pow10x) * pow10x;
        double lowerBound = tickSize * Math.round(minValue / tickSize);
        double upperBound = tickSize * Math.round(1.0 + maxValue / tickSize);

        System.out.println("min        = " + minValue);
        System.out.println("max        = " + maxValue);
        System.out.format("range      = %,.2f\n", range);
        System.out.format("tickSize   = %.2f\n", tickSize);
        System.out.format("lowerBound = %.2f\n", lowerBound);
        System.out.format("upperBound = %.2f\n", upperBound);

        return null;
    }
}
