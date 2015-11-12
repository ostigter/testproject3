package org.ozsoft.datatable.test;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class DivRateColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -8744524110427922656L;

    private Color textColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double numericValue = (double) value;
            if (numericValue >= 0.0) {
                textColor = Color.BLACK;
            } else {
                textColor = Color.RED;
            }
            return String.format("$%.2f", numericValue);
        } else {
            return "<Error>";
        }
    }

    @Override
    public Color getBackground() {
        return textColor;
    }
}
