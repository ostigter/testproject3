package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

/**
 * S2F25 Loopback Diagnostic Request (LDR) request message.
 * 
 * @author Oscar Stigter
 */
public class S2F25 extends MessageHandler {

    private static final int STREAM = 1;

    private static final int FUNCTION = 25;

    private static final String DESCRIPTION = "Loopback Diagnostic Request (LDR)";

    public S2F25() {
        super(STREAM, FUNCTION, DESCRIPTION);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsException {
        U2 sessionId = message.getSessionId();
        U4 systemBytes = message.getSystemBytes();
        Data<?> requestText = message.getText();
        if (!(requestText instanceof B)) {
            throw new SecsException("Invalid data format for S2F25 message");
        }

        // Send S2F26 Loopback Diagnostic Data (LDD).
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, PType.SECS_II, SType.DATA, systemBytes, requestText);
    }

}
