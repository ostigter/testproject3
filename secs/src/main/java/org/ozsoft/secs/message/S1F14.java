package org.ozsoft.secs.message;

import org.ozsoft.secs.CommunicationState;
import org.ozsoft.secs.SecsEquipment;
import org.ozsoft.secs.SecsException;

/**
 * S1F14 Establish Communication Request Acknowledge (CRA) reply message.
 * 
 * @author Oscar Stigter
 */
public class S1F14 extends MessageHandler {

    private static final int STREAM = 1;

    private static final int FUNCTION = 14;
    
    private static final String DESCRIPTION = "Establish Communication Acknowledge (CRA)";

    public S1F14(SecsEquipment equipment) {
        super(STREAM, FUNCTION, DESCRIPTION, equipment);
    }

    @Override
    public DataMessage handle(DataMessage message) throws SecsException {
        //FIXME: Handle S1F14 (CRA).
        getEquipment().setCommunicationState(CommunicationState.COMMUNICATING);
        return null;
    }

}
