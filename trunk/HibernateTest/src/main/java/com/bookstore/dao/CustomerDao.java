package com.bookstore.dao;


import java.util.List;

import com.bookstore.domain.Customer;


/**
 * DAO interface for a customer.
 * 
 * @author Oscar Stigter
 */
public interface CustomerDao {
	
	
	void create(Customer customer);
	
	List<Customer> retrieveAll();
	
	Customer retrieveById(long id);
	
	void update(Customer customer);
	
	void delete(Customer customer);


}
