package org.ozsoft.secs.message;

import org.apache.log4j.Logger;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public class MessageParser {
    
    private static final Logger LOG = Logger.getLogger(MessageParser.class);
    
    public static Message parse(byte[] data, int length) throws SecsException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x ", data[i]));
        }
        LOG.debug(length + " bytes read: " + sb);
        
        // Determine message length.
        if (length < Message.LENGTH_LENGTH) {
            throw new SecsException(String.format("Incomplete message (message length: %d)", length));
        }
        byte[] lengthField = new byte[Message.LENGTH_LENGTH];
        System.arraycopy(data, 0, lengthField, 0, Message.LENGTH_LENGTH);
        int messageLength = (int) new U4(lengthField).getValue();
//        LOG.debug("Message length: " + messageLength);
        if (messageLength < Message.HEADER_LENGTH) {
            throw new SecsException("Incomplete message (invalid header length)");
        }
        if (messageLength > Message.MAX_LENGTH) {
            throw new SecsException(String.format("Message too large (%d bytes)", messageLength));
        }
        
        // Parse message header.
        
        // Parse Session ID.
        byte[] sessionIdBuf = new byte[Message.SESSION_ID_LENGTH];
        System.arraycopy(data, Message.LENGTH_LENGTH + Message.POS_SESSIONID, sessionIdBuf, 0, Message.SESSION_ID_LENGTH);
        U2 sessionId = new U2(sessionIdBuf);
//        LOG.debug(String.format("System Bytes = %04x", sessionId.getValue()));
        
        // Get Header Bytes 2 and 3.
        byte headerByte2 = data[Message.LENGTH_LENGTH + Message.POS_HEADERBYTE2];
        byte headerByte3 = data[Message.LENGTH_LENGTH + Message.POS_HEADERBYTE3];
//        LOG.debug(String.format("Header Byte 2 = %02x", headerByte2));
//        LOG.debug(String.format("Header Byte 3 = %02x", headerByte3));
        
        // Parse PType.
        byte pTypeByte = data[Message.LENGTH_LENGTH + Message.POS_PTYPE];
        PType pType = PType.parse(pTypeByte);
//        LOG.debug("PType = " + pType);
        if (pType != PType.SECS_II) {
            throw new SecsException(String.format("Unsupported protocol; not SECS-II (PType: %d)", pTypeByte));
        }
        
        // Parse SType.
        byte sTypeByte = data[Message.LENGTH_LENGTH + Message.POS_STYPE];
        SType sType = SType.parse(sTypeByte);
//        LOG.debug("SType = " + sType);
        if (sType == SType.UNKNOWN) {
            throw new SecsException(String.format("Unsupported message type (SType: %02x)", sTypeByte));
        }
        
        // Parse System Bytes (transaction ID).
        byte[] systemBytesBuf = new byte[Message.SYSTEM_BYTES_LENGTH];
        System.arraycopy(data, Message.LENGTH_LENGTH + Message.POS_SYSTEMBYTES, systemBytesBuf, 0, Message.SYSTEM_BYTES_LENGTH);
        U4 systemBytes = new U4(systemBytesBuf);
//        LOG.debug(String.format("System Bytes = %08x", systemBytes.getValue()));
        
        if (sType == SType.DATA) {
            int dataLength = messageLength - Message.HEADER_LENGTH;
            byte[] text = new byte[dataLength];
            System.arraycopy(data, Message.MIN_LENGTH, text, 0, dataLength);
//            LOG.debug("Data bytes: " + new B(text));
            return new DataMessage(sessionId, headerByte2, headerByte3, pType, sType, systemBytes, new B(text));
        } else {
            return new ControlMessage(sessionId, headerByte2, headerByte3, pType, sType, systemBytes);
        }
    }

}
