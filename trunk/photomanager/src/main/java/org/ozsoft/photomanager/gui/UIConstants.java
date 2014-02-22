package org.ozsoft.photomanager.gui;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Application-wide UI constants.
 * 
 * @author Oscar Stigter
 */
public interface UIConstants {

    /** Default application width in pixels. */
    int DEFAULT_APP_WIDTH = 900;

    /** Default application height in pixels. */
    int DEFAULT_APP_HEIGHT = 600;

    /** Default backgrond color. */
    Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    /** Background color for selected items. */
    Color SELECTION_BACKGROUND_COLOR = new Color(255, 255, 128); // light yellow

    /** Line color. */
    Color LINE_COLOR = Color.LIGHT_GRAY;

    /** Font for normal labels. */
    Font NORMAL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);

    /** Font for small labels. */
    Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);

    /** Date format. */
    DateFormat DATE_FORMAT = new SimpleDateFormat("d MMM. yyyy");

}
