package org.example.library.api;

public class LibraryException extends Exception {

    private static final long serialVersionUID = 4624174385002819259L;

    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
