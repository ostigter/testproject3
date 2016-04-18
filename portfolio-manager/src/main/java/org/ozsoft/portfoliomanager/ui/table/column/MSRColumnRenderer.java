package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import javax.swing.SwingConstants;

import org.ozsoft.datatable.DefaultColumnRenderer;
import org.ozsoft.portfoliomanager.ui.UIConstants;

/**
 * Table cell renderer for Morningstar Star Rating (1 to 5 stars).
 * 
 * @author Oscar Stigter
 */
public class MSRColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = 9128778326338428542L;

    private Color backgroundColor;

    public MSRColumnRenderer() {
        super(SwingConstants.CENTER);
    }

    @Override
    public String formatValue(Object value) {
        if (value instanceof Integer) {
            int starRating = (int) value;
            if (starRating < 1) {
                backgroundColor = Color.LIGHT_GRAY;
                return "N/R";
            } else {
                if (starRating <= 1) {
                    backgroundColor = Color.ORANGE;
                } else if (starRating == 2) {
                    backgroundColor = Color.YELLOW;
                } else if (starRating == 3) {
                    backgroundColor = Color.WHITE;
                } else if (starRating == 4) {
                    backgroundColor = Color.GREEN;
                } else {
                    backgroundColor = UIConstants.DARK_GREEN;
                }
            }
            return "*****".substring(0, starRating);
        } else {
            backgroundColor = Color.WHITE;
            return null;
        }
    }

    @Override
    public Color getBackground() {
        return backgroundColor;
    }
}
