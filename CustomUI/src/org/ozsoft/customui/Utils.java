package org.ozsoft.customui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public abstract class Utils {
    
    public static Dimension getTextDimension(String text, Font font, Graphics2D g) {
        FontRenderContext frc = g.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics(text, g.getFontRenderContext());
        Rectangle2D bounds = font.getStringBounds(text, frc);
        int width = (int) Math.round(bounds.getWidth());
        int height = (int) Math.round(lm.getHeight());
        return new Dimension(width, height);
    }

}
