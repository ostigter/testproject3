package org.ozsoft.searchengine;

import java.util.List;

public interface ItemService {
    
    void persistItem(Item item);
    
    Item retrieveItem(long id);
    
    List<Item> findAllItems();
    
    List<Item> findItemsByTitle(String title);

}
