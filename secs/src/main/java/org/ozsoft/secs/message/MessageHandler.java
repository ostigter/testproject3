package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsException;

public abstract class MessageHandler {
    
    private final int stream;
    
    private final int function;
    
    private final boolean withReply;
    
    public MessageHandler(int stream, int function, boolean withReply) {
        this.stream = stream;
        this.function = function;
        this.withReply = withReply;
    }
    
    public int getStream() {
        return stream;
    }
    
    public int getFunction() {
        return function;
    }
    
    public boolean withReply() {
        return withReply;
    }
    
    public abstract DataMessage handle(DataMessage message) throws SecsException;

}
