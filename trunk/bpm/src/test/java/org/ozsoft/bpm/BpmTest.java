package org.ozsoft.bpm;

import nu.xom.XPathContext;

import org.junit.Test;
import org.ozsoft.bpm.expression.XPathExpression;
import org.ozsoft.bpm.handler.LogHandler;
import org.ozsoft.bpm.node.ActionNode;
import org.ozsoft.bpm.node.Node;
import org.ozsoft.bpm.node.PropertyNode;

/**
 * Integration test suite for the BPM application.
 * 
 * @author Oscar Stigter
 */
public class BpmTest {
	
	@Test
	public void bpm() {
		ProcessDefinition def = new ProcessDefinition("Request Storage");
		Node startNode = def.getStartNode();

		// "Get TicketNr" node.
		XPathContext xpc = new XPathContext();
		xpc.addNamespace("req", "http://www.example.org/request");
		XPathExpression expr = new XPathExpression("message", "/req:Request/req:TicketNr", xpc);
		Node getTicketNrNode = new PropertyNode("Get TicketNr", "ticketNr", expr);
		def.addNode(getTicketNrNode);
		startNode.addTransition(getTicketNrNode);
		
		// "Log" node.
		Node logNode = new ActionNode("Log", new LogHandler());
		def.addNode(logNode);
		getTicketNrNode.addTransition(logNode);
	}

}
