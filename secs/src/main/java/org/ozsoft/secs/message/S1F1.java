package org.ozsoft.secs.message;

import org.ozsoft.secs.SecsEquipment;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

/**
 * S1F1 Are You There (R) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F1 extends MessageHandler {

    private static final int STREAM = 1;

    private static final int FUNCTION = 1;
    
    private static final String DESCRIPTION = "Are You There (R)";

    public S1F1(SecsEquipment equipment) {
        super(STREAM, FUNCTION, DESCRIPTION, equipment);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsException {
        int sessionId = message.getSessionId();
        long transactionId = message.getTransactionId();
        Data<?> requestText = message.getText();
        if (requestText != null) {
            throw new SecsException("Invalid data format for S1F1 message");
        }

        // Send S1F2 On Line Data (D).
        L replyText = new L();
        replyText.addItem(new A(getEquipment().getModelName()));
        replyText.addItem(new A(getEquipment().getSoftRev()));
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, false, transactionId, replyText);
    }

}
