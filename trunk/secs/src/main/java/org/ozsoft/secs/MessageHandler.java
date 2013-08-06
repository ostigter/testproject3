package org.ozsoft.secs;

import org.ozsoft.secs.message.DataMessage;

public abstract class MessageHandler {
    
    private final int stream;
    
    private final int function;
    
    private final String description;
    
    private SecsEquipment equipment;
    
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
    
    protected SecsEquipment getEquipment() {
        return equipment;
    }
    
    public abstract DataMessage handle(DataMessage message) throws SecsException;
    
    /* package */ void setEquipment(SecsEquipment equipment) {
        this.equipment = equipment;
    }
    
    @Override
    public String toString() {
        return String.format("S%dF%d - %s", stream, function, description);
    }

}
