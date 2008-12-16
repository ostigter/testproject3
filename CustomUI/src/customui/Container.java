package customui;


import java.util.ArrayList;
import java.util.List;


public class Container extends AbstractComponent {
	

	private static final Component[] EMPTY_COMPONENT_ARRAY = new Component[0];
	
	private List<Component> children;
	
	
	public Container(Container parent) {
		super(parent);
		children = new ArrayList<Component>();
	}
	
	
	void addComponent(Component component) {
		children.add(component);
	}
	

	Component[] getComponents() {
		return children.toArray(EMPTY_COMPONENT_ARRAY);
	}
	
	
	void doLayout() {
		// TODO
	}


}
