package org.ozsoft.bookstore.services;

import java.util.List;

import org.ozsoft.bookstore.entities.Book;

public interface BookService {
    
    void create(Book book);
    
    List<Book> findAll();

    Book retrieve(long id);
    
    void update(Book book);
    
    void delete(Book book);

}
