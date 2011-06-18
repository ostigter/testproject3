package org.ozsoft.searchengine;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ItemServiceTest {
    
    @Test
    public void itemService() {
        ItemService itemService = new ItemServiceImpl();
        Item item = new Item();
        String name = "EPS OTAS Document Store";
        item.setName(name);
        itemService.persistItem(item);
        long id = item.getId();
        Assert.assertEquals(name, item.getName());
        
        item = itemService.retrieveItem(id);
        Assert.assertNotNull(item);
        Assert.assertEquals(id, item.getId());
        Assert.assertEquals(name, item.getName());
        
        List<Item> items = itemService.findAllItems();
        Assert.assertEquals(1, items.size());
        
        items = itemService.findItemsByTitle("eps OTAS");
        Assert.assertEquals(1, items.size());
    }

}
