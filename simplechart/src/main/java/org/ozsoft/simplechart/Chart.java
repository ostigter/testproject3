package org.ozsoft.simplechart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Chart {

    private static final int WIDTH = 800;

    private static final int HEIGHT = 600;

    private static final int MARGIN = 50;

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

    public BufferedImage renderImage() {
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
        int tickCount = (int) Math.floor((upperBound - lowerBound) / tickSize);

        System.out.println("min        = " + minValue);
        System.out.println("max        = " + maxValue);
        System.out.format("range      = %,.2f\n", range);
        System.out.format("tickSize   = %.2f\n", tickSize);
        System.out.format("lowerBound = %.2f\n", lowerBound);
        System.out.format("upperBound = %.2f\n", upperBound);
        System.out.println("tickCount = " + tickCount);

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setStroke(new BasicStroke(1f));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));

        // Draw outer box (axis).
        g.setColor(Color.GRAY);
        g.drawRect(MARGIN, MARGIN, WIDTH - 2 * MARGIN, HEIGHT - 2 * MARGIN);

        // Draw time tick lines and labels (X axis).
        int timeTickSize = (WIDTH - 2 * MARGIN) / timeWidthInUnits;
        int textOffset = (int) Math.round(timeTickSize / 1.5);
        System.out.println(timeTickSize);
        for (int i = 1; i <= timeWidthInUnits; i++) {
            int x = MARGIN + i * timeTickSize;
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x, MARGIN + 1, x, HEIGHT - MARGIN - 1);
            g.setColor(Color.DARK_GRAY);
            String text = String.format("%02d", i);
            g.drawString(text, x - textOffset, HEIGHT - MARGIN + 12);
        }

        // Draw price tick lines and labels (Y axis).
        int valueTickSize = (HEIGHT - 2 * MARGIN) / tickCount;
        for (int i = 0; i <= tickCount; i++) {
            g.setColor(Color.LIGHT_GRAY);
            int y = HEIGHT - MARGIN + 1 - i * valueTickSize;
            g.drawLine(MARGIN + 1, y, WIDTH - MARGIN - 1, y);
            g.setColor(Color.DARK_GRAY);
            String text = String.format("%,.0f", lowerBound + i * tickSize);
            g.drawString(text, WIDTH - MARGIN + 5, y);
        }

        return image;
    }
}
