package org.ozsoft.bpm.node;

import org.apache.log4j.Logger;
import org.ozsoft.bpm.ActionHandler;
import org.ozsoft.bpm.ProcessContext;
import org.ozsoft.bpm.exception.BpmActionException;

public class ActionNode extends Node {
	
	private static final Logger LOG = Logger.getLogger(ActionNode.class);
	
	private final ActionHandler actionHandler;
	
	public ActionNode(String name, ActionHandler actionHandler) {
		super(name);
		this.actionHandler = actionHandler;
	}
	
	public ActionHandler getActionHandler() {
		return actionHandler;
	}

	@Override
	public void performAction(ProcessContext context) {
		try {
			actionHandler.performAction(context);
		} catch (BpmActionException e) {
			LOG.error("Error while performing action", e);
		}
	}
	
}
