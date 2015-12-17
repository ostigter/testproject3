package org.ozsoft.portfoliomanager.ui.table.column;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class PercentageColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -765351086313615291L;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double percValue = (double) value;
            if (percValue == 0.0) {
                return null;
            } else {
                return String.format("%.2f %%", (double) value);
            }
        } else {
            return "<ERROR>";
        }
    }
}
