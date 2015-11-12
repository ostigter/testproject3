package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class PercentageColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -765351086313615291L;

    private Color textColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double percValue = (double) value;
            if (percValue >= 0.0) {
                textColor = Color.BLACK;
            } else {
                textColor = Color.RED;
            }
            return String.format("%.2f%%", (double) value);
        } else {
            return "<Error>";
        }
    }

    @Override
    public Color getForeground() {
        return textColor;
    }
}
