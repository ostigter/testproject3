package mediacenter;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;


/**
 * A custom, lightweight button.
 * 
 * @author Oscar Stigter
 */
public class Button extends JComponent {
    
    
    private static final long serialVersionUID = 1L;
    
    private final Set<ButtonListener> listeners;
    
	private String text;
	
	private boolean isSelected = false;
	

	public Button(int width, int height, String text) {
        listeners = new HashSet<ButtonListener>();
        
        setPreferredSize(new Dimension(width, height));
		setBackground(Constants.BACKGROUND);
        setForeground(Color.BLACK);
        
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                isSelected = true;
                Button.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isSelected = false;
                Button.this.repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                fireButtonClicked();
            }

        });
		setText(text);
	}
	
	
	public void setText(String text) {
		this.text = text;
	}
	
	
    public void addButtonListener(ButtonListener listener) {
        listeners.add(listener);
    }
    
    
    public void removeButtonListener(ButtonListener listener) {
        listeners.remove(listener);
    }
    
    
	@Override // Object
	public String toString() {
	    return String.format("Button \"%s\"", text);
	}
	
	
    @Override
    protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         
         int width = getWidth();
         int height = getHeight();
         
         g.setFont(Constants.FONT);
         FontMetrics fm = g.getFontMetrics();
         Rectangle2D r = fm.getStringBounds(text, g);
         int textWidth = (int) r.getWidth();
         
         g.setColor(Constants.BACKGROUND);
         g.fillRect(0, 0, width, height);
         g.setColor(Constants.FOREGROUND);
         g.drawRect(0, 0, width - 2, height - 2);
         if (isSelected) {
             g.setColor(Constants.SELECTION);
         } else {
             g.setColor(Constants.FOREGROUND);
         }
         int x = (width / 2) - (textWidth / 2);
         int y = 2 + (Constants.FONT_SIZE / 4) + (height / 2);
         g.drawString(text, x, y);
    }
	

    private void fireButtonClicked() {
        for (ButtonListener listener : listeners) {
            listener.buttonClicked(this);
        }
    }


}
