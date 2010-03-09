package org.ozsoft.bpm;

import org.apache.log4j.Logger;
import org.ozsoft.bpm.node.Node;

public class ProcessInstance {
	
    private static final Logger LOG = Logger.getLogger(ProcessInstance.class);
    
    private final long id;
    
	private final ProcessDefinition definition;
	
	private ProcessStatus status;
	
	private ProcessContext context;
	
	private Node node;
	
	public ProcessInstance(long id, ProcessDefinition definition) {
	    this.id = id;
		this.definition = definition;
		node = definition.getStartNode();
		log();
		status = ProcessStatus.CREATED;
	}
	
	public long getId() {
	    return id;
	}
	
	public ProcessDefinition getProcessDefinition() {
		return definition;
	}
	
	public void setProcessContext(ProcessContext context) {
	    if (this.context == null) {
	        this.context = context;
	    } else {
	        throw new IllegalStateException("Process context already set");
	    }
	}
	
	public Object getProperty(String property) {
	    return context.getProperty(property);
	}
	
	public void setProperty(String property, Object value) {
	    context.setProperty(property, value);
	}
	
	public ProcessStatus getProcessState() {
	    return status;
	}
	
	public void start() {
	    if (status == ProcessStatus.RUNNING) {
	        throw new IllegalStateException("Already running");
	    }
        if (status == ProcessStatus.ENDED) {
            throw new IllegalStateException("Already ended");
        }
        if (context == null) {
            throw new IllegalStateException("Process context not set");
        }

        status = ProcessStatus.RUNNING;
        
        signal();
	}
	
	public void signal() {
	    signal(null);
	}
	
	public void signal(String transitionName) {
        String destination = node.leave(transitionName);
        node = definition.getNode(destination);
        if (node == null) {
            status = ProcessStatus.ERROR;
            String msg = String.format("No node name '%s' found in process definition '%s'", destination, definition);
            LOG.error(msg);
        }
        log();
        node.performAction(context);
	}
	
	private void log() {
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("Process instance %d now in node '%s'", id, node));
        }
	}

}
