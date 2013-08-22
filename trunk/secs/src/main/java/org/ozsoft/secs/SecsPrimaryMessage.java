package org.ozsoft.secs;

/**
 * SECS primary message. <br />
 * <br />
 * 
 * In case the primary message requires a reply message, a message transaction is started.
 * 
 * @author Oscar Stigter
 */
public abstract class SecsPrimaryMessage extends SecsMessage {

    protected abstract SecsReplyMessage handle() throws SecsException;

}
