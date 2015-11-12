package org.ozsoft.datatable;

public interface ColumnRenderer {

    void setDecimalPrecision(int decimalPrecision);

    void setHorizontalAlignment(int horizontalAlignment);

    String formatValue(Object value);
}
