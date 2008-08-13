package trmi;

/**
 * Creates {@link RemoteExceptionRecoveryStrategy}s.
 * 
 * @author Guy Gur-Ari
 */
public class DefaultRemoteExceptionRecoveryStrategyFactory
		implements RemoteExceptionRecoveryStrategyFactory {

	@SuppressWarnings("unchecked")
	public RemoteExceptionRecoveryStrategy getRecoveryStrategy(String name,
			Class[] exposedIfaces) {
		return new DefaultRemoteExceptionRecoveryStrategy(name, exposedIfaces);
	}

	@SuppressWarnings("unchecked")
	public RemoteExceptionRecoveryStrategy getRecoveryStrategy(
			Class[] exposedIfaces) {
		return new DefaultRemoteExceptionRecoveryStrategy(null, exposedIfaces);
	}
}
