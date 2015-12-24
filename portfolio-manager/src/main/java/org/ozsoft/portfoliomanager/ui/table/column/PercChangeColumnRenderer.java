package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;
import org.ozsoft.portfoliomanager.ui.UIConstants;

public class PercChangeColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -7786489823544289457L;

    private Color textColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double percValue = (double) value;
            if (percValue > 0.0) {
                textColor = UIConstants.DARKER_GREEN;
                return String.format("%+.2f %%", (double) value);
            } else if (percValue < 0.0) {
                textColor = Color.RED;
                return String.format("%+.2f %%", (double) value);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Color getForeground() {
        return textColor;
    }
}
