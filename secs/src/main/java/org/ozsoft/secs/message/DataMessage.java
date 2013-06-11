package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public class DataMessage extends Message {

    public DataMessage(U2 sessionId, byte headerByte2, byte headerByte3, PType pType, SType sType, U4 systemBytes) {
        super(sessionId, headerByte2, headerByte3, pType, sType, systemBytes);
    }
    
    @Override
    public String toString() {
        return String.format("DataMessage(systemBytes = %d)", getSystemBytes().getValue());
    }

    @Override
    public B toB() {
        //TODO: Add message text.
        B b = new B();
        b.add(LENGTH_BYTES);
        b.add(getSessionId().toB());
        b.add(getHeaderByte2());
        b.add(getHeaderByte3());
        b.add(0);
        b.add(getSType().ordinal());
        b.add(getSystemBytes().toB());
        return b;
    }

}
