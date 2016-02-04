package org.ozsoft.task;

/**
 * Listener for task status events.
 * 
 * @author Oscar Stigter
 */
public interface TaskListener {

    /**
     * Indicates that the task is started.
     */
    void taskStarted();

    /**
     * Indicates that the task's progress has been updated.
     * 
     * @param progress
     *            The progress as percentage of completion (0 to 100).
     */
    void taskUpdated(int progress);

    /**
     * Indicates that the task is completed successfully.
     */
    void taskCompleted();

    /**
     * Indicates that the task is canceled.
     */
    void taskCanceled();
}
