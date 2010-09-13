package org.ozsoft.task;

public interface TaskListener {
    
    void taskStarted();
    
    void taskUpdated(int progress);
    
    void taskCompleted();
    
    void taskCanceled();

}
