package org.ozsoft.taskman.test;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.taskman.domain.Status;
import org.ozsoft.taskman.domain.Task;
import org.ozsoft.taskman.domain.User;
import org.ozsoft.taskman.services.UserService;
import org.ozsoft.taskman.services.UserServiceImpl;

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
	User user = new User();
	user.setUsername("alice");
	user.setPassword("secret");
	userService.create(user);
	long id = user.getId();
	
	// Retrieve user.
	user = userService.retrieve(id);
	Assert.assertNotNull(user);
	Assert.assertEquals("alice", user.getUsername());
	Assert.assertEquals("secret", user.getPassword());
	Assert.assertNull(user.getTasks());
	
	// Add task.
	Task task = new Task();
	task.setSummary("Mowing the lawn");
	task.setStatus(Status.OPEN);
	user.addTask(task);

	// Edit user.
	user.setPassword("guessme");
	userService.update(user);
	
	// Retrieve user.
	user = userService.retrieve(id);
	Assert.assertNotNull(user);
	Assert.assertEquals("alice", user.getUsername());
	Assert.assertEquals("guessme", user.getPassword());
	Set<Task> tasks = user.getTasks();
	Assert.assertNotNull(tasks);
	Assert.assertEquals(1, tasks.size());
	task = tasks.toArray(new Task[0])[0];
	Assert.assertEquals("Moving the lawn", task.getSummary());
	Assert.assertEquals(Status.OPEN, task.getStatus());
	
	// Delete user.
	userService.delete(user);
    }

}
