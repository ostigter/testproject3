package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;

/**
 * S1F16 OFF-LINE Acknowledge (OFLA) reply message.
 * 
 * @author Oscar Stigter
 */
public class S1F16 extends SecsReplyMessage {

    public static final int OFLACK_ACKNOWLEDGE = 0x00;

    private static final int STREAM = 1;

    private static final int FUNCTION = 16;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "OFF-LINE Acknowledge (OFLA)";
    
    private Integer ofla;
    
    public int getOfla() {
        return ofla;
    }
    
    public void setOfla(int ofla) {
        this.ofla = ofla;
    }

    @Override
    public int getStream() {
        return STREAM;
    }

    @Override
    public int getFunction() {
        return FUNCTION;
    }

    @Override
    public boolean withReply() {
        return WITH_REPLY;
    }

    @Override
    public String getDescripton() {
        return DESCRIPTION;
    }

    @Override
    protected void parseData(Data<?> data) throws SecsParseException {
        if (data == null) {
            throw new SecsParseException("Missing data");
        }
        if (!(data instanceof B)) {
            throw new SecsParseException("OFLACK must be of type B");
        }
        B b = (B) data;
        if (b.length() != 1) {
            throw new SecsParseException("OFLACK must have a length of exactly 1 byte");
        }
        int ofla = b.get(0);
        if (ofla != OFLACK_ACKNOWLEDGE) {
            throw new SecsParseException("Invalid OFLACK value: " + ofla);
        }
        setOfla(ofla);
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        // Header-only message, so no data.
        return null;
    }

    @Override
    protected void handle() throws SecsException {
        // Not implemented.
    }

}
