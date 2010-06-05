package sr.projectx.services;

public interface LogService {
    
    void fatal(String message, Object... args);
    
    void error(String message, Object... args);
    
    void warn(String message, Object... args);
    
    void info(String message, Object... args);
    
    void debug(String message, Object... args);
    
    void trace(String message, Object... args);
    
}
