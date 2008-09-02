package namepool;


import java.util.HashMap;
import java.util.Map;


/**
 * Namepool to efficiently store string values.
 * 
 * Each unique value is stored only once in memory and given an unique ID that
 * a client can use.
 * 
 * @author Oscar Stigter
 */
public class Namepool {
    
    
    private final Map<Integer, Name> namesById;
    
    private final Map<String, Name> namesByValue;
    
    private int nextId = 0;
    
    
    /**
     * Constructor.
     */
    public Namepool() {
        namesById = new HashMap<Integer, Name>();
        namesByValue = new HashMap<String, Name>();
    }
    
    
    /**
     * Stores a string value in the namepool.
     * 
     * If a matching string already exists in the pool, its usage count is
     * incremented.
     * 
     * @param value  the string value
     * 
     * @return  the ID
     */
    public int store(String value) {
        Name name = namesByValue.get(value);
        
        if (name != null) {
            name.count++;
        } else {
            name = new Name();
            int id = nextId++;
            name.id = id;
            name.value = value;
            name.count = 1;
            namesById.put(id, name);
            namesByValue.put(value, name);
        }
        
        return name.id;
    }
    
    
    /**
     * Returns the stored string value associated with the specified ID, or
     * null if not found.
     * 
     * @param id  the ID
     * 
     * @return  the string value, or null if not found
     */
    public String retrieve(int id) {
        String value = null;
        Name name = namesById.get(id);
        if (name != null) {
            value = name.value;
        }
        return value;
    }
    
    
    /**
     * Decreases the usage count of a specific string value.
     * 
     * If the usage count reaches 0, the string value is deleted from the pool.
     * 
     * @param id  the ID
     * 
     * @return  the string value, or null if not found
     */
    public void unuse(int id) {
        Name name = namesById.get(id);
        if (name != null) {
            name.count--;
            if (name.count == 0) {
                namesById.remove(id);
                namesByValue.remove(name.value);
            }
        }
    }
    
    
    public void unuse(String value) {
        Name name = namesByValue.get(value);
        if (name != null) {
            unuse(name.id);
        }
    }
    
    
    //-------------------------------------------------------------------------
    //  Inner classes
    //-------------------------------------------------------------------------
    
    
    private class Name {
        
        
        public int id;

        public String value;

        public int count;
        

    }


}
