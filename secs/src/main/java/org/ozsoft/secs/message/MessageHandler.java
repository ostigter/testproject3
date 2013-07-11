package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsException;

public abstract class MessageHandler {
    
    private final int stream;
    
    private final int function;
    
    private final String description;
    
    public MessageHandler(int stream, int function, String description) {
        this.stream = stream;
        this.function = function;
        this.description = description;
    }
    
    public int getStream() {
        return stream;
    }
    
    public int getFunction() {
        return function;
    }
    
    public String getDescription() {
        return description;
    }
    
    public abstract DataMessage handle(DataMessage message) throws SecsException;
    
    @Override
    public String toString() {
        return String.format("S%dF%d - %s", stream, function, description);
    }

}
