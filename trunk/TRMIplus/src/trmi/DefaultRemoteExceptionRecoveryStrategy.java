package trmi;

import java.rmi.RemoteException;

/**
 * TRMI+ a remote exception occurs. The default error recovery used by
 * {@link Naming}. When a remote exception occurs, a certain timeout is waited,
 * after which the strategy attempts to lookup the object again. If it succeeds,
 * the newly looked up object it returned. Otherwise, the current object is
 * returned. This goes on forever. TRMI+ : Changed the default to just throw a
 * RemoteRuntimeException when a remote exception occurs.
 * 
 * @author Guy Gur-Ari
 */
public class DefaultRemoteExceptionRecoveryStrategy implements RemoteExceptionRecoveryStrategy {

    private static final long serialVersionUID = 1L;

    protected String name;

    protected Class<?>[] exposedIfaces;

    /**
     * Delay to wait after a remote exception occurred, and before trying to
     * recover from it, in milliseconds.
     */
    protected int delay;

    protected static final int DEFAULT_DELAY = 1000;
    /**
     * Constructs the object.
     * 
     * @param name
     *            The remote object's URL. May be <code>null</code>.
     * @param exposedIfaces
     *            The remote object's exposed interfaces.
     * @param recoveryDelay
     *            Delay to wait after a remote exception occurred, and before
     *            trying to recover from it, in milliseconds.
     */
    public DefaultRemoteExceptionRecoveryStrategy(String name, Class<?>[] exposedIfaces, int recoveryDelay) {
        this.name = name;
        this.exposedIfaces = exposedIfaces.clone();
        this.delay = recoveryDelay;
    }

    /**
     * Constructs the object. Similar to previous constructor, but uses a
     * default recovery delay.
     */
    public DefaultRemoteExceptionRecoveryStrategy(String name, Class<?>[] exposedIfaces) {
        this(name, exposedIfaces, DEFAULT_DELAY);
    }

    /**
     * Method operation is described in the class documentation.
     */
    public RemoteObjectWrapper recoverFromRemoteException(RemoteObjectWrapper currentWrapper, RemoteException ex) {
        throw new RemoteRuntimeException();
        // TRMI+ sleep();
        // TRMI+ return lookupRemoteObject(currentWrapper, ex);
    }

    protected void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
    }

    protected RemoteObjectWrapper lookupRemoteObject(RemoteObjectWrapper currentWrapper, RemoteException ex) {
        if (name == null) {
            return currentWrapper;
        }

        try {
            RemoteObjectWrapper wrapper = trmi.Naming.lookupRemoteObjectWrapper(name);
            return wrapper;
        } catch (Exception e) {
            return currentWrapper;
        }
    }

}
