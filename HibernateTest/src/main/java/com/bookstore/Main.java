package com.bookstore;



import java.util.List;

import org.apache.log4j.Logger;

import com.bookstore.dao.CustomerDao;
import com.bookstore.dao.hibernate.HibernateCustomerDao;
import com.bookstore.domain.Customer;


/**
 * Test driver for the BookStore application with Hibernate.
 * 
 * @author Oscar Stigter
 */
public class Main {


	private static final Logger logger = Logger.getLogger(Main.class);

	
	public static void main(String[] args) throws Exception {
		logger.info("Started");
		
		// Get the customer DAO (normally injected with Spring).
		CustomerDao customerDao = new HibernateCustomerDao();
		
		// Create new customer.
		Customer customer = new Customer();
		customer.setLastName("Klaassen");
		customer.setFirstName("Jan");
		customerDao.create(customer);
		
		// Retrieve all customers.
		List<Customer> customers = customerDao.retrieveAll();
		for (Customer c : customers) {
			logger.info(String.format("Retrieved customer: '%s'", c));
		}
		
		// Update customer.
		customer.setLastName("Janssen");
		customerDao.update(customer);
		
		// Retrieve customer by ID.
		int id = 1;
		customer = customerDao.retrieveById(id);
		if (customer != null) {
			logger.info(String.format("Retrieved customer by ID: '%s'", customer));
		} else {
			logger.error(String.format("Customer by ID %d not found", id));
		}
		
		// Delete customer.
		customerDao.delete(customer);

		logger.info("Finished");
	}


}
