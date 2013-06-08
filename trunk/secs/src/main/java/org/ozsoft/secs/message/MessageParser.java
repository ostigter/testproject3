package org.ozsoft.secs.message;

import org.apache.log4j.Logger;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.SecsUtils;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public class MessageParser {
    
    private static final int MESSAGE_LENGTH_LENGTH = 4;
    
    private static final int HEADER_LENGTH = 10;
    
    private static final int MAX_MESSAGE_LENGTH = 256 * 1024; // 256 kB
    
    private static final int SESSION_ID_LENGTH = 2;
    
    private static final int SYSTEM_BYTES_LENGTH = 4;
    
    private static final int POS_SESSIONID = 0;
    
    private static final int POS_HEADERBYTE2 = 2;
    
    private static final int POS_HEADERBYTE3 = 3;
    
    private static final int POS_PTYPE = 4;
    
    private static final int POS_STYPE = 5;
    
    private static final int POS_SYSTEMBYTES = 6;
    
    private static final Logger LOG = Logger.getLogger(MessageParser.class);
    
    public static Message parse(byte[] data, int length) throws SecsException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", data[i]));
        }
        LOG.debug(length + " bytes read: " + sb);
        
        // Determine message length.
        if (length < MESSAGE_LENGTH_LENGTH) {
            throw new SecsException(String.format("Incomplete message (message length: %d)", length));
        }
        byte[] lengthField = new byte[MESSAGE_LENGTH_LENGTH];
        System.arraycopy(data, 0, lengthField, 0, MESSAGE_LENGTH_LENGTH);
        int messageLength = SecsUtils.toU4(lengthField);
        LOG.debug("Message length: " + messageLength);
        if (messageLength < HEADER_LENGTH) {
            throw new SecsException("Incomplete message (invalid header length)");
        }
        if (messageLength > MAX_MESSAGE_LENGTH) {
            throw new SecsException(String.format("Message too large (%d bytes)", messageLength));
        }
        
        // Parse message header.
        
        // Parse Session ID.
        byte[] sessionIdBuf = new byte[SESSION_ID_LENGTH];
        System.arraycopy(data, MESSAGE_LENGTH_LENGTH + POS_SESSIONID, sessionIdBuf, 0, SESSION_ID_LENGTH);
        U2 sessionId = new U2(sessionIdBuf);
        LOG.debug("Session ID = " + sessionId.getValue());
        
        // Get Header Bytes 2 and 3.
        byte headerByte2 = data[MESSAGE_LENGTH_LENGTH + POS_HEADERBYTE2];
        byte headerByte3 = data[MESSAGE_LENGTH_LENGTH + POS_HEADERBYTE3];
        LOG.debug("Header Byte 2 = " + headerByte2);
        LOG.debug("Header Byte 3 = " + headerByte3);
        
        // Parse PType.
        byte pTypeByte = data[MESSAGE_LENGTH_LENGTH + POS_PTYPE];
        PType pType = PType.parse(pTypeByte);
        LOG.debug("PType = " + pType);
        if (pType != PType.SECS_II) {
            throw new SecsException(String.format("Unsupported protocol; not SECS-II (PType: %d)", pTypeByte));
        }
        
        // Parse SType.
        byte sTypeByte = data[MESSAGE_LENGTH_LENGTH + POS_STYPE];
        SType sType = SType.parse(sTypeByte);
        LOG.debug("SType = " + sType);
        if (sType == SType.UNKNOWN) {
            throw new SecsException(String.format("Unsupported message type (SType: 0x%02X)", sTypeByte));
        }
        
        // Parse System Bytes (transaction ID).
        byte[] systemBytesBuf = new byte[SYSTEM_BYTES_LENGTH];
        System.arraycopy(data, MESSAGE_LENGTH_LENGTH + POS_SYSTEMBYTES, systemBytesBuf, 0, SYSTEM_BYTES_LENGTH);
        U4 systemBytes = new U4(systemBytesBuf);
        LOG.debug("System Bytes = " + systemBytes.getValue());
        
        Message message = null;
        return message;
    }

}
