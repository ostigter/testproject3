package namepool;

import java.util.HashMap;
import java.util.Map;

/**
 * Namepool to efficiently store string values. <br />
 * <br />
 * 
 * Each unique value is stored only once in memory and given an unique ID that
 * multiple clients can share.
 * 
 * @author Oscar Stigter
 */
public class Namepool {

    private final Map<Long, Name> namesById;

    private final Map<String, Name> namesByValue;

    private long nextId = 1;

    /**
     * Constructor.
     */
    public Namepool() {
        namesById = new HashMap<Long, Name>();
        namesByValue = new HashMap<String, Name>();
    }

    /**
     * Stores a string value in the namepool.
     * 
     * If a matching string already exists in the pool, its usage count is
     * incremented.
     * 
     * @param value
     *            The string value.
     * 
     * @return the ID
     */
    public long store(String value) {
        Name name = namesByValue.get(value);
        if (name == null) {
            long id = getNextId();
            name = new Name(id, value);
            namesById.put(id, name);
            namesByValue.put(value, name);
        } else {
            name.incrementCount();
        }
        return name.id;
    }
    
    /**
     * Returns the stored string value associated with the specified ID, or null
     * if not found.
     * 
     * @param id
     *            The name ID.
     * 
     * @return the string value, or null if not found
     */
    public String retrieve(long id) {
        String value = null;
        Name name = namesById.get(id);
        if (name != null) {
            value = name.value;
        }
        return value;
    }

    /**
     * Decreases the usage count of a name.
     * 
     * If the usage count reaches 0, the name is deleted from the pool.
     * 
     * @param id
     *            The name ID.
     * 
     * @return the string value, or null if not found
     */
    public void unuse(long id) {
        Name name = namesById.get(id);
        if (name != null) {
            name.decrementCount();
            if (name.getCount() == 0) {
                namesById.remove(id);
                namesByValue.remove(name.value);
            }
        }
    }

    /**
     * Decreases the usage count of a name.
     * 
     * @param value
     *            The string value.
     */
    public void unuse(String value) {
        Name name = namesByValue.get(value);
        if (name != null) {
            unuse(name.id);
        }
    }

    private long getNextId() {
        return nextId++;
    }

    private static class Name {

        private final long id;

        private final String value;

        private long count;
        
        public Name(long id, String value) {
            this.id = id;
            this.value = value;
            count = 1;
        }
        
        public long getCount() {
            return count;
        }
        
        public void incrementCount() {
            count++;
        }
        
        public void decrementCount() {
            if (count > 0) {
                count--;
            }
        }

    }

}
