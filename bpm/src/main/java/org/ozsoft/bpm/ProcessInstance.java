package org.ozsoft.bpm;

public class ProcessInstance {
	
	private final ProcessDefinition processDefinition;
	
	public ProcessInstance(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}
	
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

}
