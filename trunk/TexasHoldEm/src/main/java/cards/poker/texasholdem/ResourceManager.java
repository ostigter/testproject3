package cards.poker.texasholdem;

import java.net.URL;
import javax.swing.ImageIcon;

import cards.Card;

/**
 * Resource manager.
 * 
 * @author Oscar Stigter
 */
public abstract class ResourceManager {
    
    public static ImageIcon getCardImage(Card card) {
        String numberString = String.valueOf(card.hashCode());
        if (numberString.length() == 1) {
            numberString = "0" + numberString;
        }
        String path = "/images/card_" + numberString + ".png";
        ImageIcon icon = getIcon(path);
        return icon;
    }
    
    public static ImageIcon getIcon(String path) {
        ImageIcon icon = null;
//        URL url = ClassLoader.getSystemResource(path);
        URL url = ResourceManager.class.getResource(path);
        if (url != null) {
            icon = new ImageIcon(url);
        } else {
            System.err.println("ERROR: Resource file not found: " + path);
        }
        return icon;
    }
    
}
