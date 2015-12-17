package org.ozsoft.portfoliomanager.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.ozsoft.portfoliomanager.domain.ClosingPrice;
import org.ozsoft.portfoliomanager.domain.Configuration;
import org.ozsoft.portfoliomanager.domain.Stock;
import org.ozsoft.portfoliomanager.domain.StockAnalysis;
import org.ozsoft.portfoliomanager.domain.StockPerformance;
import org.ozsoft.portfoliomanager.domain.TimeRange;
import org.ozsoft.portfoliomanager.util.HttpPageReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UpdateService {

    private static final String CCC_LIST_URI = "http://www.dripinvesting.org/Tools/U.S.DividendChampions.xls";

    private static final String SHEET_NAME = "All CCC";

    private static final int SYMBOL_COLUMN_INDEX = CellReference.convertColStringToIndex("B");

    private static final int YEARS_GROWTH_COLUMN_INDEX = CellReference.convertColStringToIndex("D");

    private static final int DIV_GROWTH_COLUMN_INDEX = CellReference.convertColStringToIndex("AN");

    private static final String FULL_STOCK_QUOTE_URI = "http://query.yahooapis.com/v1/public/yql?q=select+*+from+yahoo.finance.quotes+where+symbol+=+'%s'&env=http://datatables.org/alltables.env&format=json";

    private static final String SIMPLE_STOCK_QUOTE_URI = "http://download.finance.yahoo.com/d/quotes.csv?s=%s&f=l1";

    private static final String HISTORICAL_CLOSINGS_URL = "http://ichart.finance.yahoo.com/table.csv?s=%s";

    private static final String HISTORICAL_DIVIDENDS_URL = "http://ichart.finance.yahoo.com/table.csv?s=%s&g=v";

    private static final DateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("yyyy-MM-dd");

    // private static final DateFormat DATE_FORMAT_LONG = new SimpleDateFormat("MM/dd/yyyy h:mma");

    private final Configuration config = Configuration.getInstance();

    private final HttpPageReader httpPageReader = new HttpPageReader();

    public void updateStockData() {
        System.out.println("Updating stocks");
        updateStatistics();
        updateAllPrices();
        Configuration.save();
    }

    public void updatePrice(Stock stock) {
        String symbol = stock.getSymbol();
        try {
            String quote = IOUtils.toString(httpPageReader.downloadFile(String.format(SIMPLE_STOCK_QUOTE_URI, symbol)));
            double newPrice = Double.parseDouble(quote);
            stock.setPrevClose(stock.getPrice());
            stock.setPrice(newPrice);
        } catch (IOException e) {
            System.err.format("ERROR: Failed to retrieve quote for '%s': %s\n", symbol, e.getMessage());
        }
    }

    public void analyzeAllStocks() {
        System.out.println("\nAnalyzing stocks...");
        List<StockAnalysis> analyses = new ArrayList<StockAnalysis>();
        for (Stock stock : config.getStocks()) {
            System.out.format("  %s\n", stock);
            analyses.add(analyzeStock(stock));
        }
        Collections.sort(analyses);

        System.out.println("\nStocks sorted by score:");
        for (StockAnalysis analysis : analyses) {
            System.out.println(analysis);
        }
    }

    public StockAnalysis analyzeStock(Stock stock) {
        List<ClosingPrice> prices = getClosingPrices(stock);

        StockPerformance perf10yr = new StockPerformance(prices, TimeRange.TEN_YEAR);
        StockPerformance perf5yr = new StockPerformance(prices, TimeRange.FIVE_YEAR);
        StockPerformance perf1yr = new StockPerformance(prices, TimeRange.ONE_YEAR);

        double score = 20.0 + perf10yr.getCagr() + perf5yr.getCagr() - 12.0 + 2.0 * (perf5yr.getCagr() - perf10yr.getCagr()) - 0.5
                * (perf10yr.getVolatility() - 10.0) + 0.5 * perf1yr.getDiscount();

        StockAnalysis analysis = new StockAnalysis(stock, perf10yr.getCagr(), perf5yr.getCagr(), perf1yr.getChangePerc(), perf10yr.getVolatility(),
                perf1yr.getHighPrice(), perf1yr.getLowPrice(), perf1yr.getEndPrice(), perf5yr.getDiscount(), perf1yr.getDiscount(), score);

        return analysis;
    }

    public void printStockAnalysis(Stock stock) {
        StockAnalysis analysis = analyzeStock(stock);
        System.out.format("\nAnalysis for %s:\n\n", analysis.getStock());
        System.out.format("10-year CAGR:        %+.2f %%\n", analysis.getCagr10yr());
        System.out.format(" 5-year CAGR:        %+.2f %%\n", analysis.getCagr5yr());
        System.out.format(" 1-year change:      %+.2f %%\n", analysis.getChange1yr());
        System.out.format("10-year volatility:   %.2f %%\n", analysis.getVolatility());
        System.out.format("52-week high:       $ %.2f\n", analysis.getHigh52wk());
        System.out.format("52-week low:        $ %.2f\n", analysis.getLow52wk());
        System.out.format("Current price:      $ %.2f\n", analysis.getCurrentPrice());
        System.out.format(" 5-year discount:    %.2f %%\n", analysis.getDiscount5yr());
        System.out.format(" 1-year discount:    %.2f %%\n", analysis.getDiscount1yr());
        System.out.format("Score:               %.2f\n", analysis.getScore());
    }

    private void updateStatistics() {
        // Download CCC list if missing or newer available
        File cccListFile = config.getCCCListFile();
        long localTimestamp = (cccListFile.exists()) ? cccListFile.lastModified() : -1L;
        try {
            System.out.println("Check for new version of CCC list");
            long remoteTimestamp = httpPageReader.getFileLastModified(CCC_LIST_URI);
            if (remoteTimestamp > localTimestamp) {
                System.out.println("Downloading CCC list");
                downloadCCCList(cccListFile);
            }

            // Update stock statistics from CCC list
            System.out.println("Updating stock statistics");
            try {
                Workbook workbook = WorkbookFactory.create(cccListFile);
                Sheet sheet = workbook.getSheet(SHEET_NAME);
                int count = 0;
                for (Row row : sheet) {
                    if (row.getRowNum() > 5) {
                        Cell cell = row.getCell(SYMBOL_COLUMN_INDEX);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String symbol = cell.getStringCellValue();
                            int yearsDivGrowth = (int) Math.floor(row.getCell(YEARS_GROWTH_COLUMN_INDEX).getNumericCellValue());
                            cell = row.getCell(DIV_GROWTH_COLUMN_INDEX);
                            double divGrowth = (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cell.getNumericCellValue() : -1.0;

                            Stock stock = config.getStock(symbol);
                            if (stock != null) {
                                stock.setDivGrowth(divGrowth);
                                stock.setYearsDivGrowth(yearsDivGrowth);
                                count++;
                            }
                        }
                    }
                }
                System.out.format("Statistics updated for %d stocks.\n", count);

            } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
                System.err.format("ERROR: Failed to process CCC list: %s\n", e.getMessage());
            }
        } catch (IOException e) {
            System.err.format("ERROR: Failed to download CCC list: %s\n", e.getMessage());
        }
    }

    private void downloadCCCList(File file) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = httpPageReader.downloadFile(CCC_LIST_URI);
            os = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.copy(is, os);
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        } catch (IOException e) {
            System.err.format("ERROR: Failed to download CCC list: %s\n", e.getMessage());
            file.delete();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

    private void updateAllPrices() {
        System.out.println("Updating stock prices");

        int count = 0;

        JsonParser parser = new JsonParser();
        for (Stock stock : config.getStocks()) {
            String symbol = stock.getSymbol();
            try {
                String json = httpPageReader.read(String.format(FULL_STOCK_QUOTE_URI, stock.getSymbol()));
                JsonObject quote = parser.parse(json).getAsJsonObject().getAsJsonObject("query").getAsJsonObject("results").getAsJsonObject("quote");
                double price = getJsonDoubleValue(quote, "LastTradePriceOnly");
                double prevClose = getJsonDoubleValue(quote, "PreviousClose");
                double peRatio = getJsonDoubleValue(quote, "PERatio");
                double divRate = getJsonDoubleValue(quote, "DividendShare");
                config.updateStock(symbol, price, prevClose, peRatio, divRate);
                System.out.format("Updated stock price of %s.\n", stock);
                count++;

            } catch (IOException e) {
                System.err.format("ERROR: Failed to retrieve stock data for '%s': %s\n", symbol, e.getMessage());
            }
        }

        System.out.format("%d stock prices updated\n", count);
    }

    public List<ClosingPrice> getClosingPrices(Stock stock) {
        String symbol = stock.getSymbol();
        String uri = String.format(HISTORICAL_CLOSINGS_URL, symbol);
        List<ClosingPrice> prices = new ArrayList<ClosingPrice>();
        boolean inHeader = true;
        try {
            String response = httpPageReader.read(uri);
            for (String line : response.split("\n")) {
                if (inHeader) {
                    inHeader = false;
                } else {
                    String[] fields = line.split(",");
                    if (fields.length == 7) {
                        try {
                            Date date = DATE_FORMAT_SHORT.parse(fields[0]);
                            double value = Double.parseDouble(fields[6]);
                            prices.add(new ClosingPrice(date, value));
                        } catch (ParseException e) {
                            System.err.format("ERROR: Could not parse price quote: '%s'\n", line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.format("ERROR: Could retrieve historical prices for '%s': %s\n", symbol, e.getMessage());
            e.printStackTrace(System.err);
        }

        return prices;
    }

    public List<ClosingPrice> getDividends(Stock stock) {
        String symbol = stock.getSymbol();
        String uri = String.format(HISTORICAL_DIVIDENDS_URL, symbol);
        List<ClosingPrice> prices = new ArrayList<ClosingPrice>();
        boolean inHeader = true;
        try {
            String response = httpPageReader.read(uri);
            for (String line : response.split("\n")) {
                if (inHeader) {
                    inHeader = false;
                } else {
                    String[] fields = line.split(",");
                    if (fields.length == 2) {
                        try {
                            Date date = DATE_FORMAT_SHORT.parse(fields[0]);
                            double value = Double.parseDouble(fields[1]);
                            prices.add(new ClosingPrice(date, value));
                        } catch (ParseException e) {
                            System.err.format("ERROR: Could not parse price quote: '%s'\n", line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.format("ERROR: Could retrieve historical dividend payments for '%s': %s\n", symbol, e.getMessage());
            e.printStackTrace(System.err);
        }

        return prices;
    }

    private static double getJsonDoubleValue(JsonObject obj, String memberName) {
        JsonElement elem = obj.get(memberName);
        if (!elem.isJsonNull()) {
            return elem.getAsDouble();
        } else {
            return -1.0;
        }
    }
}
