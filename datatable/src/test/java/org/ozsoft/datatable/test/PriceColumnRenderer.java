package org.ozsoft.datatable.test;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class PriceColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -5054680250451958817L;

    private Color backgroundColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double numericValue = (double) value;
            if (numericValue >= 10.0) {
                backgroundColor = Color.WHITE;
            } else if (numericValue >= 5.0) {
                backgroundColor = Color.YELLOW;
            } else {
                backgroundColor = Color.ORANGE;
            }
            return String.format("$%.2f", numericValue);
        } else {
            return "<Error>";
        }
    }

    @Override
    public Color getBackground() {
        if (isFooter()) {
            return FOOTER_BACKGROUND;
        } else {
            return backgroundColor;
        }
    }
}
