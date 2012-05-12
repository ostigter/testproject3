package org.ozsoft.jobrunner;

public interface JobRunner {
    
    <T extends Job> T createJob(Class<T> clazz) throws JobException;
    
    <T extends Job> T getJob(long id) throws JobException;
    
    void scheduleJob(Job job) throws JobException;
    
    void abortJob(long id) throws JobException;
    
}
