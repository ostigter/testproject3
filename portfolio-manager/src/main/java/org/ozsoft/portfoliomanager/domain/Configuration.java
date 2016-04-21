package org.ozsoft.portfoliomanager.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Configuration {

    private static final File DATA_DIR = new File("data");

    private static final File CCC_LIST_FILE = new File(DATA_DIR, "CCC_list.xls");

    private static final File PORTFOLIO_FILE = new File(DATA_DIR, "portfolio.json");

    private static final File ANALYSIS_RESULT_FILE = new File(DATA_DIR, "stock_analysis.csv");

    // private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    private static final Logger LOGGER = LogManager.getLogger(Configuration.class);

    private static Configuration config;

    private boolean showClosedPositions = false;

    private final TreeMap<String, Stock> stocks;

    private final List<Transaction> transactions;

    private Configuration() {
        stocks = new TreeMap<String, Stock>();
        transactions = new ArrayList<Transaction>();
        LOGGER.info("Configuration created");
    }

    public static Configuration getInstance() {
        if (config == null) {
            config = Configuration.load();
        }
        return config;
    }

    public File getCCCListFile() {
        return CCC_LIST_FILE;
    }

    public File getAnalysisResultFile() {
        return ANALYSIS_RESULT_FILE;
    }

    public Set<Stock> getStocks() {
        return new TreeSet<Stock>(stocks.values());
    }

    public Set<Stock> getOwnedStocks() {
        Set<Stock> ownedStocks = new TreeSet<Stock>();
        for (Position position : getPortfolio().getPositions()) {
            ownedStocks.add(position.getStock());
        }
        return ownedStocks;
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public boolean addStock(Stock stock) {
        String symbol = stock.getSymbol();
        if (!stocks.containsKey(symbol)) {
            stocks.put(symbol, stock);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteStock(Stock stock) {
        String symbol = stock.getSymbol();
        if (stocks.containsKey(symbol)) {
            stocks.remove(symbol);
            return true;
        } else {
            return false;
        }
    }

    public List<Transaction> getTransactions() {
        // TODO: Only sort and update IDs when needed (dirty flag).
        Collections.sort(transactions);

        // Update transaction IDs (incremental, sorted by date).
        int id = 1;
        for (Transaction transaction : transactions) {
            transaction.setId(id++);
        }

        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void deleteTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public Portfolio getPortfolio() {
        Portfolio portfolio = new Portfolio();
        for (Transaction transaction : getTransactions()) {
            portfolio.addTransaction(transaction);
        }
        portfolio.update(this);
        return portfolio;
    }

    public boolean getShowClosedPositions() {
        return showClosedPositions;
    }

    public void setShowClosedPositions(boolean showClosedPositions) {
        this.showClosedPositions = showClosedPositions;
    }

    private static Configuration load() {
        if (DATA_DIR.isDirectory()) {
            Gson gson = new GsonBuilder().create();
            if (PORTFOLIO_FILE.isFile()) {
                try (Reader reader = new BufferedReader(new FileReader(PORTFOLIO_FILE))) {
                    config = gson.fromJson(reader, Configuration.class);
                } catch (IOException e) {
                    LOGGER.error("Could not read data file: " + PORTFOLIO_FILE.getAbsolutePath(), e);
                }
            }
        }

        if (config == null) {
            config = new Configuration();
        }

        return config;
    }

    public static void save() {
        if (!DATA_DIR.exists()) {
            DATA_DIR.mkdirs();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new BufferedWriter(new FileWriter(PORTFOLIO_FILE))) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            LOGGER.error("Could not write data file: " + PORTFOLIO_FILE.getAbsolutePath(), e);
        }
    }
}
