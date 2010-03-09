package org.ozsoft.bpm.node;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ozsoft.bpm.ProcessContext;
import org.ozsoft.bpm.Transition;

public abstract class Node {
    
    private static final Logger LOG = Logger.getLogger(Node.class);
	
	protected final String name;
	
	protected final Map<String, Transition> transitions;
	
	public Node(String name) {
		this.name = name;
		transitions = new HashMap<String, Transition>();
	}
	
	public final String getName() {
		return name;
	}
	
	protected boolean allowMultipleTransitions() {
		return false;
	}
	
	public final void addTransition(Node toNode) {
		addTransition(null, toNode);
	}
	
	public final void addTransition(String name, Node toNode) {
		if (transitions.size() == 1 && !allowMultipleTransitions()) {
			throw new IllegalStateException("This node type does allow multiple transitions");
		}
		
		if (!transitions.containsKey(name)) {
			transitions.put(name, new Transition(name, toNode.getName()));
		}
	}
	
	public final String leave() {
	    return leave(null);
	}
	
	public final String leave(String transitionName) {
        Transition transition = transitions.get(transitionName);
        if (transition != null) {
            return transition.getDestination();
        } else {
            String msg = String.format("No transition named '%s' on node '%s'", transitionName, name);
            LOG.error(msg);
            throw new IllegalStateException(msg);
        }
	}
	
	public abstract void performAction(ProcessContext context);

	@Override
	public String toString() {
		return name;
	}

}
