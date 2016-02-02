package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import javax.swing.SwingConstants;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class SharesColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -4207280209997046453L;

    private Color textColor;

    public SharesColumnRenderer() {
        super(SwingConstants.RIGHT);
    }

    @Override
    public String formatValue(Object value) {
        if (value instanceof Integer) {
            int intValue = (int) value;
            if (intValue >= 0) {
                textColor = Color.BLACK;
            } else {
                textColor = Color.RED;
            }
            return String.valueOf(intValue);
        } else {
            return null;
        }
    }

    @Override
    public Color getForeground() {
        return textColor;
    }
}
