package org.ozsoft.bpm;

import org.junit.Test;
import org.ozsoft.bpm.handler.LogHandler;
import org.ozsoft.bpm.node.ActionNode;
import org.ozsoft.bpm.node.EndNode;
import org.ozsoft.bpm.node.Node;

/**
 * Integration test suite for the BPM application.
 * 
 * @author Oscar Stigter
 */
public class BpmTest {
	
	@Test
	public void bpm() {
		ProcessDefinition definition = new ProcessDefinition("Request Storage");
		Node startNode = definition.getStartNode();

        // "Log" node.
        Node logNode = new ActionNode("Log", new LogHandler());
        definition.addNode(logNode);
        startNode.addTransition(logNode);
        
//		// "Get TicketNr" node.
//		XPathContext xpc = new XPathContext();
//		xpc.addNamespace("req", "http://www.example.org/request");
//		XPathExpression expr = new XPathExpression("message", "/req:Request/req:TicketNr", xpc);
//		Node getTicketNrNode = new PropertyNode("Get TicketNr", "ticketNr", expr);
//		definition.addNode(getTicketNrNode);
//		logNode.addTransition(getTicketNrNode);
        
        // End node.
        Node endNode = new EndNode("Finished");
        definition.addNode(endNode);
        logNode.addTransition(endNode);
		
		ProcessEngine engine = new ProcessEngine();
		ProcessInstance instance = engine.createProcessInstance(definition);
		instance.setProperty("ticketId", 123L);
		instance.start();
	}

}
