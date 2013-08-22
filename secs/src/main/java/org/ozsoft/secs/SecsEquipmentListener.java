package org.ozsoft.secs;

/**
 * Listener for SECS equipment events like state changes and messages sent and
 * received.
 * 
 * @author Oscar Stigter
 */
public interface SecsEquipmentListener {

    /**
     * Notification that the HSMS Connection State has changed.
     * 
     * @param connectionState
     *            The new Connection State.
     */
    void connectionStateChanged(ConnectionState connectionState);

    /**
     * Notification that the SECS Communication State has changed.
     * 
     * @param communicationState
     *            The new Communication State.
     */
    void communicationStateChanged(CommunicationState communicationState);

    /**
     * Notification that the SECS Control State has changed.
     * 
     * @param controlState
     *            The new Control State.
     */
    void controlStateChanged(ControlState connectionState);

    /**
     * Notification of an incoming SECS message.
     * 
     * @param message
     *            The message that has been received.
     */
    void messageReceived(Message message);

    /**
     * Notification of an outgoing SECS message.
     * 
     * @param message
     *            The message that has been sent.
     */
    void messageSent(Message message);

}
