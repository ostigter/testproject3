package org.ozsoft.bpm.node;

import org.ozsoft.bpm.ProcessContext;

public class StartNode extends Node {
	
	private static final String NAME = "START";
	
	public StartNode() {
		super(NAME);
	}

	@Override
	public void performAction(ProcessContext context) {
		// Empty implementation.
	}
	
}
