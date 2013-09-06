package org.ozsoft.secs.message;

import org.ozsoft.secs.CommunicationState;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsPrimaryMessage;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

/**
 * S1F13 Establish Communication Request (CR) primary message. <br />
 * <br />
 * 
 * Format:
 * <pre>
 * <L
 *      MDLN            // A:20
 *      SOFTREV         // A:20
 * >
 * </pre>
 * 
 * @author Oscar Stigter
 */
public class S1F13 extends SecsPrimaryMessage {
    
    private static final int STREAM = 1;

    private static final int FUNCTION = 13;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Establish Communication Request (CR)";

    private static final int COMMACK_ACCEPTED = 0x00;

    private String modelName;
    
    private String softRev;
    
    public String getModelName() {
        return modelName;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public String getSoftRev() {
        return softRev;
    }
    
    public void setSoftRev(String softRev) {
        this.softRev = softRev;
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
        if (!(data instanceof L)) {
            throw new SecsParseException("Root data item must be of type L");
        }
        L l = (L) data;
        if (l.length() == 0) {
            // No MDLN and SOFTREV specified; use empty values.
            setModelName("");
            setSoftRev("");
        } else if (l.length() == 2) {
            Data<?> dataItem = l.getItem(0);
            if (!(dataItem instanceof A)) {
                throw new SecsParseException("MDLN must be of type A");
            }
            String modelName = ((A) dataItem).getValue();
            dataItem = l.getItem(1);
            if (!(dataItem instanceof A)) {
                throw new SecsParseException("SOFTREV must be of type A");
            }
            String softRev = ((A) dataItem).getValue();
            setModelName(modelName);
            setSoftRev(softRev);
        } else {
            throw new SecsParseException("L must contain exactly 2 items");
        }
    }

    @Override
    protected Data<?> getData() throws SecsParseException {
        if (modelName == null) {
            throw new SecsParseException("MDLN not set");
        }
        if (softRev == null) {
            throw new SecsParseException("SOFTREV not set");
        }
        
        L l = new L();
        l.addItem(new A(modelName));
        l.addItem(new A(softRev));
        return l;
    }

    @Override
    protected SecsReplyMessage handle() {
        // Always accept.
        getEquipment().setCommunicationState(CommunicationState.COMMUNICATING);
        S1F14 s1f14 = new S1F14();
        s1f14.setCommAck(COMMACK_ACCEPTED);
        s1f14.setModelName(getEquipment().getModelName());
        s1f14.setSoftRev(getEquipment().getSoftRev());
        return s1f14;
    }

}
