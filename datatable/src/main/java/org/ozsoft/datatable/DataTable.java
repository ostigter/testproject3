package org.ozsoft.datatable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DataTable extends JPanel {

    private static final long serialVersionUID = 3822671047219990819L;

    private final Table mainTable;

    public DataTable() {
        mainTable = new Table();
        JScrollPane mainScrollPane = new JScrollPane(mainTable);

        setLayout(new BorderLayout());
        add(mainScrollPane, BorderLayout.NORTH);
    }

    public void setColumns(List<Column> columns) {
        mainTable.setColumns(columns);
    }

    public int getColumnCount() {
        return mainTable.getColumnCount();
    }

    public Column[] getColumns() {
        return mainTable.getColumns();
    }

    public int getRowCount() {
        return mainTable.getRowCount();
    }

    public Row[] getRows() {
        return mainTable.getRows();
    }

    public Object getCellValue(int rowIndex, int columnIndex) {
        return mainTable.getCellValue(rowIndex, columnIndex);
    }

    public void setCellValue(int rowIndex, int columnIndex, Object value) {
        mainTable.setCellValue(rowIndex, columnIndex, value);
    }

    public void addRow(Object... cellValues) {
        mainTable.addRow(cellValues);
    }

    public void update() {
        mainTable.update();
    }

    public void clear() {
        mainTable.clear();
    }

    private static class Table extends JTable {

        private static final long serialVersionUID = 5019884184139593450L;

        private DataTableModel model;

        private TableRowSorter<TableModel> sorter;

        public Table() {
            setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        public void setColumns(List<Column> columns) {
            model = new DataTableModel(columns);
            super.setModel(model);

            sorter = new TableRowSorter<TableModel>(model);
            setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
            sorter.sort();
        }

        @Override
        public int getColumnCount() {
            return (model != null) ? model.getColumnCount() : 0;
        }

        public Column[] getColumns() {
            return (model != null) ? model.getColumns() : null;
        }

        @Override
        public int getRowCount() {
            return (model != null) ? model.getRowCount() : 0;
        }

        public Row[] getRows() {
            return (model != null) ? model.getRows() : null;
        }

        public Object getCellValue(int rowIndex, int columnIndex) {
            if (model == null) {
                throw new IllegalStateException("Model not set");
            }

            int modelIndex = sorter.convertRowIndexToModel(rowIndex);
            return model.getValueAt(modelIndex, columnIndex);
        }

        public void setCellValue(int rowIndex, int columnIndex, Object value) {
            if (model == null) {
                throw new IllegalStateException("Model not set");
            }

            if (columnIndex < getColumnCount()) {
                if (rowIndex < getRowCount()) {
                    model.getRows()[rowIndex].setCellValue(columnIndex, value);
                } else {
                    throw new IllegalArgumentException("rowIndex out of bounds: " + rowIndex);
                }
            } else {
                throw new IllegalArgumentException("columnIndex out of bounds: " + columnIndex);
            }
        }

        @Override
        public TableCellRenderer getCellRenderer(int rowIndex, int columnIndex) {
            return model.getCellRenderer(rowIndex, columnIndex);
        }

        public void addRow(Object... cellValues) {
            if (model == null) {
                throw new IllegalStateException("Model not set");
            }

            model.addRow(cellValues);
        }

        public void update() {
            if (model != null) {
                model.fireTableDataChanged();
                resizeColumns();
                sorter.sort();
            }
        }

        public void clear() {
            if (model != null) {
                model.clear();
            }
        }

        private void resizeColumns() {
            int columnCount = getColumnCount();
            int rowCount = getRowCount();
            TableColumnModel columnModel = getColumnModel();
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                TableColumn column = columnModel.getColumn(columnIndex);
                int preferredWidth = column.getMinWidth();
                int maxWidth = column.getMaxWidth();

                // Header value with
                TableCellRenderer cellRenderer = column.getHeaderRenderer();
                if (cellRenderer == null) {
                    cellRenderer = getTableHeader().getDefaultRenderer();
                }
                Object headerValue = column.getHeaderValue();
                Component comp = cellRenderer.getTableCellRendererComponent(this, headerValue, false, false, 0, columnIndex);
                int width = comp.getPreferredSize().width + getIntercellSpacing().width + 15;
                preferredWidth = Math.max(preferredWidth, width);

                // Row values' width
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    cellRenderer = getCellRenderer(rowIndex, columnIndex);
                    comp = prepareRenderer(cellRenderer, rowIndex, columnIndex);
                    width = comp.getPreferredSize().width + getIntercellSpacing().width + 15;
                    preferredWidth = Math.max(preferredWidth, width);
                    if (preferredWidth >= maxWidth) {
                        preferredWidth = maxWidth;
                        break;
                    }
                }

                column.setPreferredWidth(preferredWidth);
            }
        }

        private static class DataTableModel extends AbstractTableModel {

            private static final long serialVersionUID = -1718244270107253916L;

            private static final TableCellRenderer DEFAULT_CELL_RENDERER = new DefaultColumnRenderer();

            private final List<Column> columns;

            private final List<Row> rows;

            public DataTableModel(List<Column> columns) {
                if (columns == null || columns.isEmpty()) {
                    throw new IllegalArgumentException("Null or empty columns");
                }

                this.columns = columns;

                rows = new ArrayList<Row>();
            }

            @Override
            public int getColumnCount() {
                return columns.size();
            }

            @Override
            public String getColumnName(int columnIndex) {
                if (columnIndex < getColumnCount()) {
                    return columns.get(columnIndex).getName();
                } else {
                    throw new IllegalArgumentException("columnIndex out of bounds: " + columnIndex);
                }
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (getRowCount() > 0) {
                    Object value = getValueAt(0, columnIndex);
                    if (value != null) {
                        return value.getClass();
                    } else {
                        return Object.class;
                    }
                } else {
                    return Object.class;
                }
            }

            @Override
            public int getRowCount() {
                return rows.size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex < getColumnCount()) {
                    if (rowIndex < getRowCount()) {
                        return rows.get(rowIndex).getCellValue(columnIndex);
                    } else {
                        throw new IllegalArgumentException("rowIndex out of bounds: " + rowIndex);
                    }
                } else {
                    throw new IllegalArgumentException("columnIndex out of bounds: " + columnIndex);
                }
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            public TableCellRenderer getCellRenderer(int rowIndex, int columnIndex) {
                if (columnIndex < getColumnCount()) {
                    TableCellRenderer columnRenderer = (TableCellRenderer) columns.get(columnIndex).getRenderer();
                    return (columnRenderer != null) ? columnRenderer : DEFAULT_CELL_RENDERER;
                } else {
                    throw new IndexOutOfBoundsException("columnIndex out of bounds: " + columnIndex);
                }
            }

            public void addRow(Object... cellValues) {
                int columnCount = getColumnCount();
                if (cellValues.length != columnCount) {
                    throw new IllegalArgumentException(String.format("Invalid number of columns (expected: %d, actual: %d)", columnCount,
                            cellValues.length));
                }
                Row row = new Row(columnCount);
                row.setCellValues(cellValues);
                rows.add(row);
            }

            public Column[] getColumns() {
                return columns.toArray(new Column[0]);
            }

            public Row[] getRows() {
                return rows.toArray(new Row[0]);
            }

            public void clear() {
                rows.clear();
            }
        }
    }
}
