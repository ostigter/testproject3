package org.ozsoft.taskman.test;

import java.util.List;

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
        String username = "alice";
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        userService.create(user);

        // Retrieve user.
        user = userService.retrieve(username);
        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getUsername());
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
        user = userService.retrieve(username);
        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getUsername());
        Assert.assertEquals("guessme", user.getPassword());
        List<Task> tasks = user.getTasks();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(1, tasks.size());
        task = tasks.toArray(new Task[0])[0];
        Assert.assertEquals("Mowing the lawn", task.getSummary());
        Assert.assertEquals(Status.OPEN, task.getStatus());

        // Delete user.
        userService.delete(user);
    }

}
