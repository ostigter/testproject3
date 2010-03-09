package org.ozsoft.bpm.node;

import java.util.HashMap;
import java.util.Map;

import org.ozsoft.bpm.ProcessContext;
import org.ozsoft.bpm.Transition;

public abstract class Node {
	
	protected final String name;
	
	protected final Map<String, Transition> transitions;
	
	public Node(String name) {
		this.name = name;
		transitions = new HashMap<String, Transition>();
	}
	
	public String getName() {
		return name;
	}
	
	protected boolean allowMultipleTransitions() {
		return false;
	}
	
	public void addTransition(Node toNode) {
		addTransition(null, toNode);
	}
	
	public void addTransition(String name, Node toNode) {
		if (transitions.size() == 1 && !allowMultipleTransitions()) {
			throw new IllegalStateException("This node type does allow multiple transitions");
		}
		
		if (!transitions.containsKey(name)) {
			transitions.put(name, new Transition(name, toNode.getName()));
		}
	}
	
	public abstract void performAction(ProcessContext context);

	@Override
	public String toString() {
		return name;
	}

}
