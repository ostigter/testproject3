package org.ozsoft.bookstore.web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ozsoft.bookstore.entities.Book;
import org.ozsoft.bookstore.services.BookService;

@ManagedBean
@RequestScoped
public class BookController {
    
    @EJB
    private BookService bookService;
    
    private String author;
    
    private String title;
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void doAddBook() {
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        bookService.create(book);
    }

}
