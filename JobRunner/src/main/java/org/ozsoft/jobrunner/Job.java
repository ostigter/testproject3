package org.ozsoft.jobrunner;

import java.io.Serializable;

public abstract class Job implements Serializable {
    
    protected long id = -1L;
    
    protected JobState state = JobState.CREATED;
    
    protected Throwable errorCause;
    
    private static final long serialVersionUID = -5351211665071336052L;
    
    public final long getId() {
        return id;
    }
    
    /* package */ final void setId(long id) {
        this.id = id;
    }
    
    public final JobState getState() {
        return state;
    }
    
    /* package */ void setState(JobState state) {
        this.state = state;
    }
    
    public final Throwable getErrorCause() {
        return errorCause;
    }
    
    /* package */ void setErrorCause(Throwable errorCause) {
        this.errorCause = errorCause;
    }
    
    @Override
    public String toString() {
        String className = getClass().getName();
        return String.format("Job %d (%s, %s)", id, className, state);
    }
    
    protected abstract void execute() throws JobException;
    
    /* package */ void abort() {
        setState(JobState.ABORTED);
    }
    
}
