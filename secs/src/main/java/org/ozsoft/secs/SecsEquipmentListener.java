package org.ozsoft.secs;


public interface SecsEquipmentListener {
    
    void connectionStateChanged(ConnectionState connectionState);
    
    void communicationStateChanged(CommunicationState communicationState);
    
    void controlStateChanged(ControlState connectionState);
    
    void messageReceived(Message message);
    
    void messageSent(Message message);
    
}
