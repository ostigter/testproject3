package trmi;

/** TRMI+ (renamed)
 * Thrown by a <code>RemoteExceptionRecoveryStrategy</code> when a new wrapper
 * cannot be provided.
 *
 * @author Guy Gur-Ari
 */
public class RemoteRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
     * Constructs the object.
     */
    public RemoteRuntimeException() {
    }

    /**
     * Constructs the object.
     *
     * @param desc A description of the error.
     */
    public RemoteRuntimeException(String desc) {
        super(desc);
    }

}
