package org.example.library.api;

public class BookNotFoundException extends LibraryException {

    private static final long serialVersionUID = -5292156927680283588L;

    public BookNotFoundException(long id) {
        super(String.format("Book with ID %d not found", id));
    }
}
