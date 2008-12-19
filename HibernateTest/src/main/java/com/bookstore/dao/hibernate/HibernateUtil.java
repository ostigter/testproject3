package com.bookstore.dao.hibernate;


import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * Hibernate specific utility class.
 * 
 * @author Oscar Stigter
 */
public class HibernateUtil {
	
	
	/** Logger. */
	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
	
	/** SessionFactory singleton. */
	private static final SessionFactory sessionFactory;
    

    /**
     * Instantiates the SessionFactory from hibernate.cfg.xml.
     */
    static {
    	logger.debug("Create SessionFactory");
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
        	throw new RuntimeException(
        			"Error creating SessionFactory: " + e.getMessage());
        }
    }
    
    
    /**
     * Returns the current session.
     * 
     * @return  the current session
     */
    public static Session getCurrentSession() {
    	return sessionFactory.getCurrentSession();
    }


}
