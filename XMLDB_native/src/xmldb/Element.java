package xmldb;


import java.util.ArrayList;


public class Element extends Node {

	
    private static final Attribute[] EMPTY_ATTRIBUTE_ARRAY = new Attribute[0];
    
    private ArrayList<Attribute> attributes;
    
    
    public Element(String name) {
        super(name);
    }
    

    public int getNoOfAttributes() {
    	if (attributes != null) {
    		return attributes.size();
    	} else {
    		return 0;
    	}
    }
    

    public Attribute[] getAttributes() {
    	if (attributes != null) {
    		return attributes.toArray(EMPTY_ATTRIBUTE_ARRAY);
    	} else {
    		return EMPTY_ATTRIBUTE_ARRAY;
    	}
    }

    
    public void addAttribute(Attribute attribute) {
    	if (attributes == null) {
    		attributes = new ArrayList<Attribute>();
    	}
        attributes.add(attribute);
    }
    
    
    @Override // Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(name);
        if (attributes.size() != 0) {
            for (Attribute attr : attributes) {
                sb.append(' ');
                sb.append(attr.getName());
                sb.append("=\"");
                sb.append(attr.getValue());
                sb.append("\"");
            }
        }
        if (children.size() != 0) {
            sb.append('>');
            for (int id : children) {
                Node node = Database.getInstance().getNode(id);
                if (node != null) {
                    sb.append(node.toString());
                }
            }
            sb.append("</");
            sb.append(name);
            sb.append('>');
        } else {
            sb.append("/>");
        }
        return sb.toString();
    }
    
}
