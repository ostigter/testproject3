package org.ozsoft.secs.message;

import org.ozsoft.secs.CommunicationState;
import org.ozsoft.secs.SecsEquipment;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

/**
 * S1F13 Establish Communication Request (CR) request message.
 * 
 * @author Oscar Stigter
 */
public class S1F13 extends MessageHandler {
    
    private static final int STREAM = 1;

    private static final int FUNCTION = 13;
    
    private static final String DESCRIPTION = "Establish Communication Request (CR)";

    public S1F13(SecsEquipment equipment) {
        super(STREAM, FUNCTION, DESCRIPTION, equipment);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsParseException {
        int sessionId = message.getSessionId();
        long transactionId = message.getTransactionId();
        Data<?> requestText = message.getText();
        if (!(requestText instanceof L)) {
            throw new SecsParseException("Invalid data format for S1F13 message");
        }
        L l = (L) requestText;
//        String mdln = null;
//        String softrev = null;
        if (l.length() == 0) {
            // No MDLN and SOFTREV specified.
//            mdln = "";
//            softrev = "";
        } else if (l.length() == 2) {
            Data<?> dataItem = l.getItem(0);
            if (!(dataItem instanceof A)) {
                throw new SecsParseException("Invalid data format for S1F13 message");
            }
//            mdln = ((A) dataItem).getValue();
            dataItem = l.getItem(1);
            if (!(dataItem instanceof A)) {
                throw new SecsParseException("Invalid data format for S1F13 message");
            }
//            softrev = ((A) dataItem).getValue();
        } else {
            throw new SecsParseException("Invalid data format for S1F13 message");
        }
        
        // Send S1F14 Establish Communication Request Acknowledge (CRA).
        int commack = -1;
        if (getEquipment().getCommunicationState() != CommunicationState.COMMUNICATING) {
            commack = 0x00; // COMMACK = Accepted
            getEquipment().setCommunicationState(CommunicationState.COMMUNICATING);
        } else {
            commack = 0x01; // COMMACK = Denied, Try Again
        }
        L replyText = new L();
        replyText.addItem(new B(commack));
        l = new L();
        l.addItem(new A(getEquipment().getModelName()));
        l.addItem(new A(getEquipment().getSoftRev()));
        replyText.addItem(l);
        return new DataMessage(sessionId, STREAM, FUNCTION + 1, false, transactionId, replyText);
    }

}
