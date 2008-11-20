package mediacenter;


import java.awt.Color;
import java.awt.Font;


/**
 * Application-wide constants.
 * 
 * @author Oscar Stigter
 */
public interface Constants {


    /** Background color. */
    Color BACKGROUND = new Color(0, 100, 255);  // medium blue
    
    /** Foreground color. */
    Color FOREGROUND = Color.CYAN;
    
    /** Selection color. */
    Color SELECTION = Color.YELLOW;
    
    /** Font size. */
    int FONT_SIZE = 20;
    
    /** Font. */
    Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);
    

}
