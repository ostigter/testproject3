package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.SecsReplyMessage;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

/**
 * S1F2 On Line Data (D) reply message.
 * 
 * @author Oscar Stigter
 */
public class S1F2 extends SecsReplyMessage {

    private static final int STREAM = 1;

    private static final int FUNCTION = 2;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "On Line Data (D)";
    
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
            throw new SecsParseException("Top-level data item must be an L");
        }
        L l = (L) data;
        String softRev = "";
        String modelName = "";
        
        
        if (l.length() == 0) {
            softRev = "";
            modelName = "";
        } else if (l.length() == 2) {
            data = l.getItem(0);
            if (!(data instanceof A)) {
                throw new SecsParseException("MDLN must be an A");
            }
            modelName = ((A) data).getValue();
            data = l.getItem(1);
            if (!(data instanceof A)) {
                throw new SecsParseException("SOFTREV must be an A");
            }
            softRev = ((A) data).getValue();
        } else {
            throw new SecsParseException("L must contain 0 or 2 items");
        }
        setModelName(modelName);
        setSoftRev(softRev);
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
    protected void handle() {
        // Not implemented.
    }

}
