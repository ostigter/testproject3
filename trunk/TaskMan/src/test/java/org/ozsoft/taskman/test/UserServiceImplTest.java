package org.ozsoft.taskman.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.taskman.domain.Status;
import org.ozsoft.taskman.domain.Task;
import org.ozsoft.taskman.domain.User;
import org.ozsoft.taskman.services.TaskService;
import org.ozsoft.taskman.services.TaskServiceImpl;
import org.ozsoft.taskman.services.UserService;
import org.ozsoft.taskman.services.UserServiceImpl;

/**
 * Test suite for the User service.
 *  
 * @author Oscar Stigter
 */
public class UserServiceImplTest {
    
    @Test
    public void testBasicPersistence() throws Exception {
	UserService userService = new UserServiceImpl();
//	TaskService taskService = new TaskServiceImpl();
	
	User user = new User();
	user.setUsername("alice");
	user.setPassword("secret");
	
	userService.create(user);
	long id = user.getId();
	
	user = userService.retrieve(id);
	Assert.assertNotNull(user);
	Assert.assertEquals("alice", user.getUsername());
	Assert.assertEquals("secret", user.getPassword());
	
	// Create task.
	Task task = new Task();
	task.setSummary("Mowing the lawn");
	task.setStatus(Status.OPEN);
//	taskService.create(task);
//	long taskId = task.getId();

	// Add task to user.
	Set<Task> tasks = user.getTasks();
	Assert.assertNull(tasks);
	tasks = new HashSet<Task>();
	tasks.add(task);
	user.setTasks(tasks);

	user.setPassword("guessme");
	userService.update(user);
	
	user = userService.retrieve(id);
	Assert.assertNotNull(user);
	Assert.assertEquals("alice", user.getUsername());
	Assert.assertEquals("guessme", user.getPassword());
	
	userService.delete(user);
    }

}
