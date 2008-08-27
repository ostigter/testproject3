package xmldb;


public class Text extends Node {
    

	public Text(String value) {
        super(value);
    }
    
    
	@Override // Object
    public String toString() {
        return "text(\"" + name + "\")";
    }
    
}
