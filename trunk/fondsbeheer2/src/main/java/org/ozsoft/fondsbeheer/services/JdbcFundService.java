package org.ozsoft.fondsbeheer.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Closing;
import org.ozsoft.fondsbeheer.entities.Fund;

/**
 * Implementation of the Fund service using JDBC.
 * 
 * @author Oscar Stigter
 */
public class JdbcFundService implements FundService {
    
    private static final Logger LOG = Logger.getLogger(JdbcFundService.class);

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    private static final String HOST = "localhost";
    
    private static final String DATABASE = "fondsbeheer";
    
    private static final String USERNAME = "fondsbeheer";
    
    private static final String PASSWORD = "fondsbeheer";
    
    private static final String CONNECTION_STRING =
            String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
                    HOST, DATABASE, USERNAME, PASSWORD);
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    private static Connection connection;

    public JdbcFundService() throws DatabaseException {
//        LOG.debug("Creating");
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception e) {
            String msg = "Could not instantiate JDBC driver";
            LOG.error(msg, e);
            throw new DatabaseException(msg, e);
        }
//        LOG.debug("Created");
    }
    
    public void connect() throws DatabaseException {
        if (connection == null) {
            try {
//                LOG.debug("Connectng");
                connection = DriverManager.getConnection(CONNECTION_STRING);
//                LOG.debug("Connected");
            } catch (SQLException e) {
                String msg = "Could not connect to the database"; 
                LOG.error(msg, e);
                throw new DatabaseException(msg, e);
            }
        } else {
            String msg = "Already connected";
            LOG.error(msg);
            throw new DatabaseException(msg);
        }
    }
    
    public void close() throws DatabaseException {
        if (connection != null) {
            try {
//                LOG.debug("Closing connection");
                connection.close();
//                LOG.debug("Connection closed");
            } catch (SQLException e) {
                LOG.error("A database error occurred", e);
            } finally {
                connection = null;
            }
        } else {
            String msg = "Connection already closed";
            LOG.error(msg);
            throw new DatabaseException(msg);
        }
    }

    public List<Category> getCategories() throws DatabaseException {
        checkConnected();
        List<Category> categories = new LinkedList<Category>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT id, name FROM Category");
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                Category category = new Category();
                category.setId(id);
                category.setName(name);
                categories.add(category);
            }
        } catch (SQLException e) {
            String msg = "Could not retrieve categories";
            LOG.error(msg, e);
            throw new DatabaseException(msg, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOG.error("Could not close result set", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.error("Could not close statement", e);
                }
            }
        }
        return categories;
    }
    
    public Category getCategory(String categoryId) throws DatabaseException {
        checkConnected();
        return null;
    }
    
    public void storeCategory(Category category) throws DatabaseException {
        checkConnected();
    }

    public List<Fund> getFunds() throws DatabaseException {
        checkConnected();
        return null;
    }

    public List<Fund> getFunds(String categoryId) throws DatabaseException {
        checkConnected();
        List<Fund> funds = new LinkedList<Fund>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String query = String.format(
                    "SELECT id, name FROM Fund WHERE categoryId = '%s'", categoryId);
            stmt = connection.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                Fund fund = new Fund();
                fund.setId(id);
                fund.setName(name);
                fund.setCategoryId(categoryId);
                funds.add(fund);
            }
        } catch (Exception e) {
            String msg = "Could not retrieve funds for category";
            LOG.error(msg, e);
            throw new DatabaseException(msg, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOG.error("Could not close result set", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.error("Could not close statement", e);
                }
            }
        }
        return funds;
    }
    
    public Fund getFund(String fundId) throws DatabaseException {
        checkConnected();
        Fund fund = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String query = String.format(
                    "SELECT id, name FROM Fund WHERE fundId = '%s'", fundId);
            stmt = connection.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String categoryId = rs.getString("categoryId");
                fund = new Fund();
                fund.setId(id);
                fund.setName(name);
                fund.setCategoryId(categoryId);
                break;
            }
        } catch (SQLException e) {
            String msg = String.format("Could not retrieve fund with ID '%s'", fundId);
            LOG.error(msg, e);
            throw new DatabaseException(msg, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOG.error("Could not close result set", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.error("Could not close statement", e);
                }
            }
        }
        return fund;
    }
    
    public void storeFund(Fund fund) throws DatabaseException {
        checkConnected();
    }

    public List<Closing> getClosings(String fundId) throws DatabaseException {
        checkConnected();
        return null;
    }

    public double getClosingPrice(String fundId, Date date) throws DatabaseException {
        checkConnected();
        double price = -1.0;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT price FROM Closing WHERE fundId = '%s' AND date = '%s'");
            while (rs.next()) {
                price = rs.getDouble(1);
                break;
            }
        } catch (SQLException e) {
            String msg = "Could not retrieve closing price";
            LOG.error(msg, e);
            throw new DatabaseException(msg, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOG.error("Could not close result set", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.error("Could not close statement", e);
                }
            }
        }
        return price;
    }

    public void storeClosing(Closing closing) throws DatabaseException {
        checkConnected();
        String fundId = closing.getFundId();
        Date date = closing.getDate();
        double price = closing.getPrice();
        Statement stmt = null;
        double storedPrice = getClosingPrice(fundId, date);
        String dateString = DATE_FORMAT.format(date); 
        if (storedPrice == -1.0) {
            // Add.
            String query = String.format(Locale.US,
                    "INSERT INTO Closing (id, fundId, date, price) VALUES (NULL, '%s', '%s', %f)",
                    fundId, dateString, price);
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                String msg = "Could not insert closing";
                LOG.error(msg, e);
                throw new DatabaseException(msg, e);
            } finally {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.error("Could not close statement", e);
                }
            }
        } else if (price != storedPrice) {
            // Update.
            String query = String.format(Locale.US,
                    "UPDATE Closing SET price = %f WHERE fundId = '%s' AND date = '%s'",
                    price, fundId, dateString);
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                String msg = "Could not update closing";
                LOG.error(msg, e);
                throw new DatabaseException(msg, e);
            } finally {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.error("Could not close statement", e);
                }
            }
        } else {
            // Already up-to-date. 
        }
    }
    
    private void checkConnected() throws DatabaseException {
        if (connection == null) {
            throw new DatabaseException("Not connected"); 
        }
    }

}
