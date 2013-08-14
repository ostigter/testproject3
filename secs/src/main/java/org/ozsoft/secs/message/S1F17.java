package org.ozsoft.secs.message;

import org.ozsoft.secs.ControlState;
import org.ozsoft.secs.DataMessage;
import org.ozsoft.secs.MessageHandler;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;

/**
 * S1F17 Request ON-LINE (RONL) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F17 extends MessageHandler {

    private static final int STREAM = 1;

    private static final int FUNCTION = 17;
    
    private static final String DESCRIPTION = "Request ON-LINE (RONL)";

    public S1F17() {
        super(STREAM, FUNCTION, DESCRIPTION);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsParseException {
        int sessionId = message.getSessionId();
        long transactionId = message.getTransactionId();
        Data<?> requestText = message.getText();
        if (requestText != null) {
            throw new SecsParseException("Invalid data format for S1F17 message");
        }

        // Send S1F18 ON-LINE Acknowledge (ONLA).
        getEquipment().setControlState(ControlState.ONLINE_REMOTE);
        B replyText = new B(0x00); // ONLACK = ON-LINE Accepted
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, false, transactionId, replyText);
    }

}
