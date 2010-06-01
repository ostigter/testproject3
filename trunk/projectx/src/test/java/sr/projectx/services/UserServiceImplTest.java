package sr.projectx.services;

import junit.framework.Assert;

import org.junit.Test;

import sr.projectx.entities.User;

/**
 * Test suite for the User service.
 * 
 * @author Oscar Stigter
 */
public class UserServiceImplTest {
	
    @Test
    public void persistence() throws Exception {
        UserService userService = new UserServiceImpl();
        
        // Create user.
        String username = "alice";
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail("alice@somewhere.net");
        userService.create(user);
        
        // Retrieve user.
        user = userService.retrieve(username);
        Assert.assertNotNull(user);
        long id = user.getId();
        Assert.assertEquals(username, user.getUsername());
        Assert.assertEquals("secret", user.getPassword());
        Assert.assertEquals("alice@somewhere.net", user.getEmail());
        
        // Update user.
        user.setPassword("guessme");
        userService.update(user);
        
        // Retrieve updated user.
        user = userService.retrieve(id);
        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getUsername());
        Assert.assertEquals("guessme", user.getPassword());
    
        // Delete user.
        userService.delete(user);
        user = userService.retrieve(username);
        Assert.assertNull(user);
    }

}
