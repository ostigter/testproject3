package org.ozsoft.bookstore.web;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.ozsoft.bookstore.entities.Book;
import org.ozsoft.bookstore.services.BookService;

@ManagedBean
@RequestScoped
public class BookController {
    
    @EJB
    private BookService bookService;
    
    private String author;
    
    private String title;
    
    public BookController() {
        reset();
    }
    
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
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            bookService.create(book);
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "The book has been added successfully.", null));
            reset();
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An error occured while adding the book: " + e.getMessage(), null));
        }
    }
    
    private void reset() {
        author = null;
        title = null;
    }

}
