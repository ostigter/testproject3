package org.ozsoft.fondsbeheer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.FundValue;
import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.entities.SmallDate;
import org.ozsoft.fondsbeheer.filestore.FileStore;
import org.ozsoft.fondsbeheer.filestore.FileStoreException;

/**
 * Manager for the categories and funds.
 * 
 * @author Oscar Stigter
 */
public class FundService {
	
	private static final Logger LOG = Logger.getLogger(FundService.class);
	
    private static final File CATEGORY_FILE = new File("data/categories.dat");
    
    private static final Pattern CLOSING_PATTERN = Pattern.compile("^(\\d{6}):(.*)$");

    private final TreeMap<String, Category> categories;
    
    private final FileStore fileStore;
    
    private final HttpPageReader pageReader;
    
    private int noOfNewCategories;
    
    private int noOfNewFunds;
    
    private int noOfUpdatedFunds;

    public FundService() {
        categories = new TreeMap<String, Category>();
        fileStore = new FileStore();
        pageReader = new HttpPageReader();
//        pageReader.setUseProxy(true);
//        pageReader.setProxyHost("");
//        pageReader.setProxyPort(8080);
//        pageReader.setProxyUsername("");
//        pageReader.setProxyPassword("");
    }

    public void start() {
        LOG.debug("Starting");
        try {
            fileStore.start();
        } catch (FileStoreException e) {
        	String msg = "Error starting file store";
        	LOG.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        readCategoryFile();
        LOG.debug("Started");
    }
    
    public void shutdown() {
        LOG.debug("Shutting down");
        try {
            fileStore.shutdown();
            LOG.debug("Shut down");
        } catch (FileStoreException e) {
        	String msg = "Error shutting down file store";
        	LOG.error(msg, e);
        }
    }

    public Category[] getCategories() {
        return categories.values().toArray(new Category[0]);
    }

    public Category getCategory(String categoryId) {
        return categories.get(categoryId);
    }

    public int getNoOfCategories() {
        return categories.size();
    }

    public int getNoOfFunds() {
        int noOfFunds = 0;
        for (Category category : categories.values()) {
            noOfFunds += category.getNoOfFunds();
        }
        return noOfFunds;
    }

    public void updateCategories() {
        LOG.info("Updating categories");
        
        noOfNewCategories = 0;
        noOfNewFunds = 0;
        
        LOG.info("Updating NL categories");
        updateCategoriesFromPage("http://www.behr.nl/Beurs/H/index_nl..2.html");
        LOG.info("Updating EU categories");
        updateCategoriesFromPage("http://www.behr.nl/Beurs/H/index_eu..2.html");
        LOG.info("Updating WRLD categories");
        updateCategoriesFromPage("http://www.behr.nl/Beurs/H/index_wrld..2.html");
        
        writeCategoryFile();

        LOG.info("Categories updated");
    }

    public int getNoOfNewCategories() {
        return noOfNewCategories;
    }

    public int getNoOfNewFunds() {
        return noOfNewFunds;
    }

    public void resetUpdateCounter() {
        noOfUpdatedFunds = 0;
    }

    public void updateAll() {
    	LOG.info("Updating all funds");
        for (Category cat : categories.values()) {
            updateFundsInCategory(cat);
        }
    }

    public void updateFundsInCategory(Category cat) {
        LOG.info(String.format("Updating category '%s'", cat.getName()));
        for (Fund fund : cat.getFunds()) {
            updateFund(fund);
        }
    }

    public void updateFund(Fund fund) {
        LOG.info(String.format("Updating fund '%s'", fund.getName()));
        
        // Create fund with stored data.
        byte[] bytes = null;
        try {
            bytes = fileStore.retrieve(fund.getId());
            fund = new Fund(bytes);
        } catch (FileStoreException e) {
            LOG.info("Fund added: " + fund.getName());
        }

        // Update fund using online service.
        boolean updated = false;
        String url = String.format("http://www.behr.nl/Beurs/Fondsh/%s.html", fund.getId());
        String[] lines = pageReader.read(url);
        for (String line : lines) {
            Matcher m = CLOSING_PATTERN.matcher(line);
            if (m.matches()) {
                String dateString = m.group(1);
                SmallDate date = null;
                try {
                    date = SmallDate.parseDate(dateString);
                } catch (Exception ex) {
                    LOG.error(String.format(
                    		"Could not parse date '%s' for fund '%s'",
                    		dateString, fund.getName()));
                }
                String valueString = m.group(2);
                valueString = valueString.replaceFirst(",", ".").replaceAll("X", "").replaceAll("\\*", "");
                float value = -1.0f;
                try {
                    value = Float.parseFloat(valueString);
                } catch (Exception ex) {
                    LOG.error(String.format(
                            "Could not parse closing value '%s' for fund '%s' on %s",
                            valueString, fund.getId(), date));
                }
                if (date != null && value != -1.0f && fund.addValue(new FundValue(date, value))) {
                    if (!updated) {
                        updated = true;
                        noOfUpdatedFunds++;
                    }
                }
            }
        }
        if (updated) {
            try {
                fileStore.store(fund.getId(), fund.getBytes());
            } catch (FileStoreException e) {
                LOG.error(String.format("Could not store fund '%s'", fund.getName()));
            }
        }
    }
    
    public int getNoOfUpdatedFunds() {
        return noOfUpdatedFunds;
    }
    
    public void checkIntegrity() {
        int errors = 0;
        int warnings = 0;
        
        LOG.info("Checking file store integrity...");
        for (String fileId : fileStore.getFileIds()) {
            try {
                fileStore.retrieve(fileId);
            } catch (FileStoreException e) {
                LOG.error(String.format("Could not retrieve file '%s'", fileId), e);
                errors++;
            }
        }
        
        LOG.info("Checking for dead funds...");
        for (Category cat : categories.values()) {
            for (Fund fund : cat.getFunds()) {
                if (!fileStore.contains(fund.getId())) {
                    LOG.warn(String.format(
                    		"Could not find fund '%s/%s' with ID '%s'",
                    		cat.getName(), fund.getName(), fund.getId()));
                    warnings++;
                }
            }
        }
        LOG.info("Check complete");
        if (errors > 0) {
            LOG.info("Errors found:   " + errors);
        }
        if (warnings > 0) {
            LOG.info("Warnings found: " + warnings);
        }
    }
    
    public void purgeDeadFunds() {
        LOG.info("Purging dead funds...");
        boolean purged = false;
        for (Category cat : categories.values()) {
            for (Fund fund : cat.getFunds()) {
                String id = fund.getId();
                if (!fileStore.contains(id)) {
                    cat.removeFund(id);
                    purged = true;
                    LOG.info("Removed dead fund '" + cat.getName()
                            + " / " + fund.getName() + "' ('" + id + "').");
                }
            }
        }
        if (purged) {
            writeCategoryFile();
        }
    }
    
    public void printDiskUsage() {
    	fileStore.printSizeInfo();
    }

    private void readCategoryFile() {
//        LOG.info("FundService: Reading category file");
        categories.clear();
        if (CATEGORY_FILE.exists() && CATEGORY_FILE.canRead()) {
            try {
                DataInputStream dis =
                        new DataInputStream(new FileInputStream(CATEGORY_FILE));
                int noOfCategories = dis.readInt();
                for (int i = 0; i < noOfCategories; i++) {
                    String catId = dis.readUTF();
                    String catName = dis.readUTF();
                    Category category = new Category(catId, catName);
                    int noOfFunds = dis.readInt();
                    for (int j = 0; j < noOfFunds; j++) {
                        String fundId = dis.readUTF();
                        String fundName = dis.readUTF();
                        category.addFund(new Fund(fundId, fundName));
                    }
                    categories.put(catId, category);
                }
                dis.close();
            } catch (IOException e) {
                LOG.error("Could not read categories file", e);
            }
        }
    }
    
    private void writeCategoryFile() {
//        LOG.info("FundService: Writing category file");
        try {
            DataOutputStream dos =
                    new DataOutputStream(new FileOutputStream(CATEGORY_FILE));
            dos.writeInt(categories.size());
            for (Category cat : categories.values()) {
                dos.writeUTF(cat.getId());
                dos.writeUTF(cat.getName());
                dos.writeInt(cat.getNoOfFunds());
                for (Fund fund : cat.getFunds()) {
                    dos.writeUTF(fund.getId());
                    dos.writeUTF(fund.getName());
                }
            }
            dos.close();
        } catch (IOException e) {
            LOG.error("Could not write categories file", e);
        }
    }
    
    private void updateCategoriesFromPage(String url) {
        String[] lines = pageReader.read(url);
        if (lines != null) {
            String categoryIdPattern = "<A NAME=\"";
            String categoryNamePattern = "<H2>";
            String fundPattern = "<LI><A target=_top href=";
            String categoryId = null;
            String categoryName = null;
            Category category = null;
            String fundId = null;
            String fundName = null;
            for (String line : lines) {
                // Look for category ID.
                int aPos = line.indexOf(categoryIdPattern);
                if (aPos != -1) {
                    int endPos = aPos + categoryIdPattern.length();
                    int quotPos = line.indexOf('\"', endPos);
                    categoryId = line.substring(endPos, quotPos);
                } else {
                    // Look for category name.
                    int h2Pos = line.indexOf(categoryNamePattern);
                    if (h2Pos != -1) {
                        int gtPos = line.indexOf('>', h2Pos);
                        int ltPos = line.indexOf('<', gtPos);
                        categoryName = line.substring(gtPos + 1, ltPos);
                        if (!categories.containsKey(categoryId)) {
                            category = new Category(categoryId, categoryName);
                            categories.put(categoryId, category);
                            noOfNewCategories++;
                            LOG.info("Category added: '" + categoryName + "'");
                        }
                    } else {
                        // Look for fund.
                        int fundPos = line.indexOf(fundPattern);
                        if (fundPos != -1) {
                            int endPos = fundPos + fundPattern.length();
                            int gtPos = line.indexOf('>', endPos);
                            fundId = line.substring(endPos, gtPos);
                            fundId = fundId.replaceFirst(".html", "");
                            fundName = getXmlText(line);
                            if (category != null && category.getFund(fundId) == null) {
                                category.addFund(new Fund(fundId, fundName));
                                noOfNewFunds++;
                                LOG.info("Fund added:    '" + categoryName + " / " + fundName + "'");
                            }
                        }
                    }
                }
            }
        }
    }

    private static String getXmlText(String s) {
        StringBuilder sb = new StringBuilder();
        boolean inElement = false;
        for (Character c : s.trim().toCharArray()) {
            if (c == '<') {
                inElement = true;
            } else if (c == '>') {
                inElement = false;
            } else if (!inElement) {
                sb.append(c);
            } else {
                // Skip characters in element tag. 
            }
        }
        return sb.toString();
    }

}
