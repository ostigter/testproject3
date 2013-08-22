package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.Data;

/**
 * Abort Transaction (ABORT) reply message.
 * 
 * @author Oscar Stigter
 */
public class SxF0 extends SecsReplyMessage {
    
    private static final int FUNCTION = 0;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "ABORT";
    
    /**
     * The message stream. 
     */
    private final int stream;
    
    /**
     * Constructor.
     * 
     * @param stream
     *            The message stream.
     */
    public SxF0(int stream) {
        this.stream = stream;
    }

    @Override
    public int getStream() {
        return stream;
    }

    @Override
    public int getFunction() {
        return FUNCTION;
    }

    public boolean withReply() {
        return WITH_REPLY;
    }

    @Override
    public String getDescripton() {
        return DESCRIPTION;
    }

    @Override
    protected void parseData(Data<?> data) throws SecsParseException {
        if (data != null) {
            throw new SecsParseException("Unexpected data");
        }
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        // No data.
        return null;
    }

    @Override
    protected void handle() throws SecsException {
        // Not implemented.
    }

}
