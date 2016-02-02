package org.ozsoft.portfoliomanager.ui.table.column;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingConstants;

import org.ozsoft.datatable.DefaultColumnRenderer;

public class DateColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -8106605471380329103L;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    public DateColumnRenderer() {
        super(SwingConstants.CENTER);
    }

    @Override
    public String formatValue(Object value) {
        if (value instanceof Long) {
            return DATE_FORMAT.format(new Date((long) value));
        } else {
            return null;
        }
    }
}
