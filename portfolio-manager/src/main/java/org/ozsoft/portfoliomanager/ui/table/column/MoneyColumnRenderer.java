package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class MoneyColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -8744524110427922656L;

    private static final int DEFAULT_DECIMAL_PRECISION = 2;

    private final String positiveFormat;

    private final String negativeFormat;

    private Color textColor;

    public MoneyColumnRenderer() {
        this(DEFAULT_DECIMAL_PRECISION);
    }

    public MoneyColumnRenderer(int decimalPrecision) {
        if (decimalPrecision < 0) {
            throw new IllegalArgumentException("Invalid decimalPrecision; must be equal to 0 or greater");
        }
        positiveFormat = String.format("$ %%,.%df", decimalPrecision);
        negativeFormat = String.format("($ %%,.%df)", decimalPrecision);
    }

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double numericValue = (double) value;
            if (numericValue >= 0.0) {
                textColor = Color.BLACK;
                return String.format(positiveFormat, numericValue);
            } else {
                textColor = Color.RED;
                return String.format(negativeFormat, Math.abs(numericValue));
            }
        } else {
            textColor = Color.RED;
            return "<Error>";
        }
    }

    @Override
    public Color getForeground() {
        return textColor;
    }
}
