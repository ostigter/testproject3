package customui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

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
        doLayout();
    }
    
    public Font getFont() {
    	return font;
    }
    
    public void setFont(Font font) {
    	this.font = font;
        doLayout();
    }

	@Override
	public void paint(Graphics2D g) {
	    doLayout();
	    
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        
        g.setColor(Color.GREEN);
        g.drawRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(text, x, 25 + y);
	}
	
	private void doLayout() {
	    if (isValid()) {
	        return;
	    }
	    
	    Rectangle2D bounds = font.getStringBounds(text, fontRenderContext);
	    int width = (int) bounds.getWidth();
	    int height = (int) bounds.getHeight();

		setSize(width, height);
		
		setValid(true);
	}

}
