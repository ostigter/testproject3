package sr.projectx.services;

import java.util.Date;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import sr.projectx.entities.AccessLogEntry;
import sr.projectx.entities.LogLevel;
import sr.projectx.entities.AppLogMessage;
import sr.projectx.entities.User;

/**
 * Log service implementation.
 * 
 * @author Oscar Stigter
 */
@ManagedBean(name = "logService", eager = true)
@ApplicationScoped
public class LogServiceImpl implements LogService {

    /** Log. */
    private static final Logger LOG = Logger.getLogger(LogServiceImpl.class);

    /** Entity manager. */
    private final EntityManager em;

    /**
     * Constructor.
     */
    public LogServiceImpl() {
        em = PersistenceService.getEntityManager();
        info("Application started");
    }

    /*
     * (non-Javadoc)
     * 
     * @see sr.projectx.services.LogService#fatal(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void fatal(String message, Object... args) {
        log(LogLevel.FATAL, message, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see sr.projectx.services.LogService#error(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void error(String message, Object... args) {
        log(LogLevel.ERROR, message, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see sr.projectx.services.LogService#warn(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void warn(String message, Object... args) {
        log(LogLevel.WARN, message, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see sr.projectx.services.LogService#info(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void info(String message, Object... args) {
        log(LogLevel.INFO, message, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see sr.projectx.services.LogService#debug(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void debug(String message, Object... args) {
        log(LogLevel.DEBUG, message, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see sr.projectx.services.LogService#trace(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void trace(String message, Object... args) {
        log(LogLevel.TRACE, message, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see sr.projectx.services.LogService#access(sr.projectx.entities.User,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void logAccess(User user, String address, String hostname) {
        AccessLogEntry entry = new AccessLogEntry();
        entry.setDate(new Date());
        entry.setUser(user);
        entry.setAddress(address);
        entry.setHostname(hostname);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(entry);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            String msg = "Could not persist access log entry";
            LOG.error(msg, e);
            throw new PersistenceException(msg, e);
        }
    }

    /**
     * Logs a log message.
     * 
     * @param level
     *            The log level.
     * @param message
     *            The message.
     * @param args
     *            Any optional arguments.
     */
    private void log(LogLevel level, String message, Object... args) {
        AppLogMessage logMessage = new AppLogMessage();
        logMessage.setDate(new Date());
        logMessage.setLevel(level);
        logMessage.setMessage(String.format(message, args));
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(logMessage);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            String msg = "Could not persist application log message";
            LOG.error(msg, e);
            throw new PersistenceException(msg, e);
        }
    }

}
