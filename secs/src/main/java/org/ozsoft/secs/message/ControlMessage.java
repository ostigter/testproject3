package org.ozsoft.secs.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public class ControlMessage extends Message {

    public ControlMessage(U2 sessionId, int headerByte2, int headerByte3, PType pType, SType sType, U4 systemBytes) {
        super(sessionId, headerByte2, headerByte3, pType, sType, systemBytes);
    }
    
    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(HEADER_LENGTH_BYTES);
            baos.write(getSessionId().toByteArray());
            baos.write(getHeaderByte2());
            baos.write(getHeaderByte3());
            baos.write(getPType().ordinal());
            baos.write(getSType().ordinal());
            baos.write(getSystemBytes().toByteArray());
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
        return String.format("%s {%s}", getSType(), sb);
    }

}
