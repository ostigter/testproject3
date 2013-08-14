package org.ozsoft.secs;

import org.ozsoft.secs.format.Data;

public abstract class SecsMessage {
    
    public static final int STREAM = -1;

    public static final int FUNCTION = -1;
    
    public static final boolean WITH_REPLY = false;
    
    public static final String DESCRIPTION = "<Description>";
    
    private SecsEquipment equipment;

    private int sessionId;
    
    private long transactionId;
    
    public final SecsEquipment getEquipment() {
        return equipment;
    }
    
    /* package */ void setEquipment(SecsEquipment equipment) {
        this.equipment = equipment;
    }
    
    public final int getSessionId() {
        return sessionId;
    }
    
    /* package */ void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    public final long getTransactionId() {
        return transactionId;
    }
    
    /* package */ void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }
    
    public abstract void parseData(Data<?> data) throws SecsParseException;
    
    public abstract Data<?> getData() throws SecsParseException;
    
}
