package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class DRColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -8744524110427922656L;

    private Color backgroundColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double numericValue = (double) value;
            if (numericValue >= 0.0) {
                backgroundColor = Color.WHITE;
                return String.format("$ %.2f", numericValue);
            } else {
                backgroundColor = Color.YELLOW;
                return "N/A";
            }
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
