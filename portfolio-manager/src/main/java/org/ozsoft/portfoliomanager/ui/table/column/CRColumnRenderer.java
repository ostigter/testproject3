package org.ozsoft.portfoliomanager.ui.table.column;

import java.awt.Color;

import javax.swing.SwingUtilities;

import org.ozsoft.datatable.DefaultColumnRenderer;
import org.ozsoft.portfoliomanager.domain.CreditRating;
import org.ozsoft.portfoliomanager.ui.UIConstants;

/**
 * Credit rating column renderer.
 * 
 * @author Oscar Stigter
 */
public class CRColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = 4924544217201385488L;

    private Color backgroundColor;

    public CRColumnRenderer() {
        setHorizontalAlignment(SwingUtilities.CENTER);
    }

    @Override
    public String formatValue(Object value) {
        if (value instanceof CreditRating) {
            if (value == CreditRating.NA) {
                backgroundColor = Color.LIGHT_GRAY;
            } else {
                int ordinalValue = ((CreditRating) value).ordinal();
                if (ordinalValue <= CreditRating.A_MINUS.ordinal()) {
                    backgroundColor = UIConstants.DARK_GREEN;
                } else if (ordinalValue <= CreditRating.BBB_MINUS.ordinal()) {
                    backgroundColor = Color.GREEN;
                } else if (ordinalValue <= CreditRating.B_MINUS.ordinal()) {
                    backgroundColor = Color.YELLOW;
                } else {
                    backgroundColor = Color.ORANGE;
                }
            }
            return ((CreditRating) value).getText();
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
