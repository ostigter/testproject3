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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Configuration {

    private static final File DATA_DIR = new File("data");

    private static final File CCC_LIST_FILE = new File(DATA_DIR, "CCC_list.xls");

    private static final File PORTFOLIO_FILE = new File(DATA_DIR, "portfolio.json");

    private static Configuration config;

    private final TreeMap<String, Stock> stocks;

    private final List<Transaction> transactions;

    private Configuration() {
        stocks = new TreeMap<String, Stock>();
        transactions = new ArrayList<Transaction>();
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

    public Set<Stock> getStocks() {
        return new TreeSet<Stock>(stocks.values());
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public boolean addStock(Stock stock) {
        String symbol = stock.getSymbol();
        if (!stocks.containsKey(symbol)) {
            stocks.put(symbol, stock);
            System.out.format("Added stock: %s\n", stock);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteStock(Stock stock) {
        String symbol = stock.getSymbol();
        if (stocks.containsKey(symbol)) {
            stocks.remove(symbol);
            System.out.format("Deleted stock: %s\n", stock);
            return true;
        } else {
            return false;
        }
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    private static Configuration load() {
        if (DATA_DIR.isDirectory()) {
            Gson gson = new GsonBuilder().create();
            if (PORTFOLIO_FILE.isFile()) {
                try (Reader reader = new BufferedReader(new FileReader(PORTFOLIO_FILE))) {
                    config = gson.fromJson(reader, Configuration.class);
                } catch (IOException e) {
                    System.err.println("ERROR: Could not read data file: " + PORTFOLIO_FILE.getAbsolutePath());
                    e.printStackTrace(System.err);
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
            System.err.println("ERROR: Could not write data file: " + PORTFOLIO_FILE.getAbsolutePath());
            e.printStackTrace(System.err);
        }
    }

    public void updateStock(String symbol, double price, double peRatio, double divRate) {
        Stock stock = stocks.get(symbol);
        if (stock != null) {
            if (price > 0.0) {
                stock.setPreviousPrice(stock.getPrice());
                stock.setPrice(price);
            }
            if (peRatio > 0.0) {
                stock.setPeRatio(peRatio);
            }
            if (divRate >= 0.0) {
                stock.setDivRate(divRate);
            }
        }
    }
}
