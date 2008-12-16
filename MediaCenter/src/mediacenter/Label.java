package mediacenter;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;


/**
 * A custom, lightweight label.
 * 
 * @author Oscar Stigter
 */
public class Label extends JComponent {


    private static final long serialVersionUID = 1L;
    
    private String text;
    
    
    public Label(String text, double widthPerc, double heightPerc) {
        int width = (int) (Constants.SCREEN_WIDTH * widthPerc);
        int height = (int) (Constants.SCREEN_HEIGHT * heightPerc);
        setPreferredSize(new Dimension(width, height));
        setText(text);
    }
    
    
    public void setText(String text) {
        this.text = text;
        repaint();
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int fontSize = height * 6 / 10;
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
        g.setFont(font);
        g.setColor(Constants.FOREGROUND);
        int y = 2 + (fontSize / 4) + (height / 2);
        g.drawRect(0, 0, width - 1, height - 1);
        g.drawString(text, 0, y);
    }
         

}
