package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;
import org.ozsoft.portfoliomanager.ui.UIConstants;

public class YieldColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = 7036417211003108327L;

    private Color backgroundColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double percValue = (double) value;
            if (percValue >= 6.0) {
                // Very high yield
                backgroundColor = UIConstants.DARK_GREEN;
            } else if (percValue >= 3.5) {
                // High yield
                backgroundColor = Color.GREEN;
            } else if (percValue >= 2.5) {
                // Good yield
                backgroundColor = Color.WHITE;
            } else {
                // Poor yield
                backgroundColor = Color.YELLOW;
            }
            return String.format("%.2f %%", (double) value);
        } else {
            return "<Error>";
        }
    }

    @Override
    public Color getBackground() {
        return backgroundColor;
    }
}
