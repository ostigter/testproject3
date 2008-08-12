package webdav;


import java.util.HashMap;
import java.util.Map;



public class Resource {
	
	
	private Map<String, Object> properties;
	
	
	public Resource() {
		properties = new HashMap<String, Object>();
	}
	
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}


}
