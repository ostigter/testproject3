package org.ozsoft.customui;

import java.awt.Color;
import java.awt.Graphics2D;

public class Label extends AbstractComponent {

    private String text;

    private int ascend;

    public Label(String text) {
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setValid(false);
    }

    @Override
    public void doLayout(Graphics2D g) {
        if (!isValid()) {
            Dimension dimension = Utils.getTextDimension(text, getFont(), g);
            setSize(dimension.getWidth(), dimension.getHeight());
            setValid(true);
        }
    }

    @Override
    public void paint(Graphics2D g) {
        doLayout(g);
        int x = getX();
        int y = getY();
        g.setColor(Color.BLACK);
        g.setFont(getFont());
        g.drawString(text, x, y + ascend);
    }

}
