package org.ozsoft.bpm;

import org.ozsoft.bpm.exception.BpmActionException;

public interface ActionHandler {
	
	void performAction(ProcessContext context) throws BpmActionException;

}
