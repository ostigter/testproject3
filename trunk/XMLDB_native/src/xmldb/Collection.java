package xmldb;


public class Collection extends Node {
    

	public Collection(String name) {
        super(name);
    }
    
    
	@Override // Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        Node node = Database.getInstance().getNode(parent);
        while (node.parent != -1) {
            sb.insert(0, '/');
            sb.insert(0, node.name);
            node = Database.getInstance().getNode(parent);
        }
        return sb.toString();
    }
    
}
