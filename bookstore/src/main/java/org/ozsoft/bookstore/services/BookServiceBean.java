package org.ozsoft.bookstore.services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ozsoft.bookstore.entities.Book;

@Stateless
@Local
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BookServiceBean implements BookService {
    
    private static final String FIND_ALL = "SELECT b FROM Book b";
    
    private Query findAllQuery;
    
    @PersistenceContext(unitName = "bookstore")
    private EntityManager em;
    
    @PostConstruct
    public void postConstruct() {
        findAllQuery = em.createQuery(FIND_ALL, Book.class);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void create(Book book) {
        em.persist(book);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @SuppressWarnings("unchecked")
    public List<Book> findAll() {
        return findAllQuery.getResultList();
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
