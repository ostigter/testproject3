package org.ozsoft.datatable.test;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.ozsoft.datatable.Column;
import org.ozsoft.datatable.ColumnRenderer;
import org.ozsoft.datatable.DataTable;
import org.ozsoft.datatable.DefaultColumnRenderer;

/**
 * Test driver for the {@link DataTable} class.
 * 
 * @author Oscar Stigter
 */
public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("DataTable test application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ColumnRenderer defaultColumnRenderer = new DefaultColumnRenderer();
        ColumnRenderer centeredColumnRenderer = new DefaultColumnRenderer(SwingConstants.CENTER);
        ColumnRenderer priceColumnRenderer = new PriceColumnRenderer();
        ColumnRenderer peRatioColumnRenderer = new PERatioColumnRenderer();
        ColumnRenderer moneyColumnRenderer = new MoneyColumnRenderer();
        ColumnRenderer yieldColumnRenderer = new YieldColumnRenderer();

        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("Name", "Company name", defaultColumnRenderer));
        columns.add(new Column("Symbol", "Ticker symbol", centeredColumnRenderer));
        columns.add(new Column("Price", "Current stock price", priceColumnRenderer));
        columns.add(new Column("P/E", "Current price-to-earnings ratio", peRatioColumnRenderer));
        columns.add(new Column("DR", "Current dividend rate", moneyColumnRenderer));
        columns.add(new Column("Yield", "Current dividend yield", yieldColumnRenderer));

        DataTable table = new DataTable();
        table.setColumns(columns);

        table.addRow("Johnson & Johnson", "JNJ", 75.00, 27.4, 2.45, 3.5);
        table.addRow("Realty Income", "O", 45.10, 19.7, 1.48, 5.2);
        table.addRow("Wal-Mart", "WMT", 62.85, 12.1, 1.80, 2.44);
        table.addRow("Orchid Island Capital", "ORC", 9.51, 12.2, 1.68, 17.76);

        table.setFooterRow(null, null, 0.0, 16.85, 0.0, 7.225);

        table.update();

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        frame.getContentPane().add(new JScrollPane(table), gbc);

        // JScrollPane scrollPane = new JScrollPane(table);
        // frame.getContentPane().add(scrollPane);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
