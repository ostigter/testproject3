package org.example.library.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.example.library.api.Book;
import org.example.library.api.BookNotFoundException;
import org.example.library.api.Library;

public class LibraryService implements Library {

    private final Map<Long, Book> books = new HashMap<Long, Book>();

    public LibraryService() {
        loadBooks();
    }

    @Override
    public Collection<Book> listBooks() {
        return books.values();
    }

    @Override
    public Book getBook(long id) throws BookNotFoundException {
        Book book = books.get(id);
        if (book != null) {
            return book;
        } else {
            throw new BookNotFoundException(id);
        }
    }

    @Override
    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    @Override
    public void deleteBook(long id) {
        books.remove(id);
    }

    private void loadBooks() {
        Book book = new Book();
        long id = 1L;
        book.setId(id);
        book.setAuthor("Elliot Ness");
        book.setTitle("Some long time ago");
        books.put(id, book);

        book = new Book();
        id++;
        book.setId(id);
        book.setAuthor("Bruce Wayne");
        book.setTitle("The Dark Knight rises");
        books.put(id, book);
    }
}
