package org.ozsoft.mediacenter.shared;

import java.awt.Color;
import java.awt.Font;

public interface Constants {

    /** INI file. */
    String INI_FILE = "MediaCenter.ini";
    
    /** Default server host. */
    String DEFAULT_SERVER_HOST = "127.0.0.1";
    
    /** Default server port. */
    int DEFAULT_SERVER_PORT = 6000;

	/** The service ID. */
    String SERVICE_ID = "MediaServer";
    
    /** Application width in pixels (normal size). */
    int WIDTH = 800;
    
    /** Application height in pixels (normal size). */
    int HEIGHT = 600;

	/** Font size. */
    int FONT_SIZE = 30;
	
	/** Font. */
    Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);
    
    /** Background color. */
    Color BACKGROUND = new Color(0, 0, 64);
    
    /** Foreground color. */
    Color FOREGROUND = Color.WHITE;
    
    /** Background color for selected text. */ 
    Color BACKGROUND_SELECTION = new Color(0, 128, 255);
    
    /** Foreground color for selected text. */ 
    Color FOREGROUND_SELECTION = Color.WHITE;
    
    /** Background color for already seen media files. */ 
    Color BACKGROUND_SEEN = Color.BLACK;
    
    /** Foreground color for already seen media files. */ 
    Color FOREGROUND_SEEN = Color.GRAY;
    
    /** Duration in seconds after which media file is marked as seen. */
    int SEEN_DURATION = 60;
    
}
