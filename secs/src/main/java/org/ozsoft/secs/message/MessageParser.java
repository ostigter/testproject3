package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.BOOLEAN;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public class MessageParser {
    
//    private static final Logger LOG = Logger.getLogger(MessageParser.class);
    
    public static Message parse(byte[] data, int length) throws SecsException {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            sb.append(String.format("%02x ", data[i]));
//        }
//        LOG.debug(length + " bytes read: " + sb);
        
        // Determine message length.
        if (length < Message.HEADER_LENGTH) {
            throw new SecsException(String.format("Incomplete message (message length: %d)", length));
        }
        byte[] lengthField = new byte[Message.LENGTH_LENGTH];
        System.arraycopy(data, 0, lengthField, 0, Message.LENGTH_LENGTH);
        long messageLength = new U4(lengthField).getValue();
//        LOG.debug("Message length: " + messageLength);
        if (length < (messageLength + Message.LENGTH_LENGTH)) {
            throw new SecsException(String.format("Incomplete message (declared length: %d; actual length: %d)",
                    messageLength + Message.LENGTH_LENGTH, length));
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
//        LOG.debug(String.format("System Bytes = %d", systemBytes.getValue()));
        
        if (sType == SType.DATA) {
            int dataLength = (int) (messageLength - Message.HEADER_LENGTH);
            Data<?> text = null;
            if (dataLength > 0) {
                byte[] textBytes = new byte[dataLength];
                System.arraycopy(data, Message.MIN_LENGTH, textBytes, 0, dataLength);
                text = parseText(textBytes, 0);
            }
            return new DataMessage(sessionId, headerByte2, headerByte3, pType, sType, systemBytes, text);
        } else {
            return new ControlMessage(sessionId, headerByte2, headerByte3, pType, sType, systemBytes);
        }
    }
    
    public static Data<?> parseData(String text) throws SecsException {
        if (text == null || text.isEmpty()) {
            throw new SecsException("Empty data item");
        }
        
        boolean inValue = false;
        String type = null;
        String value = null;
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!inValue) {
                // Determining type.
                if (c == '{') {
                    // Type found; start of value.
                    type = sb.toString().trim();
                    inValue = true;
                    sb.delete(0, sb.length());
                } else {
                    sb.append(c);
                }
            } else {
                // Determining value.
                if (c == '{') {
                    // Inner nesting; ignore.
                    depth++;
                    sb.append(c);
                } else if (c == '}') {
                    if (depth > 0) {
                        // Still in nested part; ignore.
                        depth--;
                        sb.append(c);
                    } else {
                        // Value found.
                        value = sb.toString();
                        inValue = false;
                        sb.delete(0, sb.length());
                    }
                } else {
                    sb.append(c);
                }
            }
        }
        
        return parseData(type, value);
    }
    
    private static Data<?> parseData(String type, String value) throws SecsException {
        Data<?> data = null;
        
        if (type.equals("L")) {
            data = parseL(value);
        } else if (type.equals("B")) {
            B b = new B();
            try {
                for (String s : value.split("\\s")) {
                    b.add(Byte.parseByte(s));
                }
            } catch (NumberFormatException e) {
                throw new SecsException("Invalid B value: " + value);
            }
            data = b;
        } else if (type.equals("BOOLEAN")) {
            if (value.equals("True")) {
                data = new BOOLEAN(true);
            } else if (value.equals("False")) {
                data = new BOOLEAN(false);
            } else {
                throw new SecsException("Invalid BOOLEAN value: " + value);
            }
        } else if (type.equals("A")) {
            data = new A(value);
        } else if (type.equals("U2")) {
            try {
                data = new U2(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new SecsException("Invalid U2 value: " + value);
            }
        } else if (type.equals("U4")) {
            try {
                data = new U4(Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new SecsException("Invalid U4 value: " + value);
            }
        } else {
            throw new SecsException("Invalid data type: " + type);
        }
        
        return data;
    }
    
    //TODO: Merge parseL(String) into parse(String).
    private static L parseL(String text) throws SecsException {
        L l = new L();
        if (!text.isEmpty()) {
            boolean inValue = false;
            String type = null;
            String value = null;
            int depth = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (!inValue) {
                    // Determining type.
                    if (c == '{') {
                        // Type found; start of value.
                        type = sb.toString().trim();
                        inValue = true;
                        sb.delete(0, sb.length());
                    } else {
                        sb.append(c);
                    }
                } else {
                    // Determining value.
                    if (c == '{') {
                        // Inner nesting; ignore.
                        depth++;
                        sb.append(c);
                    } else if (c == '}') {
                        if (depth > 0) {
                            // Still in nested part; ignore.
                            depth--;
                            sb.append(c);
                        } else {
                            // Value found.
                            value = sb.toString();
                            l.addItem(parseData(type, value));
                            inValue = false;
                            sb.delete(0, sb.length());
                        }
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return l;
    }

    private static Data<?> parseText(byte[] text, int offset) throws SecsException {
        if (text.length < 2) {
            throw new SecsException("Invalid data length: " + text.length);
        }
        
        int formatByte = text[offset];
        int formatCode = (formatByte & 0xfc);
        int noOfLengthBytes = formatByte & 0x03;
//        LOG.debug(String.format("formatByte = %02x", formatByte));
//        LOG.debug(String.format("formatCode = %02x", formatCode));
//        LOG.debug(String.format("noOfLengthBytes = %d", noOfLengthBytes));
        if (noOfLengthBytes < 1 || noOfLengthBytes > 4) {
            throw new SecsException("Invalid number of length bytes: " + noOfLengthBytes);
        }
        if (text.length < noOfLengthBytes + 1) {
            throw new SecsException("Incomplete message data");
        }
        int length = text[offset + 1];
        if (noOfLengthBytes > 1) {
            length |= (text[offset + 2] << 8);
        }
        if (noOfLengthBytes > 2) {
            length |= (text[offset + 3] << 16);
        }
        if (noOfLengthBytes > 3) {
            length |= (text[offset + 4] << 24);
        }
//        LOG.debug(String.format("length = %d", length));
        if (text.length < offset + 2 + length) {
            throw new SecsException("Incomplete message data");
        }
        
        Data<?> dataItem = null;
        offset += 1 + noOfLengthBytes;
        switch (formatCode) {
            case L.FORMAT_CODE:
                dataItem = parseL(text, offset, length);
                break;
            case B.FORMAT_CODE:
                dataItem = parseB(text, offset, length);
                break;
            case BOOLEAN.FORMAT_CODE:
                dataItem = parseBoolean(text, offset, length);
                break;
            case A.FORMAT_CODE:
                dataItem = parseA(text, offset, length);
                break;
            case U2.FORMAT_CODE:
                dataItem = parseU2(text, offset, length);
                break;
            default:
                throw new IllegalArgumentException("Invalid format code in message data: " + formatCode);
        }
        return dataItem;
    }
    
    private static L parseL(byte[] data, int offset, int length) throws SecsException {
        L l = new L();
        for (int i = 0; i < length; i++) {
            Data<?> item = parseText(data, offset);
            l.addItem(item);
            offset += item.toByteArray().length;
        }
        return l;
    }
    
    private static B parseB(byte[] data, int offset, int length) {
        B b = new B();
        for (int i = 0; i < length; i++) {
            b.add(data[offset + i]);
        }
        return b;
    }

    private static BOOLEAN parseBoolean(byte[] data, int offset, int length) throws SecsException {
        if (length != BOOLEAN.LENGTH) {
            throw new SecsException("Invalid BOOLEAN length: " + length);
        }
        return new BOOLEAN(data[offset]);
    }

    private static A parseA(byte[] data, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) data[offset + i]);
        }
        return new A(sb.toString());
    }

    private static U2 parseU2(byte[] data, int offset, int length) throws SecsException {
        if (length != U2.LENGTH) {
            throw new SecsException("Invalid U2 length: " + length);
        }
        return new U2(new byte[] {data[offset], data[offset + 1]});
    }
    
}
