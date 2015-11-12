package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;
import org.ozsoft.portfoliomanager.ui.UIConstants;

public class DGColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = 7036417211003108327L;

    private Color backgroundColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double percValue = (double) value;
            if (percValue < 0.0) {
                backgroundColor = Color.YELLOW;
                return "N/A";
            } else {
                if (percValue >= 20.0) {
                    // Excellent yield
                    backgroundColor = UIConstants.DARK_GREEN;
                } else if (percValue >= 10.0) {
                    // Good yield
                    backgroundColor = Color.GREEN;
                } else if (percValue >= 4.0) {
                    // Average yield
                    backgroundColor = Color.WHITE;
                } else if (percValue > 1.5) {
                    // Poor yield
                    backgroundColor = Color.YELLOW;
                } else {
                    // No yield
                    backgroundColor = Color.ORANGE;
                }
                return String.format("%.1f %%", (double) value);
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
