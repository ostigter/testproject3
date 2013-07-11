package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

/**
 * S1F13 Establish Communication Request (CR) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F13 extends MessageHandler {
    
    private static final int STREAM = 1;

    private static final int FUNCTION = 13;
    
    private static final boolean WITH_REPLAY = true;

    private static final String MDLN = "SECS Server";
    
    private static final String SOFTREV = "1.0";
    
    public S1F13() {
        super(STREAM, FUNCTION, WITH_REPLAY);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsException {
        U2 sessionId = message.getSessionId();
        U4 systemBytes = message.getSystemBytes();
        Data<?> requestText = message.getText();
        if (!(requestText instanceof L)) {
            throw new SecsException("Invalid data format for S1F13 message");
        }
        L l = (L) requestText;
        String mdln = null;
        String softrev = null;
        if (l.length() == 0) {
            // No MDLN and SOFTREV specified.
            mdln = "";
            softrev = "";
        } else if (l.length() == 2) {
            Data<?> dataItem = l.getItem(0);
            if (!(dataItem instanceof A)) {
                throw new SecsException("Invalid data format for S1F13 message");
            }
            mdln = ((A) dataItem).getValue();
            dataItem = l.getItem(1);
            if (!(dataItem instanceof A)) {
                throw new SecsException("Invalid data format for S1F13 message");
            }
            softrev = ((A) dataItem).getValue();
        } else {
            throw new SecsException("Invalid data format for S1F13 message");
        }
        
        // Send S1F14 Establish Communication Request Acknowledge (CRA).
        L replyText = new L();
        replyText.addItem(new B(0x00)); // COMMACK = Accepted
        l = new L();
        l.addItem(new A(MDLN));
        l.addItem(new A(SOFTREV));
        replyText.addItem(l);
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, PType.SECS_II, SType.DATA, systemBytes, replyText);
    }

}
