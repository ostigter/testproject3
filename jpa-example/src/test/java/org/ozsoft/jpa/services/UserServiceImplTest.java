package org.ozsoft.jpa.services;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.jpa.entities.Project;
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
        UserService userService = new UserServiceImpl();

        // Create a user.
        User user = new User();
        user.setUsername("alice");
        user.setPassword("secret");
        userService.create(user);

        // Remember user ID.
        long id = user.getId();

        // Retrieve user.
        user = userService.retrieve(id);
        Assert.assertNotNull(user);
        Assert.assertEquals("alice", user.getUsername());
        Assert.assertEquals("secret", user.getPassword());

        // Update user.
        user.setPassword("guessme");
        userService.update(user);

        // Check update.
        user = userService.retrieve(id);
        Assert.assertNotNull(user);
        Assert.assertEquals("alice", user.getUsername());
        Assert.assertEquals("guessme", user.getPassword());
        
        // Add projects.
        Project project = new Project();
        project.setName("Foo");
        user.addProject(project);
        project = new Project();
        project.setName("Zeta");
        user.addProject(project);
        project = new Project();
        project.setName("Bar");
        user.addProject(project);
        userService.update(user);
        
        // Check update.
        user = userService.retrieve(id);
        Project[] projects = user.getProjects().toArray(new Project[0]);
        Assert.assertEquals(3, projects.length);
        Assert.assertEquals("Bar", projects[0].getName());
        Assert.assertEquals(user, projects[0].getUser());
        Assert.assertEquals("alice", projects[0].getUser().getUsername());
        Assert.assertEquals("Foo", projects[1].getName());
        Assert.assertEquals("Zeta", projects[2].getName());
        
        // Delete project.
        user.removeProject(projects[1]);
        userService.update(user);

        // Check update.
        user = userService.retrieve(id);
        Assert.assertEquals(2, user.getProjects().size());

        // Delete user.
        userService.delete(user);
    }

}
