package org.ozsoft.bpm;

import java.util.HashMap;
import java.util.Map;

public class ProcessEngine {
    
    private final Map<Long, ProcessInstance> instances;
    
    private long nextId = 1L;
    
    public ProcessEngine() {
        instances = new HashMap<Long, ProcessInstance>();
    }
    
    public ProcessInstance createProcessInstance(ProcessDefinition definition) {
        long id = getNextId();
        ProcessInstance instance = new ProcessInstance(id, definition);
        ProcessContext context = new ProcessContext(instance);
        instance.setProcessContext(context);
        instances.put(id, instance);
        return instance;
    }
    
    private synchronized long getNextId() {
        return nextId++;
    }

}
