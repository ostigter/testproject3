package org.ozsoft.secs.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.util.ConversionUtils;

public class DataMessage implements Message {
    
    private final int sessionId;
    
    private final int stream;
    
    private final int function;
    
    private final boolean withReply;
    
    private final long transactionId;
    
    private final Data<?> text;

    public DataMessage(int sessionId, int stream, int function, boolean withReply, long transactionId, Data<?> text) {
        this.sessionId = sessionId;
        this.stream = stream;
        this.function = function;
        this.withReply = withReply;
        this.transactionId = transactionId;
        this.text = text;
    }
    
    @Override
    public int getSessionId() {
        return sessionId;
    }

    @Override
    public long getTransactionId() {
        return transactionId;
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
            baos.write(ConversionUtils.integerToBytes(length, U4.MIN_LENGTH));
            baos.write(ConversionUtils.integerToBytes(sessionId, U2.MIN_LENGTH));
            baos.write((withReply) ? stream | 0x80 : stream); // HeaderByte2
            baos.write(function); // HeaderByte3
            baos.write(PType.SECS_II.getValue());
            baos.write(SType.DATA.getValue());
            baos.write(ConversionUtils.integerToBytes(transactionId, U4.MIN_LENGTH));
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
