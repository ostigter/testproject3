package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;

/**
 * Target price index column renderer.
 * 
 * @author Oscar Stigter
 */
public class TPIColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = 7036417211003108327L;

    private Color backgroundColor;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double indexValue = (double) value;
            if (indexValue == 0.0) {
                backgroundColor = null;
                return null;
            } else {
                if (indexValue >= 100.0) {
                    // Target price reached
                    backgroundColor = Color.GREEN;
                } else if (indexValue >= 90.0) {
                    // Approaching target price
                    backgroundColor = Color.YELLOW;
                } else {
                    // Distant from target price
                    backgroundColor = null;
                }
                return String.format("%.1f", (double) value);
            }
        } else {
            backgroundColor = null;
            return null;
        }
    }

    @Override
    public Color getBackground() {
        if (backgroundColor != null) {
            return backgroundColor;
        } else {
            return super.getBackground();
        }
    }
}
