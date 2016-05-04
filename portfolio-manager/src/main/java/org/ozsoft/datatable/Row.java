package org.ozsoft.datatable;

public class Row {

    private Object[] cellValues;

    /* package */Row(int cellCount) {
        cellValues = new Object[cellCount];
    }

    public Object[] getCellValues() {
        return cellValues;
    }

    /* package */void setCellValues(Object... cellValues) {
        this.cellValues = cellValues;
    }

    public Object getCellValue(int columnIndex) {
        if (columnIndex < cellValues.length) {
            return cellValues[columnIndex];
        } else {
            throw new IllegalArgumentException("Invalid columnIndex: " + columnIndex);
        }
    }

    public void setCellValue(int columnIndex, Object cellValue) {
        if (columnIndex < cellValues.length) {
            cellValues[columnIndex] = cellValue;
        } else {
            throw new IllegalArgumentException("Invalid columnIndex: " + columnIndex);
        }
    }
}
