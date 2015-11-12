package org.ozsoft.datatable.test;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class PERatioColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = 4924544217201385488L;

    private Color backgroundColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double numericValue = (double) value;
            if (numericValue > 20.0) {
                backgroundColor = Color.ORANGE;
            } else if (numericValue > 15.0) {
                backgroundColor = Color.YELLOW;
            } else {
                backgroundColor = Color.GREEN;
            }
            return String.format("%.1f", numericValue);
        } else {
            return "<Error>";
        }
    }

    @Override
    public Color getBackground() {
        return backgroundColor;
    }
}
