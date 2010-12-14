package org.ozsoft.bookstore.entities;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.Test;

public class BookTest {
    
    private static final String PERSISTENCE_UNIT_NAME = "bookstore-test";
    
    @Test
    public void test() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = emf.createEntityManager();
        
        Book book = new Book();
        book.setAuthor("Dick Bruna");
        book.setTitle("Nijntje in de dierentuin");
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(book);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            System.err.println("ERROR: Could not store book in database: " + e.getMessage());
        }
    }

}
