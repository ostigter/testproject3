package org.ozsoft.taskman.test;

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
public class TaskServiceImplTest {
    
    @Test
    public void testBasicPersistence() throws Exception {
	UserService userService = new UserServiceImpl();
	TaskService taskService = new TaskServiceImpl();
	
	User user = new User();
	user.setUsername("mike");
	user.setPassword("secret");
	userService.create(user);
	
	Task task = new Task();
	task.setSummary("Mowing the lawn");
//	task.setUser(user);
	task.setStatus(Status.OPEN);
	taskService.create(task);
	long taskId = task.getId();
	
	task = taskService.retrieveById(taskId);
	Assert.assertNotNull(task);
	Assert.assertEquals("Mowing the lawn", task.getSummary());
//	Assert.assertEquals(user, task.getUser());
	Assert.assertEquals(Status.OPEN, task.getStatus());
	
	task.setStatus(Status.COMPLETED);
	taskService.update(task);
	
	task = taskService.retrieveById(taskId);
	Assert.assertNotNull(task);
	Assert.assertEquals("Mowing the lawn", task.getSummary());
//	Assert.assertEquals(user, task.getUser());
	Assert.assertEquals(Status.COMPLETED, task.getStatus());
    }

}
