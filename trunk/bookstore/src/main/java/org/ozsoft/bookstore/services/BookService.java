package org.ozsoft.bookstore.services;

import java.util.List;

import javax.ejb.Local;

import org.ozsoft.bookstore.entities.Book;

@Local
public interface BookService {
    
    void create(Book book);
    
    List<Book> retrieveAll();

    Book retrieve(long id);
    
    void update(Book book);
    
    void delete(Book book);

}
