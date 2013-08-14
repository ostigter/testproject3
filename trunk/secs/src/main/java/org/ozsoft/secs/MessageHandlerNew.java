package org.ozsoft.secs;

public abstract class MessageHandlerNew<REQ extends SecsMessage, RSP extends SecsMessage> {
    
    private SecsEquipment equipment;
    
    public abstract RSP handle(REQ requestMessage) throws SecsException;

    protected SecsEquipment getEquipment() {
        return equipment;
    }
    
    /* package */ void setEquipment(SecsEquipment equipment) {
        this.equipment = equipment;
    }
    
}
