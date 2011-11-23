package org.ozsoft.customui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public class Label extends AbstractComponent {

    private String text;

    private Font font;

    private int ascend;

    public Label(String text, Font font) {
        setText(text);
        setFont(font);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setValid(false);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        setValid(false);
    }

    @Override
    public void doLayout(Graphics2D g) {
        FontRenderContext frc = g.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics(text, g.getFontRenderContext());
        ascend = (int) lm.getAscent();
        Rectangle2D bounds = font.getStringBounds(text, frc);
        int width = (int) Math.round(bounds.getWidth());
        int height = (int) Math.round(lm.getHeight());
        setSize(width, height);
        setValid(true);
    }

    @Override
    public void paint(Graphics2D g) {
        doLayout(g);
        int x = getX();
        int y = getY();
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(text, x, y + ascend);
    }

}
