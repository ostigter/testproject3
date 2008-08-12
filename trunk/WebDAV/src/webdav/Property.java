package webdav;


public class Property {
	
	
	private String name;
	
	private String value;
	
	
	public Property(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public String getValue() {
		return value;
	}
	
	
	public void setValue(String value) {
		this.value = value;
	}


}
