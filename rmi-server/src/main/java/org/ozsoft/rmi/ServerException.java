package org.ozsoft.rmi;

public class ServerException extends Exception {
    
    private static final long serialVersionUID = -8711093715866420222L;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
