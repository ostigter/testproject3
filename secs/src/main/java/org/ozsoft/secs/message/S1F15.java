package org.ozsoft.secs.message;

import org.ozsoft.secs.ControlState;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsPrimaryMessage;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.Data;

/**
 * S1F15 Request OFF-LINE (ROFL) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F15 extends SecsPrimaryMessage {

    public static final int OFLA_ACKNOWLEDGE = 0x00;

    private static final int STREAM = 1;

    private static final int FUNCTION = 15;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Request OFF-LINE (ROFL)";

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
    public void parseData(Data<?> data) throws SecsParseException {
        if (data != null) {
            throw new SecsParseException("Message must not contain any data");
        }
    }

    @Override
    public Data<?> getData() throws SecsParseException {
        // Header-only message, so no data.
        return null;
    }

    @Override
    protected SecsReplyMessage handle() throws SecsException {
        // Always accept.
        getEquipment().setControlState(ControlState.HOST_OFFLINE);
        S1F16 s1f16 = new S1F16();
        s1f16.setOfla(OFLA_ACKNOWLEDGE);
        return s1f16;
    }
    
}
