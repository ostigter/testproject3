package trmi;

import java.rmi.RemoteException;


/**
 * Strategy for recovering from <code>RemoteException</code>s while invoking
 * remote methods. Users can implement this interface and a corresponding
 * factory, then use them by calling {@link
 * trmi.Naming#setRecoveryStrategyFactory(
 * RemoteExceptionRecoveryStrategyFactory)}.<p>
 *
 * Strategies can call {@link trmi.Naming#lookupRemoteObjectWrapper(String)} to
 * lookup the <code>RemoteObjectWrapper</code>s that they are required to
 * return.
 *
 * @see RemoteExceptionRecoveryStrategyFactory
 * @author Guy Gur-Ari
 */
public interface RemoteExceptionRecoveryStrategy extends java.io.Serializable {
	/**
	 * Called by <code>StubInvocationHandler</code> when a
	 * <code>RemoteException</code> is thrown while invoking a remote method.
	 * Implementations are expected to either provide a new, valid
	 * <code>RemoteObjectWrapper</code> to replace the old wrapper, or throw a
	 * <code>RuntimeException</code> if this cannot be done.
	 *
	 * @param currentWrapper The currently used wrapper the caused the
	 * exception.
	 *
	 * @param ex The thrown exception.
	 *
	 * @return A <code>RemoteObjectWrapper</code> that replaces the current
	 * wrapper (may be the same object).
	 *
     * @exception RemoteExceptionRecoveryStrategyException If a replacement
     * wrapper cannot be provided.  This exception will be propagated to the
     * user.
	 */
	public RemoteObjectWrapper recoverFromRemoteException(
			RemoteObjectWrapper currentWrapper,
			RemoteException ex) throws RemoteRuntimeException;
}
