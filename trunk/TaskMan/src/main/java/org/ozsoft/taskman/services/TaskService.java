package org.ozsoft.taskman.services;

import java.util.List;

import org.ozsoft.taskman.domain.Task;

/**
 * Service for managing tasks.
 * 
 * @author Oscar Stigter
 */
public interface TaskService {
    
    Task createTask();
    
    void saveTask(Task task);

    List<Task> getTasks(long owner);
    
}
