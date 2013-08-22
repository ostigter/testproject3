package org.ozsoft.secs;

/**
 * Root class of all SECS messages.
 * 
 * @author Oscar Stigter
 */
public abstract class Message {

    /** The SECS equipment. */
    private SecsEquipment equipment;

    /** Session ID. */
    private int sessionId;

    /** Transaction ID. */
    private long transactionId;

    /**
     * Returns the Session ID (Device ID).
     * 
     * @return The Session ID (Device ID).
     */
    public final int getSessionId() {
        return sessionId;
    }

    /**
     * Returns the Transaction ID (System Bytes).
     * 
     * @return The Transaction ID (System Bytes).
     */
    public final long getTransactionId() {
        return transactionId;
    }

    /**
     * Returns the SECS equipment.
     * 
     * @return The SECS equipment.
     */
    protected SecsEquipment getEquipment() {
        return equipment;
    }

    /**
     * Sets the SECS equipment.
     * 
     * @param equipment
     *            The SECS equipment.
     */
    /* package */void setEquipment(SecsEquipment equipment) {
        this.equipment = equipment;
    }

    /**
     * Sets the Session ID (Device ID).
     * 
     * @param sessionId
     *            The Session ID (Device ID).
     */
    /* package */void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Sets the Transaction ID (System Bytes).
     * 
     * @param transactionId
     *            The Transaction ID (System Bytes).
     */
    /* package */void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Serializes the message to a byte array.
     * 
     * @throws SecsParseException
     *             If the messag could not be serialized.
     */
    /* package */abstract byte[] toByteArray() throws SecsParseException;

}
