package org.ozsoft.secs;

/**
 * SECS reply message as response to a primary message.
 * 
 * @author Oscar Stigter
 */
public abstract class SecsReplyMessage extends SecsMessage {

    /**
     * Handles the reply message. <br />
     * <br />
     * 
     * Optionally, this method can be implemented to do any processing upon receiving this reply message.
     * 
     * @throws SecsException
     *             If the message could not be processed.
     */
    protected abstract void handle() throws SecsException;

}
