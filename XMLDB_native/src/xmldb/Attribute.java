package xmldb;


public class Attribute extends Node {
    

	private String value;
    
    
	public Attribute(String name, String value, int element) {
        super(name);
        this.value = value;
        parent = element;
    }
    
    
	public String getValue() {
        return value;
    }
    
}
