package mediacenter;


import java.awt.Color;


/**
 * Application-wide constants.
 * 
 * @author Oscar Stigter
 */
public interface Constants {


	/** Global screen width in pixels. */
	int SCREEN_WIDTH = 800;
	
	/** Global screen height in pixels. */
	int SCREEN_HEIGHT = 500;
	
    /** Background color. */
    Color BACKGROUND = Color.LIGHT_GRAY; //new Color(0, 100, 255);  // dark blue
    
    /** Foreground color. */
    Color FOREGROUND = Color.BLACK; //new Color(0, 200, 255);  // blue
    
    /** Selection color. */
    Color SELECTION = Color.BLUE; //Color.YELLOW;
    

    // Screens
    
    int MAIN = 0;
    
    int TV_SERIES = 1;
    
    int MOVIES = 2;
    
    int MUSIC = 3;
    
    int PICTURES = 4;
    

}
