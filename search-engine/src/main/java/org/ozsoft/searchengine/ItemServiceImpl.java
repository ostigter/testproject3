package org.ozsoft.searchengine;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public class ItemServiceImpl implements ItemService {
    
    private static final String PU_NAME = "search-engine";
    
    private final EntityManager em;
    
    private final TypedQuery<Item> getAllQuery;
    
    public ItemServiceImpl() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        em = emf.createEntityManager();
        getAllQuery = em.createNamedQuery("findAllItems", Item.class);
    }

    @Override
    public void persistItem(Item item) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(item);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public Item getItem(long id) {
        return em.find(Item.class, id);
    }

    @Override
    public List<Item> getItems() {
        return getAllQuery.getResultList();
    }

}
