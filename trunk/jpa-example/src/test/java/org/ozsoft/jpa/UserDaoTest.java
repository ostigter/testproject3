package org.ozsoft.jpa;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.jpa.dao.UserDao;
import org.ozsoft.jpa.dao.UserDaoImpl;
import org.ozsoft.jpa.domain.User;

/**
 * Test suite for the User DAO.
 * 
 * @author Oscar Stigter
 */
public class UserDaoTest {

	@Test
	public void testBasicPersistence() throws Exception {
		UserDao userDao = new UserDaoImpl();

		User user = new User();
		user.setUsername("alice");
		user.setPassword("secret");

		userDao.create(user);
		long id = user.getId();

		user = userDao.retrieve(id);
		Assert.assertNotNull(user);
		Assert.assertEquals("alice", user.getUsername());
		Assert.assertEquals("secret", user.getPassword());

		user.setPassword("guessme");
		userDao.update(user);

		user = userDao.retrieve(id);
		Assert.assertNotNull(user);
		Assert.assertEquals("alice", user.getUsername());
		Assert.assertEquals("guessme", user.getPassword());

		userDao.delete(user);
	}

}
