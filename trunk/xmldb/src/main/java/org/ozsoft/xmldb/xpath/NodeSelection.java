package org.ozsoft.xmldb.xpath;

import java.util.LinkedList;
import java.util.List;

import org.ozsoft.xmldb.Node;

public class NodeSelection implements XPathExpression {
	
	private final List<NodeSelection> steps;
	
	public NodeSelection() {
		steps = new LinkedList<NodeSelection>();
	}
	
	public void add(NodeSelection selection) {
		steps.add(selection);
	}

	@Override
	public List<? extends Node> evaluate(Object context) {
		if (context instanceof Node) {
			List<NodeSelection> remainingSteps = new LinkedList<NodeSelection>();
			remainingSteps.addAll(steps);
			List<Node> remainingNodes = new LinkedList<Node>();
			remainingNodes.add((Node) context);
			List<Node> resultNodes = new LinkedList<Node>();
			evaluate(remainingSteps, remainingNodes, resultNodes);
			return resultNodes;
		} else {
			throw new IllegalArgumentException("Invalid context (must be a node)");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (NodeSelection step : steps) {
			sb.append('/').append(step);
		}
		return sb.toString();
	}
	
	private void evaluate(List<NodeSelection> remainingSteps, List<Node> remainingNodes, List<Node> resultNodes) {
		if (remainingSteps.size() > 0) {
			NodeSelection step = remainingSteps.remove(0);
			if (remainingSteps.size() > 0) {
				List<Node> nextNodes = new LinkedList<Node>();
				for (Node node : remainingNodes) {
					nextNodes.addAll(step.evaluate(node));
				}
				evaluate(remainingSteps, nextNodes, resultNodes);
			} else {
				for (Node node : remainingNodes) {
					resultNodes.addAll(step.evaluate(node));
				}
			}
		}
	}

}
