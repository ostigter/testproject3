package org.ozsoft.bpm;

import java.util.HashMap;
import java.util.Map;

import org.ozsoft.bpm.node.Node;
import org.ozsoft.bpm.node.StartNode;

public class ProcessDefinition {
	
	private final String name;
	
	private final Map<String, Node> nodes;
	
	private final Node startNode;
	
	public ProcessDefinition(String name) {
		this.name = name;
		nodes = new HashMap<String, Node>();
		startNode = new StartNode();
	}
	
	public String getName() {
		return name;
	}
	
	public Node getStartNode() {
		return startNode;
	}
	
	public void addNode(Node node) {
		if (node instanceof StartNode) {
			throw new IllegalArgumentException("Cannot add start node");
		} else {
			nodes.put(node.getName(), node);
		}
	}
	
	public Node getNode(String name) {
	    return nodes.get(name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
