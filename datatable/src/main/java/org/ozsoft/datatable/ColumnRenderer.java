package org.ozsoft.datatable;

public interface ColumnRenderer {

    void setFooter(boolean isFooter);

    void setDecimalPrecision(int decimalPrecision);

    void setHorizontalAlignment(int horizontalAlignment);

    String formatValue(Object value);
}
