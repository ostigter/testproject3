package org.ozsoft.bpm;

import java.util.Map;
import java.util.TreeMap;

public class ProcessContext {
	
	private final Map<String, Object> properties;
	
	private final ProcessInstance processInstance;
	
	public ProcessContext(ProcessInstance processInstance) {
		properties = new TreeMap<String, Object>();
		this.processInstance = processInstance;
	}
	
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	public void setProperty(String name, Object value) {
		if (value == null) {
			properties.remove(name);
		} else {
			properties.put(name, value);
		}
	}

}
