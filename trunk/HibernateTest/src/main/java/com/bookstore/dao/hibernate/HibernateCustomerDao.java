package com.bookstore.dao.hibernate;



import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bookstore.dao.CustomerDao;
import com.bookstore.domain.Customer;


/**
 * Hibernate specific DAO implementation for a customer.
 * 
 * @author Oscar Stigter
 *
 */
public class HibernateCustomerDao implements CustomerDao {
	
	
	private static final Logger logger =
			Logger.getLogger(HibernateCustomerDao.class);


	public void create(Customer customer) {
		logger.debug(String.format("Insert customer '%s'", customer));
		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		session.save(customer);
		session.getTransaction().commit();
	}

	
	@SuppressWarnings("unchecked")
	public List<Customer> retrieveAll() {
		logger.debug("Retrieving all customers");
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		List<Customer> customers = null;
		try {
			customers = session.createQuery("from Customer").list();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			String msg = String.format(
					"Error retrieving all customers: %s", e.getMessage());
			logger.error(msg, e);
		}
		
		return customers;
	}


	public Customer retrieveById(long id) {
		logger.debug(String.format("Retrieve customer with ID %d", id));
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		Customer customer = null;
		try {
			customer = (Customer) session.get(Customer.class, new Long(id));
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			String msg = String.format(
					"Error retrieving customer with ID %d: %s",
					id, e.getMessage());
			logger.error(msg, e);
		}
		return customer;
	}

	
	public void update(Customer customer) {
		logger.debug(String.format("Update customer '%s'", customer));
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		try {
			session.update(customer);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			String msg = String.format("Error updating customer '%s': %s",
					customer, e.getMessage());
			logger.error(msg, e);
		}
	}
	
	
	public void delete(Customer customer) {
		logger.debug(String.format("Delete customer '%s'", customer));
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		try {
			session.delete(customer);
		} catch (Exception e) {
			tx.rollback();
			String msg = String.format("Error deleting customer '%s': %s",
					customer, e.getMessage());
			logger.error(msg, e);
		}
	}


}
