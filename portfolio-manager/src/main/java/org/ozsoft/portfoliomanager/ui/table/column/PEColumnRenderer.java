package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class PEColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = 4924544217201385488L;

    private Color backgroundColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double numericValue = (double) value;
            if (numericValue < 0.0) {
                backgroundColor = Color.ORANGE;
                return "N/A";
            } else {
                if (numericValue > 20.0) {
                    backgroundColor = Color.ORANGE;
                } else if (numericValue > 15.0) {
                    backgroundColor = Color.YELLOW;
                } else {
                    backgroundColor = Color.GREEN;
                }
                return String.format("%.1f", numericValue);
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
