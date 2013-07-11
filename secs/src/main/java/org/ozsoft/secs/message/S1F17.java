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
 * S1F17 Request ON-LINE (RONL) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F17 extends MessageHandler {

    private static final int STREAM = 1;

    private static final int FUNCTION = 17;
    
    private static final String DESCRIPTION = "Request ON-LINE (RONL)";

    public S1F17(SecsEquipment equipment) {
        super(STREAM, FUNCTION, DESCRIPTION, equipment);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsException {
        U2 sessionId = message.getSessionId();
        U4 systemBytes = message.getSystemBytes();
        Data<?> requestText = message.getText();
        if (requestText != null) {
            throw new SecsException("Invalid data format for S1F17 message");
        }

        // Send S1F18 ON-LINE Acknowledge (ONLA).
        getEquipment().setControlState(ControlState.ONLINE_REMOTE);
        B replyText = new B(0x00); // ONLACK = ON-LINE Accepted
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, PType.SECS_II, SType.DATA, systemBytes, replyText);
    }

}
