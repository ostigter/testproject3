package webdav;


import java.util.ArrayList;
import java.util.List;



public class Collection extends Resource {
	
	
	private List<Resource> resources;
	
	
	public Collection() {
		super();
		resources = new ArrayList<Resource>();
	}
	
	
	public List<Resource> getResources() {
		return resources;
	}
	
	
	public void addResource(Resource res) {
		resources.add(res);
	}


}
