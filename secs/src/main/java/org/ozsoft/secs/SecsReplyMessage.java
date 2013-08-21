package org.ozsoft.secs;

public abstract class SecsReplyMessage extends SecsMessage {
    
//    private SecsPrimaryMessage primaryMessage;
//
//    public final SecsPrimaryMessage getPrimaryMessage() {
//        return primaryMessage;
//    }
//    
//    /* package */ void setPrimaryMessage(SecsPrimaryMessage primaryMessage) {
//        this.primaryMessage = primaryMessage;
//    }
    
    protected abstract void handle() throws SecsException;
    
}
