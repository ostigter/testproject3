package org.ozsoft.taskman.services;

import java.util.List;

import org.ozsoft.taskman.domain.Task;

/**
 * Service for managing tasks.
 * 
 * @author Oscar Stigter
 */
public interface TaskService {
    
    /**
     * Creates a task.
     * 
     * @param task
     *            The task.
     */
    void create(Task task);
    
    /**
     * Retrieves a task by its ID.
     * 
     * @param taskId
     *            The task's ID.
     * 
     * @return The task.
     */
    Task retrieveById(long taskId);
    
    /**
     * Retrieves the tasks of a specific user.
     * 
     * @param userId
     *            The user's ID.
     * 
     * @return The tasks.
     */
    List<Task> retrieveByUser(long userId);
    
    /**
     * Updates a task.
     * 
     * @param task
     *            The task.
     */
    void update(Task task);
    
}
