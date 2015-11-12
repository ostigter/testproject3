package org.ozsoft.datatable;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DataTableTest {

    @Test
    public void test() {
        // Create table with column definitions
        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("Symbol", "Ticker symbol"));
        columns.add(new Column("Name", "Company name"));
        columns.add(new Column("Price", "Current stock price"));
        DataTable table = new DataTable();
        table.setColumns(columns);

        Assert.assertEquals(3, table.getColumnCount());
        Assert.assertEquals(0, table.getRowCount());

        // Add rows
        table.addRow("JNJ", "Johnson & Johnson", 48.25);
        table.addRow("O", "Realty Income", 45.10);
        table.addRow("PG", "Proctor & Gamble", 62.85);
        Assert.assertEquals(3, table.getRowCount());

        // Access rows
        Assert.assertEquals(3, table.getRows().length);
        Row row = table.getRows()[0];
        Assert.assertNotNull(row);
        Assert.assertEquals("JNJ", row.getCellValue(0));
        Assert.assertEquals(48.25, row.getCellValue(2));
        row.setCellValue(2, 50.00);
        Assert.assertEquals(50.00, row.getCellValue(2));

        // Read and write cells directly
        Assert.assertEquals("Realty Income", table.getCellValue(1, 1));
        Assert.assertEquals(45.10, table.getCellValue(1, 2));
        table.setCellValue(0, 2, 51.00);
        Assert.assertEquals(51.00, table.getCellValue(0, 2));
    }
}
