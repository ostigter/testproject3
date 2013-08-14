package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.SecsMessage;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

/**
 * S1F1 Are You There (R) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F2New extends SecsMessage {

    public static final int STREAM = 1;

    public static final int FUNCTION = 2;
    
    public static final boolean WITH_REPLY = false;
    
    public static final String DESCRIPTION = "On Line Data (D)";
    
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
    public void parseData(Data<?> data) throws SecsParseException {
        if (data == null) {
            throw new SecsParseException("Missing data");
        }
        if (!(data instanceof L)) {
            throw new SecsParseException("Top-level data item must be an L");
        }
        L l = (L) data;
        if (l.length() != 2) {
            throw new SecsParseException("L must contain exactly 2 items");
        }
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
    }

    @Override
    public SecsMessage handle(int sessionId, long transactionId) throws SecsException {
        // Reply message, so not implemented.
        return null;
    }

    @Override
    public Data<?> getData() throws SecsParseException {
        if (modelName == null) {
            throw new SecsParseException("modelName not set");
        }
        if (softRev == null) {
            throw new SecsParseException("softRev not set");
        }
        L l = new L();
        l.addItem(new A(modelName));
        l.addItem(new A(softRev));
        return l;
    }

}
