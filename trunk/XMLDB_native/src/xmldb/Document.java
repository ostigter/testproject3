package xmldb;


import java.io.File;


public class Document extends Node {
    
    
    private static final XmlParser parser = new XmlParser();
	
	
    public Document(String name) {
        super(name);
    }
    
    
    public void setContent(File file) throws XmldbException {
		parser.parse(file, this);
    }
    
    
    @Override // Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int child : children) {
            Node node = Database.getInstance().getNode(child);
            if (node != null) {
                sb.append(node.toString());
            } else {
            	throw new RuntimeException(
            			"Node with ID " + child + "not found");
            }
        }
        return sb.toString();
    }
    
}
