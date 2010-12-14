package org.ozsoft.bookstore.services;

import org.ozsoft.bookstore.entities.Book;

//@Local
public interface BookService {
    
    void create(Book book);
    
    Book retrieve(long id);
    
    void update(Book book);
    
    void delete(Book book);

}
