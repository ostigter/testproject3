package org.ozsoft.searchengine;

import java.util.List;

public interface ItemService {
    
    void persistItem(Item item);
    
    Item getItem(long id);
    
    List<Item> getItems();

}
