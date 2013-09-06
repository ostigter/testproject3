package org.ozsoft.secs.message;

import org.ozsoft.secs.ControlState;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsPrimaryMessage;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.Data;

/**
 * S1F17 Request ON-LINE (RONL) request message. <br />
 * <br />
 * 
 * This message does not have any data.
 * 
 * @author Oscar Stigter
 */
public class S1F17 extends SecsPrimaryMessage {

    private static final int STREAM = 1;

    private static final int FUNCTION = 17;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Request ON-LINE (RONL)";

    /** ONLACK: Acknowledge. */
    private static final int ACKNOWLEDGE = 0x00;

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
        if (data != null) {
            throw new SecsParseException("Message must not contain any data");
        }
    }

    @Override
    protected Data<?> getData() {
        // Header-only message, so no data.
        return null;
    }

    @Override
    protected SecsReplyMessage handle() {
        // Always acknowledge request.
        getEquipment().setControlState(ControlState.ONLINE_REMOTE);
        S1F18 s1f18 = new S1F18();
        s1f18.setOnlAck(ACKNOWLEDGE);
        return s1f18;
    }
    
}
