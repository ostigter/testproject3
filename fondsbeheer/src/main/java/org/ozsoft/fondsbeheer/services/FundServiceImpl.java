package org.ozsoft.fondsbeheer.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.entities.FundValue;
import org.ozsoft.fondsbeheer.entities.SmallDate;
import org.ozsoft.fondsbeheer.filestore.FileStore;
import org.ozsoft.fondsbeheer.filestore.FileStoreException;

/**
 * Fund service implementation.
 * 
 * @author Oscar Stigter
 */
public class FundServiceImpl implements FundService {
	
	/** The logger. */
	private static final Logger LOG = Logger.getLogger(FundServiceImpl.class);
	
	/** Default data directory. */ 
	private static final String DEFAULT_DATA_DIR = "./data";
	
    /** The file with the categories. */
	private static final String CATEGORY_FILE = "categories.dat";
    
    /** Regular expression for a fund closing from the internet. */
	private static final Pattern CLOSING_PATTERN = Pattern.compile("^(\\d{6}):(.*)$");

    /** The categories mapping by ID. */
	private final TreeMap<String, Category> categories;
    
	/** The file store. */
	private final FileStore fileStore;
    
	/** The HTTP page reader. */
	private final HttpPageReader pageReader;
    
	/** The data directory. */
	private File dataDir;
	
	/** The category file. */
	private File categoryFile;
	
	/** The number of new categories during the last update. */
	private int noOfNewCategories;
    
	/** The number of new funds during the last update. */
	private int noOfNewFunds;
    
	/** The number of updated funds during the last update. */
	private int noOfUpdatedFunds;

