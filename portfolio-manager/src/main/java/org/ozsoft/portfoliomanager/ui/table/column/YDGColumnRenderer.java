package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;
import org.ozsoft.portfoliomanager.ui.UIConstants;

public class YDGColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -8644113732788006823L;

    private Color backgroundColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Integer) {
            int numericValue = (int) value;
            if (numericValue < 5) {
                backgroundColor = Color.ORANGE;
                return "<5";
            } else {
                if (numericValue >= 25) {
                    backgroundColor = UIConstants.DARK_GREEN;
                } else if (numericValue >= 10.0) {
                    backgroundColor = Color.GREEN;
                } else {
                    backgroundColor = Color.YELLOW;
                }
                return String.valueOf(numericValue);
            }
        } else {
            return "<Error>";
        }
    }

    @Override
    public Color getBackground() {
        return backgroundColor;
    }
}
