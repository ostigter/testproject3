package customui;

import java.awt.Color;
import java.awt.Graphics;

public class Label extends AbstractComponent {

    private String text;

    public Label(String text) {
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void doLayout() {
        // FIXME: Fixed size for testing
        setWidth(200);
        setHeight(50);
    }

    public void paintComponent(Graphics g) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        g.setColor(Color.BLACK);
        g.drawRect(x, y, width - 1, height - 1);
    }

}
