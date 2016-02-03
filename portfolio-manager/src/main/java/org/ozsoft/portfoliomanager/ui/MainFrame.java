package org.ozsoft.portfoliomanager.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.ozsoft.datatable.DataTable;
import org.ozsoft.portfoliomanager.domain.Configuration;
import org.ozsoft.portfoliomanager.services.UpdateService;
import org.ozsoft.portfoliomanager.ui.table.BenchTable;
import org.ozsoft.portfoliomanager.ui.table.GoalTable;
import org.ozsoft.portfoliomanager.ui.table.StockTable;
import org.ozsoft.portfoliomanager.ui.table.WatchTable;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 410441330732167672L;

    private static final String TITLE = "Portfolio Manager";

    private static final int DEFAULT_WIDTH = 950;

    private static final int DEFAULT_HEIGHT = 600;

    private final Configuration config = Configuration.getInstance();

    private final UpdateService updateService = new UpdateService();

    private JTabbedPane tabbedPane;

    private OwnedPanel ownedPanel;

    private DataTable goalTable;

    private DataTable watchTable;

    private DataTable benchTable;

    private DataTable allTable;

    private EditStockDialog addStockDialog;

    public MainFrame() {
        Locale.setDefault(Locale.US);

        initUI();

        updateTables();

        setVisible(true);
    }

    private void initUI() {
        setTitle(TITLE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        getContentPane().setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(UIConstants.SPACER_BORDER);
        toolBar.setFloatable(false);

        JButton button = new JButton("Update Prices");
        button.setToolTipText("Update stock prices (real-time)");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStockPrices();
            }
        });
        toolBar.add(button);

        button = new JButton("Update All");
        button.setToolTipText("Update all stock data");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStockData();
            }
        });
        toolBar.add(button);

        button = new JButton("Analyze All");
        button.setToolTipText("Analyze all stocks");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeStocks();
            }
        });
        toolBar.add(button);

        button = new JButton("Add Stock");
        button.setToolTipText("Add a new stock to the watch list");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStock();
            }
        });
        toolBar.add(button);

        getContentPane().add(toolBar, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(UIConstants.SPACER_BORDER);

        ownedPanel = new OwnedPanel(this);
        tabbedPane.add("Owned", ownedPanel);

        goalTable = new GoalTable(this);
        tabbedPane.add("Goal", new JScrollPane(goalTable));

        watchTable = new WatchTable(this);
        tabbedPane.add("Watch", new JScrollPane(watchTable));

        benchTable = new BenchTable(this);
        tabbedPane.add("Bench", new JScrollPane(benchTable));

        allTable = new StockTable(this);
        tabbedPane.add("All", new JScrollPane(allTable));

        tabbedPane.setToolTipTextAt(0, "Stocks currently or once owned");
        tabbedPane.setToolTipTextAt(1, "Favorite stocks to be owned sooner or later");
        tabbedPane.setToolTipTextAt(2, "Watch list of potential stocks to own");
        tabbedPane.setToolTipTextAt(3, "Stocks currently on the bench (disqualified)");
        tabbedPane.setToolTipTextAt(4, "All stocks being tracked");

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        addStockDialog = new EditStockDialog(this);

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public void updateTables() {
        ownedPanel.update();
        goalTable.update();
        watchTable.update();
        benchTable.update();
        allTable.update();
    }

    private void updateStockPrices() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateService.updatePrices(config.getStocks());
                updateTables();
            }
        });
    }

    private void updateStockData() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateService.updateStockData();
                updateTables();
            }
        });
    }

    private void addStock() {
        if (addStockDialog.show() == Dialog.OK) {
            config.addStock(addStockDialog.getStock());
            updateTables();
            tabbedPane.setSelectedIndex(2); // Jump to 'Watch' tab
        }
    }

    private void analyzeStocks() {
        updateService.analyzeAllStocks();
    }

    private void exit() {
        Configuration.save();
    }
}
