package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public class ControlMessage extends Message {

    public ControlMessage(U2 sessionId, byte headerByte2, byte headerByte3, PType pType, SType sType, U4 systemBytes) {
        super(sessionId, headerByte2, headerByte3, pType, sType, systemBytes);
    }
    
    @Override
    public B toB() {
        B b = new B();
        b.add(LENGTH_BYTES);
        b.add(getSessionId().toB());
        b.add(getHeaderByte2());
        b.add(getHeaderByte3());
        b.add(0x00);
        b.add(getSType().ordinal());
        b.add(getSystemBytes().toByteArray());
        return b;
    }
    
    @Override
    public String toString() {
        return String.format("ControlMessage(SType = %s, systemBytes = %08d)", getSType(), getSystemBytes().getValue());
    }

}
