package trmi;

import java.io.Serializable;

/**
 * Throws by {@link RemoteObjectWrapper} if an error occurs while invoking a
 * method.
 * 
 * @author Guy Gur-Ari
 */
public class RemoteObjectWrapperException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs the object.
     */
    public RemoteObjectWrapperException() {
        super();
    }

    /**
     * Constructs the object.
     * 
     * @param desc
     *            A description of the exception.
     */
    public RemoteObjectWrapperException(String desc) {
        super(desc);
    }

    /**
     * Constructs the object.
     * 
     * @param desc
     *            A description of the exception.
     * @param cause
     *            The cause of this exception.
     */
    public RemoteObjectWrapperException(String desc, Throwable cause) {
        super(desc, cause);
    }

}
