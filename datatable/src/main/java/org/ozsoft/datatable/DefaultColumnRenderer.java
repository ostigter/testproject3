package org.ozsoft.datatable;

import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class DefaultColumnRenderer extends DefaultTableCellRenderer implements ColumnRenderer {

    private static final long serialVersionUID = -6888931596009424634L;

    private static final int DEFAULT_DECIMAL_PRECISION = 2;

    protected static final Color FOOTER_BACKGROUND = new Color(0xf0, 0xf0, 0xf0); // silver

    private int decimalPrecision = DEFAULT_DECIMAL_PRECISION;

    private Integer horizontalAlignment = null;

    private boolean isFooter = false;

    public DefaultColumnRenderer() {
        // Empty implementation.
    }

    public DefaultColumnRenderer(int horizontalAlignment) {
        setHorizontalAlignment(horizontalAlignment);
    }

    public DefaultColumnRenderer(int horizontalAlignment, int decimalPrecision) {
        setHorizontalAlignment(horizontalAlignment);
        setDecimalPrecision(decimalPrecision);
    }

    public final int getDecimalPrecision() {
        return decimalPrecision;
    }

    @Override
    public final void setDecimalPrecision(int decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
    }

    @Override
    public final int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    @Override
    public final void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    @Override
    public void setFooter(boolean isFooter) {
        this.isFooter = isFooter;
    }

    protected boolean isFooter() {
        return isFooter;
    }

    @Override
    public String formatValue(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            String format = String.format("%%,.%df", getDecimalPrecision());
            return String.format(format, value);
        } else if (value instanceof Integer) {
            return String.format("%,d", value);
        } else if (value instanceof Long) {
            return String.format("%,d", value);
        } else {
            return value.toString();
        }
    }

    @Override
    protected final void setValue(Object value) {
        if (horizontalAlignment == null) {
            setDefaultHorizontalAlignment(value);
        }

        setText(formatValue(value));
    }

    @Override
    public Color getBackground() {
        if (isFooter) {
            return FOOTER_BACKGROUND;
        } else {
            return super.getBackground();
        }
    }

    private void setDefaultHorizontalAlignment(Object value) {
        if (value instanceof Number) {
            horizontalAlignment = SwingConstants.RIGHT;
        } else {
            horizontalAlignment = SwingConstants.LEFT;
        }
    }
}
