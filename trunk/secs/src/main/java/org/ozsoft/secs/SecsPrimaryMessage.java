package org.ozsoft.secs;

public abstract class SecsPrimaryMessage extends SecsMessage {

    protected abstract SecsReplyMessage handle() throws SecsException;
    
}
