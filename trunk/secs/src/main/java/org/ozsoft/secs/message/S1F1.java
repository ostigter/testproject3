package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

/**
 * S1F1 Are You There (R) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F1 extends MessageHandler {

    private static final int STREAM = 1;

    private static final int FUNCTION = 1;

    private static final boolean WITH_REPLAY = true;

    private static final String MDLN = "SECS Server";
    
    private static final String SOFTREV = "1.0";
    
    public S1F1() {
        super(STREAM, FUNCTION, WITH_REPLAY);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsException {
        U2 sessionId = message.getSessionId();
        U4 systemBytes = message.getSystemBytes();
        Data<?> requestText = message.getText();
        if (requestText != null) {
            throw new SecsException("Invalid data format for S1F1 message");
        }

        // Send S1F2 On Line Data (D).
        L replyText = new L();
        replyText.addItem(new A(MDLN));
        replyText.addItem(new A(SOFTREV));
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, PType.SECS_II, SType.DATA, systemBytes, replyText);
    }

}
