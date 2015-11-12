package org.ozsoft.portfoliomanager.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.ozsoft.portfoliomanager.domain.Configuration;
import org.ozsoft.portfoliomanager.domain.Stock;
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

    // private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy h:mma");

    private final Configuration config = Configuration.getInstance();

    private final HttpPageReader httpPageReader = new HttpPageReader();

    public void updateStockData() {
        System.out.println("Updating stocks");
        // readNewStocks();
        updateStatistics();
        updateAllPrices();
        Configuration.save();
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
                double peRatio = getJsonDoubleValue(quote, "PERatio");
                double divRate = getJsonDoubleValue(quote, "DividendShare");
                config.updateStock(symbol, price, peRatio, divRate);
                System.out.format("Updated stock price of %s.\n", stock);
                count++;

            } catch (IOException e) {
                System.err.format("ERROR: Failed to retrieve stock data for '%s': %s\n", symbol, e.getMessage());
            }
        }

        System.out.format("%d stock prices updated\n", count);
    }

    public void updatePrice(Stock stock) {
        String symbol = stock.getSymbol();

        try {
            String quote = IOUtils.toString(httpPageReader.downloadFile(String.format(SIMPLE_STOCK_QUOTE_URI, symbol)));
            double newPrice = Double.parseDouble(quote);
            // String[] fields = quote.split(",");
            // String dateString = stripQuotes(fields[0]) + " " + stripQuotes(fields[1]);
            // try {
            // Calendar date = Calendar.getInstance();
            // date.setTime(DATE_FORMAT.parse(dateString));
            stock.setPreviousPrice(stock.getPrice());
            // stock.setPrice(Double.valueOf(fields[2]));
            stock.setPrice(newPrice);
            // } catch (ParseException e) {
            // System.err.format("ERROR: Could not parse date '%s'\n", dateString);
            // e.printStackTrace(System.err);
            // }

        } catch (IOException e) {
            System.err.format("ERROR: Failed to retrieve quote for '%s': %s\n", symbol, e.getMessage());
        }
    }

    private static double getJsonDoubleValue(JsonObject obj, String memberName) {
        JsonElement elem = obj.get(memberName);
        if (!elem.isJsonNull()) {
            return elem.getAsDouble();
        } else {
            return -1.0;
        }
    }

    // private static String stripQuotes(String text) {
    // int length = (text != null) ? text.length() : 0;
    // if (length > 0 && text.charAt(0) == '\"' && text.charAt(length - 1) == '\"') {
    // return text.substring(1, length - 1);
    // } else {
    // return text;
    // }
    // }

    // private void readNewStocks() {
    // System.out.println("Read new stocks");
    // try {
    // Workbook workbook = WorkbookFactory.create(new File("data/portfolio.xlsx"));
    // Sheet sheet = workbook.getSheet("Goal");
    // int count = 0;
    // for (Row row : sheet) {
    // if (row.getRowNum() > 4) {
    // Cell cell = row.getCell(1);
    // if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
    // String symbol = cell.getStringCellValue();
    // String name = row.getCell(0).getStringCellValue();
    // cell = row.getCell(10);
    // String ratingText = (cell != null) ? cell.getStringCellValue() : null;
    // cell = row.getCell(12);
    // String comment = (cell != null) ? cell.getStringCellValue() : null;
    //
    // Stock stock = new Stock(symbol, name);
    // CreditRating creditRating = CreditRating.parse(ratingText);
    // if (creditRating != null) {
    // stock.setCreditRating(creditRating);
    // }
    // if (comment != null) {
    // stock.setComment(comment);
    // }
    //
    // config.addStock(stock);
    // System.out.format("Added stock: %s (%s)\n", symbol, name);
    // count++;
    // }
    // }
    // }
    // System.out.format("%d stocks updated\n", count);
    //
    // } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
    // System.err.format("ERROR: Failed to process CCC list: %s\n", e.getMessage());
    // }
    // }
}
