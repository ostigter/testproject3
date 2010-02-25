package org.ozsoft.xmldb.xpath;

import java.util.LinkedList;
import java.util.List;

import org.ozsoft.xmldb.Node;

public class NodeSelection implements Expression {
	
	private final List<NodeSelection> selections;
	
	public NodeSelection() {
		selections = new LinkedList<NodeSelection>();
	}
	
	public void add(NodeSelection selection) {
		selections.add(selection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> evaluate(Object context) {
		List<Node> nodes = new LinkedList<Node>();
		for (NodeSelection selection : selections) {
			List<?> list = selection.evaluate(context);
			for (Object node : list) {
				nodes.addAll((List) selection.evaluate(node));
			}
		}
		return nodes;
	}

}
