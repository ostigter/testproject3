package org.ozsoft.bookstore.services;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.bookstore.entities.Book;

@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BookServiceBean implements BookService {
    
    @PersistenceContext(unitName = "bookstore")
    private EntityManager em;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void create(Book book) {
        em.persist(book);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Book> retrieveAll() {
        return em.createNamedQuery("findAllBooks", Book.class).getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Book retrieve(long id) {
        return em.find(Book.class, id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Book book) {
        em.merge(book);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Book book) {
        em.remove(book);
    }

}
