package org.ozsoft.portfoliomanager.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

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
        for (Transaction transaction : transactions) {
            long timestamp = transaction.getDate();
            String text = String.valueOf(timestamp);
            if (text.startsWith("2015") || text.startsWith("2016")) {
                try {
                    Date date = DATE_FORMAT.parse(text);
                    transaction.setDate(date.getTime());
                } catch (ParseException e) {
                    System.err.println("ERROR: Invalid date: " + timestamp);
                    e.printStackTrace();
                }
            }
        }

        return Collections.unmodifiableList(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Portfolio getPortfolio() {
        Portfolio portfolio = new Portfolio();
        for (Transaction transaction : getTransactions()) {
            portfolio.addTransaction(transaction);
        }
        portfolio.update(this);
        return portfolio;
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
}
