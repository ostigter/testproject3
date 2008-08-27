package xmldb;


import java.util.ArrayList;
import java.util.List;


public class Node {
    

	private static final Integer[] EMPTY_INT_ARRAY = new Integer[0];
    
    protected int id;
    
    protected String name;
    
    protected int parent = -1;
    
    protected List<Integer> children;
    

    public Node(String name) {
        this.name = name;
        
        id = Database.getInstance().getNextId();
        
        Database.getInstance().storeNode(this);
    }
    

    public int getId() {
        return id;
    }
    

    public String getName() {
        return name;
    }
    

    public int getParent() {
        return id;
    }
    

    public int getNoOfChildren() {
    	if (children != null) {
            return children.size();
    	} else {
    		return 0;
    	}
    }
    

    public Integer[] getChildren() {
    	if (children != null) {
    		return children.toArray(EMPTY_INT_ARRAY);
    	} else {
    		return EMPTY_INT_ARRAY;
    	}
    }


    public void addChild(int id) {
    	if (children == null) {
            children = new ArrayList<Integer>();
    	}
        children.add(id);
    }
    
}
