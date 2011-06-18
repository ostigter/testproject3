package org.ozsoft.searchengine;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public class ItemServiceImpl implements ItemService {
    
    private static final String PU_NAME = "search-engine";
    
    private final EntityManager em;
    
    private final TypedQuery<Item> findAllItemsQuery;
    
    private final TypedQuery<Tag> findTagQuery;
    
    private final TypedQuery<Index> findIndexQuery;
    
    public ItemServiceImpl() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        em = emf.createEntityManager();
        findAllItemsQuery = em.createNamedQuery("findAllItems", Item.class);
        findTagQuery = em.createNamedQuery("findTag", Tag.class);
        findIndexQuery = em.createNamedQuery("findIndex", Index.class);
    }

    @Override
    public void persistItem(Item item) {
        String itemName = item.getName();
        if (itemName == null || itemName.isEmpty()) {
            throw new IllegalStateException("Item with null or empty name");
        }
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            // Store item.
            em.persist(item);
            
            // Index item based on title (split into tags).
            for (String tagName : itemName.split(" ")) {
                // Find existing tag.
                tagName = tagName.toLowerCase();
                Tag tag = null;
                List<Tag> tags = findTagQuery.setParameter(1, tagName).getResultList();
                if (tags.isEmpty()) {
                    // Store new tag.
                    tag = new Tag();
                    tag.setName(tagName);
                    em.persist(tag);
                } else {
                    tag = tags.get(0);
                }
                // Store new index.
                Index index = new Index();
                index.setItem(item);
                index.setTag(tag);
                em.persist(index);
            }
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public Item retrieveItem(long id) {
        return em.find(Item.class, id);
    }

    @Override
    public List<Item> findAllItems() {
        return findAllItemsQuery.getResultList();
    }
    
    @Override
    public List<Item> findItemsByTitle(String title) {
        List<Item> items = new LinkedList<Item>();
        for (String tagName : title.split(" ")) {
            Tag tag = findTag(tagName);
            if (tag != null) {
                List<Index> indexes = findIndexQuery.setParameter(1, tag).getResultList();
                for (Index index : indexes) {
                    items.add(index.getItem());
                }
            }
        }
        return items;
    }
    
    private Tag findTag(String name) {
        Tag tag = null;
        try {
            tag = findTagQuery.setParameter(1, name).getSingleResult();
        } catch (NoResultException e) {
            // Ignore;
        }
        return tag;
    }

}
