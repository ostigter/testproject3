package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public class DataMessage extends Message {
    
    private final int stream;
    
    private final int function;
    
    private final boolean withReply;
    
    private final B text;

    public DataMessage(U2 sessionId, byte headerByte2, byte headerByte3, PType pType, SType sType, U4 systemBytes, B text) {
        super(sessionId, headerByte2, headerByte3, pType, sType, systemBytes);
        withReply = ((headerByte2 & 0x80) == 0x80);
        stream = (headerByte2 & 0x7f);
        function = headerByte3;
        this.text = text;
    }
    
    public int getStream() {
        return stream;
    }
    
    public int getFunction() {
        return function;
    }
    
    public boolean withReply() {
        return withReply;
    }
    
    public B getText() {
        return text;
    }
    
    @Override
    public B toB() {
        B b = new B();
        b.add(new U4(Message.MIN_LENGTH + text.length()).toB());
        b.add(getSessionId().toB());
        b.add(getHeaderByte2());
        b.add(getHeaderByte3());
        b.add(0x00);
        b.add(getSType().ordinal());
        b.add(getSystemBytes().toB());
        b.add(text);
        return b;
    }
    
    public String getType() {
        return String.format("S%02dF%02d", stream, function);
    }
    
    @Override
    public String toString() {
        return String.format("DataMessage(type = %s, withReply = %s, length = %d, systemBytes = %08x)",
                getType(), withReply, text.length(), getSystemBytes().getValue());
    }
    
}
