package org.ozsoft.jobrunner;

public class JobException extends Exception {
    
    private static final long serialVersionUID = 1876132481195659938L;

    public JobException(String message) {
        super(message);
    }
    
    public JobException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
