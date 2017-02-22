package org.ozsoft.blackbeard.parsers;

public class ParseException extends Exception {

    private static final long serialVersionUID = 6207436211690318179L;

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
