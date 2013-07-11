package org.ozsoft.secs.message;

import org.ozsoft.secs.ControlState;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsEquipment;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

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
    public DataMessage handle(DataMessage message) throws SecsException {
        U2 sessionId = message.getSessionId();
        U4 systemBytes = message.getSystemBytes();
        Data<?> requestText = message.getText();
        if (requestText != null) {
            throw new SecsException("Invalid data format for S1F15 message");
        }
        
        // Send S1F16 OFF-LINE Acknowledge (OFLA).
        getEquipment().setControlState(ControlState.HOST_OFFLINE);
        B replyText = new B(0x00); // OFLACK = OFF-LINE Acknowledge
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, PType.SECS_II, SType.DATA, systemBytes, replyText);
    }

}