    /**
     * Constructor.
     */
    public FundServiceImpl() {
        categories = new TreeMap<String, Category>();
        fileStore = new FileStore();
        pageReader = new HttpPageReader();
//        pageReader.setUseProxy(true);
//        pageReader.setProxyHost("");
//        pageReader.setProxyPort(8080);
//        pageReader.setProxyUsername("");
//        pageReader.setProxyPassword("");
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#getDataDirectory()
     */
    public String getDataDirectory() {
		return dataDir.getAbsolutePath();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.fondsbeheer.FundService#setDataDirectory(java.lang.String)
	 */
    public void setDataDirectory(String path) {
    	dataDir = new File(path);
    	categoryFile = new File(dataDir, CATEGORY_FILE);
	}

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#start()
     */
    public void start() {
        LOG.debug("Starting");
        try {
        	if (dataDir == null) {
        		setDataDirectory(DEFAULT_DATA_DIR);
        	}
        	fileStore.setDataDir(dataDir.getPath());
            fileStore.start();
        } catch (FileStoreException e) {
        	String msg = "Error starting file store";
        	LOG.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        readCategoryFile();
        LOG.debug("Started");
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#stop()
     */
    public void stop() {
        LOG.debug("Stopping");
        try {
            fileStore.shutdown();
            LOG.debug("Stopped");
        } catch (FileStoreException e) {
        	String msg = "Error shutting down file store";
        	LOG.error(msg, e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#getNoOfCategories()
     */
    public int getNoOfCategories() {
        return categories.size();
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#getCategories()
     */
    public Collection<Category> getCategories() {
        return categories.values();
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#getCategory(java.lang.String)
     */
    public Category getCategory(String categoryId) {
        return categories.get(categoryId);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.services.FundService#addCategory(org.ozsoft.fondsbeheer.entities.Category)
     */
    public void addCategory(Category category) {
    	if (category == null) {
    		throw new IllegalArgumentException("Null category");
    	}
    	String id = category.getId();
    	if (!categories.containsKey(id)) {
    		categories.put(id, category);
    		LOG.info(String.format("Added new category '%s'", category));
            writeCategoryFile();
    	} else {
    		LOG.warn(String.format("Category '%s' already exists", category));
    	}
	}

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#getNoOfFunds()
     */
    public int getNoOfFunds() {
        int noOfFunds = 0;
        for (Category category : categories.values()) {
            noOfFunds += category.getNoOfFunds();
        }
        return noOfFunds;
    }

    public void retrieveFund(Fund fund) {
    	if (fund == null) {
    		throw new IllegalArgumentException("Null fund");
    	}
    	if (LOG.isDebugEnabled()) {
    		LOG.debug(String.format("Retrieving fund '%s'", fund));
    	}
		String id = fund.getId();
    	try {
    		byte[] data = fileStore.retrieve(id);
    		if (data != null) {
	    		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
	    		fund.deserialize(dis);
	    		dis.close();
    		} else {
    			LOG.warn(String.format("Could not find fund with ID '%s'", id));
    		}
    	} catch (FileStoreException e) {
    		LOG.error(String.format("Could not retrieve fund '%s'", fund), e);
    	} catch (IOException e) {
    		LOG.error(String.format("Could not deserialize fund '%s'", fund), e);
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#storeFund(org.ozsoft.fondsbeheer.entities.Fund)
     */
    public void storeFund(Fund fund) {
    	if (fund == null) {
    		throw new IllegalArgumentException("Null fund");
    	}
    	if (LOG.isDebugEnabled()) {
    		LOG.debug(String.format("Storing fund '%s'", fund.getName()));
    	}
    	try {
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		DataOutputStream dos = new DataOutputStream(baos);
    		fund.serialize(dos);
    		byte[] data = baos.toByteArray();
    		dos.close();
    		fileStore.store(fund.getId(), data);
    	} catch (FileStoreException e) {
    		LOG.error(String.format("Could not retrieve fund with ID '%s'", e));
    	} catch (IOException e) {
    		LOG.error(String.format("Could not deserialize fund with ID '%s'", e));
    	}
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

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#updateAll()
     */
    public void updateAll() {
    	LOG.info("Updating all funds");
        for (Category cat : categories.values()) {
            updateFundsInCategory(cat);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.FundService#updateFundsInCategory(org.ozsoft.fondsbeheer.entities.Category)
     */
    public void updateFundsInCategory(Category category) {
        LOG.info(String.format("Updating category '%s'", category));
        for (Fund fund : category.getFunds()) {
            updateFund(fund);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.fondsbeheer.services.FundService#updateFund(org.ozsoft.fondsbeheer.entities.Fund)
     */
    public void updateFund(Fund fund) {
    	if (fund == null) {
    		throw new IllegalArgumentException("Null fund");
    	}
        String id = fund.getId();
        if (fileStore.contains(id)) {
        	retrieveFund(fund);
        }
        LOG.info(String.format("Updating fund '%s'", fund));
        boolean updated = false;
        String uri = String.format("http://www.behr.nl/Beurs/Fondsh/%s.html", id);
        String[] lines = pageReader.read(uri);
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
                    		dateString, fund));
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
        	storeFund(fund);
        }
    }
    
    public int getNoOfNewCategories() {
        return noOfNewCategories;
    }

    public int getNoOfNewFunds() {
        return noOfNewFunds;
    }

    public int getNoOfUpdatedFunds() {
        return noOfUpdatedFunds;
    }
    
    public void resetUpdateCounter() {
        noOfUpdatedFunds = 0;
    }

    public void checkIntegrity() {
        int errors = 0;
        int warnings = 0;
        
        LOG.info("Checking file store integrity...");
        for (String fileId : fileStore.getFileIds()) {
            try {
                fileStore.retrieve(fileId);
            } catch (FileStoreException e) {
                LOG.error(String.format("Could not retrieve fund with ID '%s'", fileId), e);
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
                    LOG.info(String.format("Removed dead fund '%s/%s' with ID '%s'", cat, fund, id));
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
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("Reading category file");
    	}
        categories.clear();
        if (categoryFile.exists() && categoryFile.canRead()) {
            try {
                DataInputStream dis = new DataInputStream(new FileInputStream(categoryFile));
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
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("Writing category file");
    	}
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(categoryFile));
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
