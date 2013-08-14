package org.ozsoft.secs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.util.ConversionUtils;

public class ControlMessage implements Message {
    
    /** Header length bytes for a header-only message. */
    private static final byte[] HEADER_LENGTH_BYTES = new byte[] {0x00, 0x00, 0x00, 0x0a};
    
    private int sessionId;
    
    private int headerByte2;
    
    private int headerByte3;
    
    private SType sType;
    
    private long transactionId;

    public ControlMessage(int sessionId, int headerByte2, int headerByte3, SType sType, long transactionId) {
        this.sessionId = sessionId;
        this.headerByte2 = headerByte2;
        this.headerByte3 = headerByte3;
        this.sType = sType;
        this.transactionId = transactionId;
    }
    
    @Override
    public int getSessionId() {
        return sessionId;
    }
    
    public int getHeaderByte2() {
        return headerByte2;
    }
    
    public int getHeaderByte3() {
        return headerByte3;
    }
    
    public SType getSType() {
        return sType;
    }

    @Override
    public long getTransactionId() {
        return transactionId;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(HEADER_LENGTH_BYTES);
            baos.write(ConversionUtils.integerToBytes(sessionId, U2.SIZE));
            baos.write(headerByte2);
            baos.write(headerByte3);
            baos.write(PType.SECS_II.getValue());
            baos.write(sType.getValue());
            baos.write(ConversionUtils.integerToBytes(transactionId, U4.SIZE));
            return baos.toByteArray();
        } catch (IOException e) {
            // Internal error (should never happen).
            throw new RuntimeException("Could not serialize message: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : toByteArray()) {
            sb.append(String.format("%02x ", b));
        }
        return String.format("%s {%s}", sType, sb);
    }

}
