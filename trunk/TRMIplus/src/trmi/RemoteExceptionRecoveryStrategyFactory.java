package trmi;

/**
 * Creates <code>RemoteExceptionRecoveryStrategy</code>s for use by {@link
 * Naming}.
 *
 * @see RemoteExceptionRecoveryStrategy
 * @author Guy Gur-Ari
 */
public interface RemoteExceptionRecoveryStrategyFactory {
	
	/**
	 * Returns an error recovery strategy for the object specified by the given
	 * object name.
	 *
	 * @param name The remote object's URL.
	 * @param exposedIfaces The remote object's exposed interfaces.
	 *
	 * @return An error recovery strategy.
	 */
	@SuppressWarnings("unchecked")
	public RemoteExceptionRecoveryStrategy getRecoveryStrategy(
			String name, Class[] exposedIfaces);

	/**
	 * Returns an error recovery strategy for a nameless object.
	 *
	 * @param exposedIfaces The remote object's exposed interfaces.
	 *
	 * @return An error recovery strategy.
	 */
	@SuppressWarnings("unchecked")
	public RemoteExceptionRecoveryStrategy getRecoveryStrategy(
			Class[] exposedIfaces);

}
