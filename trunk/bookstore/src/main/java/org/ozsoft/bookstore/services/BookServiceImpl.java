package org.ozsoft.bookstore.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.bookstore.entities.Book;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BookServiceImpl implements BookService {
    
    @PersistenceContext(unitName = "bookstore")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void create(Book book) {
        em.persist(book);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Override
    public List<Book> retrieveAll() {
        return em.createNamedQuery("findAllBooks", Book.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Override
    public Book retrieve(long id) {
        return em.find(Book.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void update(Book book) {
        em.merge(book);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void delete(Book book) {
        em.remove(book);
    }

}
