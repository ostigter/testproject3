package sr.projectx.services;

import sr.projectx.entities.User;

public interface LogService {

    void fatal(String message, Object... args);

    void error(String message, Object... args);

    void warn(String message, Object... args);

    void info(String message, Object... args);

    void debug(String message, Object... args);

    void trace(String message, Object... args);

    void logAccess(User user, String address, String hostname);

}
