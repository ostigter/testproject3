package customui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Label extends AbstractComponent {

    private String text;
    
    private Font font;

    public Label(String text, Font font) {
        setText(text);
        setFont(font);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public Font getFont() {
    	return font;
    }
    
    public void setFont(Font font) {
    	this.font = font;
    }

    @Override
    public void doLayout() {
        // FIXME: Fixed size for testing
        setWidth(200);
        setHeight(50);
    }

	@Override
	public void paint(Graphics2D g) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width - 1, height - 1);
        g.setFont(font);
        g.drawString(text, x + 5, y + y);
	}

}
