package org.ozsoft.secs.message;

import org.ozsoft.secs.MessageHandlerNew;
import org.ozsoft.secs.SecsException;

public class S1F1Handler extends MessageHandlerNew<S1F1New, S1F2New> {

    @Override
    public S1F2New handle(S1F1New requestMessage) throws SecsException {
        S1F2New s1f2 = new S1F2New();
        s1f2.setModelName(getEquipment().getModelName());
        s1f2.setSoftRev(getEquipment().getSoftRev());
        return s1f2;
    }

}
