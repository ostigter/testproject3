package org.ozsoft.secs.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public class DataMessage extends Message {
    
    private final int stream;
    
    private final int function;
    
    private final boolean withReply;
    
    private final Data<?> text;

    public DataMessage(U2 sessionId, int headerByte2, int headerByte3, PType pType, SType sType, U4 systemBytes, Data<?> text) {
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
    
    public Data<?> getText() {
        return text;
    }
    
    @Override
    public byte[] toByteArray() {
        int length = HEADER_LENGTH;
        byte[] textBytes = null;
        if (text != null) {
            textBytes = text.toByteArray();
            length += textBytes.length;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(new U4(length).toByteArray());
            baos.write(getSessionId().getBytes());
            baos.write(getHeaderByte2());
            baos.write(getHeaderByte3());
            baos.write(getPType().ordinal());
            baos.write(getSType().ordinal());
            baos.write(getSystemBytes().toByteArray());
            if (text != null) {
                baos.write(textBytes);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            // Internal error (should never happen).
            throw new RuntimeException("Could not serialize message: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
    
    public String getType() {
        return String.format("S%dF%d", stream, function);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (byte b : toByteArray()) {
            sb.append(String.format("%02x ", b));
        }
        sb.append('}');
        if (text != null) {
            sb.append('\n');
            sb.append(text.toSml());
        }
        return String.format("%s %s", getType(), sb);
    }
    
}
