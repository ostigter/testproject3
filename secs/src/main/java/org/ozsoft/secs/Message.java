package org.ozsoft.secs;

public abstract class Message {
    
    private SecsEquipment equipment;
    
    private int sessionId;
    
    private long transactionId;
    
    public final int getSessionId() {
        return sessionId;
    }
    
    public final long getTransactionId() {
        return transactionId;
    }
    
    protected SecsEquipment getEquipment() {
        return equipment;
    }
    
    /* package */ void setEquipment(SecsEquipment equipment) {
        this.equipment = equipment;
    }
    
    /* package */ void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    /* package */ void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }
    
    /* package */ abstract byte[] toByteArray() throws SecsParseException;
    
}
