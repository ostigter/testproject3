package org.ozsoft.secs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.util.ConversionUtils;

public abstract class SecsMessage extends Message {
    
    private static final int WITH_REPLY_MASK = 0x80;

    public abstract int getStream();
    
    public abstract int getFunction();
    
    public abstract boolean withReply();
    
    public abstract String getDescripton();
    
    public final String getType() {
        return String.format("S%dF%d", getStream(), getFunction());
    }
    
    protected abstract void parseData(Data<?> data) throws SecsParseException;
    
    protected abstract Data<?> getData() throws SecsParseException;
    
    @Override
    /* package */ final byte[] toByteArray() throws SecsParseException {
        int length = SecsConstants.HEADER_LENGTH;
        Data<?> data = getData();
        byte[] dataBytes = null;
        if (data != null) {
            dataBytes = data.toByteArray();
            length += dataBytes.length;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(ConversionUtils.integerToBytes(length, U4.SIZE));
            baos.write(ConversionUtils.integerToBytes(getSessionId(), U2.SIZE));
            baos.write((withReply()) ? getStream() | WITH_REPLY_MASK : getStream()); // HeaderByte2
            baos.write(getFunction()); // HeaderByte3
            baos.write(PType.SECS_II.getValue());
            baos.write(SType.DATA.getValue());
            baos.write(ConversionUtils.integerToBytes(getTransactionId(), U4.SIZE));
            if (data != null) {
                baos.write(dataBytes);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            // Internal error (should never happen).
            throw new SecsParseException("Could not serialize data message", e);
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
    
    @Override
    public String toString() {
//        return String.format("%s - %s", getType(), getDescripton());
        StringBuilder sb = new StringBuilder();
        try {
            for (byte b : toByteArray()) {
                sb.append(String.format("%02x ", b));
            }
        } catch (SecsParseException e) {
            throw new RuntimeException("Could not serialize message", e);
        }
        return String.format("%s - %s {%s}", getType(), getDescripton(), sb);
    }
    
}
