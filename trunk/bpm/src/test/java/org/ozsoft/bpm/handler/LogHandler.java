package org.ozsoft.bpm.handler;

import org.apache.log4j.Logger;
import org.ozsoft.bpm.ActionHandler;
import org.ozsoft.bpm.ProcessContext;
import org.ozsoft.bpm.exception.BpmActionException;

public class LogHandler implements ActionHandler {
	
	private static final Logger LOG = Logger.getLogger(LogHandler.class);

	@Override
	public void performAction(ProcessContext context) throws BpmActionException {
		LOG.info("Logging some message!");
		context.getProcessInstance().signal();
	}

}
