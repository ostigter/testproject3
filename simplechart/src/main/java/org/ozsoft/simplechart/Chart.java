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

    private static final int PREFERRED_TICK_COUNT = 12;

    private static final Color PRICE_LINE_COLOR = new Color(0x80, 0x80, 0xff);

//    private String caption;
//
//    private String xAxisLabel;
//
//    private String yAxisLabel;

    private final List<DataPoint> dataPoints;

    public Chart() {
        dataPoints = new ArrayList<DataPoint>();
    }

//    public void setCaption(String caption) {
//        this.caption = caption;
//    }
//
//    public void setXAxisLabel(String xAxisLabel) {
//        this.xAxisLabel = xAxisLabel;
//    }
//
//    public void setYAxisLabel(String yAxisLabel) {
//        this.yAxisLabel = yAxisLabel;
//    }

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

        // Prepare X axis (date).
        Date oldestDate = dataPoints.get(0).getDate();
        Date newestDate = dataPoints.get(pointCount - 1).getDate();
        long timeDiff = newestDate.getTime() - oldestDate.getTime();
        System.out.format("Time diff: %,d ms\n", timeDiff);
        TimeScale timeScale = TimeScale.parse(timeDiff);
        int timeWidthInUnits = (int) Math.round(timeDiff / (double) timeScale.getMilliseconds());
        System.out.format("%d %s\n", timeWidthInUnits, timeScale);

        // Prepare Y axis (price).
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
        double tickSize = (maxValue - minValue) / (PREFERRED_TICK_COUNT - 1);
        double pow10 = Math.pow(10, Math.ceil(Math.log10(tickSize) - 1.0));
        tickSize = Math.ceil(tickSize / pow10) * pow10;
        pow10 = Math.pow(10, Math.ceil(Math.log10(tickSize) - 1.0));
        int p = 0;
        while (tickSize >= 1.0) {
            tickSize /= 10.0;
            p++;
        }
        if (tickSize < 0.125) {
            tickSize = 0.1;
        } else if (tickSize < 0.225) {
            tickSize = 0.2;
        } else if (tickSize < 0.75) {
            tickSize = 0.5;
        } else {
            tickSize = 1.0;
        }
        tickSize = tickSize * Math.pow(10, p);
        double lowerBound = tickSize * Math.round(minValue / tickSize);
        while (lowerBound > minValue) {
            lowerBound -= tickSize;
        }
        double upperBound = tickSize * Math.round(maxValue / tickSize);
        while (upperBound < maxValue) {
            upperBound += tickSize;
        }
        int tickCount = (int) ((upperBound - lowerBound) / tickSize);
        System.out.format("min        = %.4f\n", minValue);
        System.out.format("max        = %.4f\n", maxValue);
        System.out.format("lowerBound = %.4f\n", lowerBound);
        System.out.format("upperBound = %.4f\n", upperBound);
        System.out.format("tickSize   = %.4f\n", tickSize);
        System.out.format("tickCount  = %d\n", tickCount);

        // Prepare image for the graph.
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setStroke(new BasicStroke(1.0f));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));

        // Draw outer box (axes).
        g.setColor(Color.GRAY);
        g.drawRect(MARGIN, MARGIN, WIDTH - 2 * MARGIN, HEIGHT - 2 * MARGIN);

        // Draw X axis tick lines and labels (date).
        double timeTickSize = (WIDTH - 2 * MARGIN) / (double) timeWidthInUnits;
        for (int i = 0; i <= timeWidthInUnits; i++) {
            int x = MARGIN + (int) Math.round(i * timeTickSize);
            if (i > 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(x, MARGIN + 1, x, HEIGHT - MARGIN - 1);
            }
            g.setColor(Color.DARK_GRAY);
            String text = String.format("%d", i);
            g.drawString(text, x, HEIGHT - MARGIN + 12);
        }

        // Draw Y axis tick lines and labels (price).
        double valueTickSize = (HEIGHT - 2 * MARGIN) / (double) tickCount;
        for (int i = 0; i <= tickCount; i++) {
            g.setColor(Color.LIGHT_GRAY);
            int y = HEIGHT - MARGIN + 1 - (int) Math.round(i * valueTickSize);
            g.drawLine(MARGIN + 1, y, WIDTH - MARGIN - 1, y);
            g.setColor(Color.DARK_GRAY);
            String text = String.format("%,.2f", lowerBound + i * tickSize);
            g.drawString(text, WIDTH - MARGIN + 5, y);
        }

        // Drop price line.
        double oldestTime = oldestDate.getTime();
        double newestTime = newestDate.getTime();
        double graphWidth = WIDTH - 2 * MARGIN;
        int graphHeight = HEIGHT - MARGIN;
        g.setColor(PRICE_LINE_COLOR);
        int prevX = -1;
        int prevY = -1;
        boolean firstPoint = true;
        for (DataPoint dp : dataPoints) {
            double time = dp.getDate().getTime();
            int x = MARGIN + (int) Math.round((time - oldestTime) / (newestTime - oldestTime) * graphWidth);
            double value = dp.getValue().doubleValue();
            int y = graphHeight - (int) Math.round((value - lowerBound) / (upperBound - lowerBound) * (graphHeight - MARGIN));
            System.out.format("%,.2f: (%d, %d)\n", value, x, y);
            if (firstPoint) {
                firstPoint = false;
            } else {
                g.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
        }

        return image;
    }
}
