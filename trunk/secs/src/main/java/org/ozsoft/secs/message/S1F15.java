package org.ozsoft.secs.message;

import org.ozsoft.secs.ControlState;
import org.ozsoft.secs.SecsEquipment;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;

/**
 * S1F15 Request OFF-LINE (ROFL) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F15 extends MessageHandler {

    private static final int STREAM = 1;

    private static final int FUNCTION = 15;
    
    private static final String DESCRIPTION = "Request OFF-LINE (ROFL)";

    public S1F15(SecsEquipment equipment) {
        super(STREAM, FUNCTION, DESCRIPTION, equipment);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsParseException {
        int sessionId = message.getSessionId();
        long transactionId = message.getTransactionId();
        Data<?> requestText = message.getText();
        if (requestText != null) {
            throw new SecsParseException("Invalid data format for S1F15 message");
        }
        
        // Send S1F16 OFF-LINE Acknowledge (OFLA).
        getEquipment().setControlState(ControlState.HOST_OFFLINE);
        B replyText = new B(0x00); // OFLACK = OFF-LINE Acknowledge
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, false, transactionId, replyText);
    }

}
