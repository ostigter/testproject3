package org.ozsoft.jpa;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.jpa.entities.User;
import org.ozsoft.jpa.services.UserService;
import org.ozsoft.jpa.services.UserServiceImpl;

/**
 * Test suite for the User service.
 * 
 * @author Oscar Stigter
 */
public class UserServiceImplTest {

    @Test
    public void testBasicPersistence() throws Exception {
        UserService userDao = new UserServiceImpl();

        // Create a user.
        User user = new User();
        user.setUsername("alice");
        user.setPassword("secret");
        userDao.create(user);

        // Remember user ID.
        long id = user.getId();

        // Retrieve user.
        user = userDao.retrieve(id);
        Assert.assertNotNull(user);
        Assert.assertEquals("alice", user.getUsername());
        Assert.assertEquals("secret", user.getPassword());

        // Update user.
        user.setPassword("guessme");
        userDao.update(user);

        // Check update.
        user = userDao.retrieve(id);
        Assert.assertNotNull(user);
        Assert.assertEquals("alice", user.getUsername());
        Assert.assertEquals("guessme", user.getPassword());

        // Delete user.
        userDao.delete(user);
    }

}
