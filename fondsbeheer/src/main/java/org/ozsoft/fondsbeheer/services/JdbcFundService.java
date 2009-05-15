package org.ozsoft.fondsbeheer.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

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

    public List<Category> findCategories() throws DatabaseException {
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
    
    public Category findCategory(String categoryId) throws DatabaseException {
        return null;
    }
    
    public void storeCategory(Category category) throws DatabaseException {
    }

    public List<Fund> findFunds() throws DatabaseException {
        return null;
    }

    public List<Fund> findFunds(String categoryId) throws DatabaseException {
        return null;
    }
    
    public Fund findFund(String fundId) throws DatabaseException {
        return null;
    }
    
    public void storeFund(Fund fund) throws DatabaseException {
    }

    public List<Closing> findClosings(String fundId) throws DatabaseException {
        return null;
    }

    public void storeClosing(Closing closing) throws DatabaseException {
    }

}
