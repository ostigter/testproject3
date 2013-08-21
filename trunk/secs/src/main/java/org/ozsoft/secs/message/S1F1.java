package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsPrimaryMessage;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.Data;

/**
 * S1F1 Are You There (R) primary message.
 * 
 * @author Oscar Stigter
 */
public class S1F1 extends SecsPrimaryMessage {

    private static final int STREAM = 1;

    private static final int FUNCTION = 1;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Are You There (R)";
    
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
            throw new SecsParseException("No data expected");
        }
    }

    @Override
    protected Data<?> getData() {
        // No data.
        return null;
    }

    @Override
    protected SecsReplyMessage handle() throws SecsException {
        S1F2 s1f2 = new S1F2();
        s1f2.setModelName(getEquipment().getModelName());
        s1f2.setSoftRev(getEquipment().getSoftRev());
        return s1f2;
    }

}
