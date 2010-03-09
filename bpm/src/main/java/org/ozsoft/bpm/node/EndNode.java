package org.ozsoft.bpm.node;

import org.ozsoft.bpm.ProcessContext;

public class EndNode extends Node {
    
    public EndNode(String name) {
        super(name);
    }

    @Override
    public void performAction(ProcessContext context) {
        // Empty implementation.
    }

}
