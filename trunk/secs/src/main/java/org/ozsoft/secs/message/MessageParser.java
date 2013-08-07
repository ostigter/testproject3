package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsConstants;
import org.ozsoft.secs.SecsParseException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.BOOLEAN;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.F4;
import org.ozsoft.secs.format.F8;
import org.ozsoft.secs.format.I1;
import org.ozsoft.secs.format.I2;
import org.ozsoft.secs.format.I4;
import org.ozsoft.secs.format.I8;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.format.U1;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.format.U8;
import org.ozsoft.secs.util.ConversionUtils;

public class MessageParser {
    
    private static final int LENGTH_LENGTH = 4;
    
//    private static final byte[] HEADER_LENGTH_BYTES = new byte[] {0, 0, 0, (byte) SecsConstants.HEADER_LENGTH};
    
    private static final int MIN_LENGTH = LENGTH_LENGTH + SecsConstants.HEADER_LENGTH;
    
    private static final long MAX_LENGTH = 256 * 1024; // 256 kB
    
    private static final int SESSION_ID_LENGTH = 2;
    
    private static final int SYSTEM_BYTES_LENGTH = 4;
    
    private static final int POS_SESSIONID = 0;
    private static final int POS_HEADERBYTE2 = 2;
    private static final int POS_HEADERBYTE3 = 3;
    private static final int POS_PTYPE = 4;
    private static final int POS_STYPE = 5;
    private static final int POS_SYSTEMBYTES = 6;
    
    public static Message parse(byte[] data, int length) throws SecsParseException {
        // Determine message length.
        if (length < SecsConstants.HEADER_LENGTH) {
            throw new SecsParseException(String.format("Incomplete message (message length: %d)", length));
        }
        byte[] lengthField = new byte[LENGTH_LENGTH];
        System.arraycopy(data, 0, lengthField, 0, LENGTH_LENGTH);
        long messageLength = new U4(lengthField).getValue(0);
        if (length < (messageLength + LENGTH_LENGTH)) {
            throw new SecsParseException(String.format("Incomplete message (declared length: %d; actual length: %d)",
                    messageLength + LENGTH_LENGTH, length));
        }
        if (messageLength > MAX_LENGTH) {
            throw new SecsParseException(String.format("Message too large (%d bytes)", messageLength));
        }
        
        // Parse message header.
        
        // Parse Session ID.
        byte[] sessionIdBuf = new byte[SESSION_ID_LENGTH];
        System.arraycopy(data, LENGTH_LENGTH + POS_SESSIONID, sessionIdBuf, 0, SESSION_ID_LENGTH);
        int sessionId = (int) ConversionUtils.bytesToUnsignedInteger(sessionIdBuf);
        
        // Get Header Bytes.
        byte headerByte2 = data[LENGTH_LENGTH + POS_HEADERBYTE2];
        byte headerByte3 = data[LENGTH_LENGTH + POS_HEADERBYTE3];
        
        // Parse PType.
        byte pTypeByte = data[LENGTH_LENGTH + POS_PTYPE];
        PType pType = PType.parse(pTypeByte);
        if (pType != PType.SECS_II) {
            throw new SecsParseException(String.format("Unsupported protocol; not SECS-II (PType: %d)", pTypeByte));
        }
        
        // Parse SType.
        byte sTypeByte = data[LENGTH_LENGTH + POS_STYPE];
        SType sType = SType.parse(sTypeByte);
        if (sType == SType.UNKNOWN) {
            throw new SecsParseException(String.format("Unsupported message type (SType: %02x)", sTypeByte));
        }
        
        // Parse Transaction ID.
        byte[] transactionIdBuf = new byte[SYSTEM_BYTES_LENGTH];
        System.arraycopy(data, LENGTH_LENGTH + POS_SYSTEMBYTES, transactionIdBuf, 0, SYSTEM_BYTES_LENGTH);
        long transactionId = ConversionUtils.bytesToUnsignedInteger(transactionIdBuf);
        
        if (sType == SType.DATA) {
            int dataLength = (int) (messageLength - SecsConstants.HEADER_LENGTH);
            Data<?> text = null;
            if (dataLength > 0) {
                byte[] textBytes = new byte[dataLength];
                System.arraycopy(data, MIN_LENGTH, textBytes, 0, dataLength);
                text = parseText(textBytes, 0);
            }
            int stream = headerByte2 & 0x7f;
            int function = headerByte3;
            boolean withReply = ((headerByte2 & 0x80) == 0x80);
            return new DataMessage(sessionId, stream, function, withReply, transactionId, text);
        } else {
            return new ControlMessage(sessionId, headerByte2, headerByte3, sType, transactionId);
        }
    }
    
    public static Data<?> parseData(String text) throws SecsParseException {
        if (text == null || text.isEmpty()) {
            throw new SecsParseException("Empty data item");
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
    
    private static Data<?> parseData(String type, String value) throws SecsParseException {
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
                throw new SecsParseException("Invalid B value: " + value);
            }
            data = b;
        } else if (type.equals("BOOLEAN")) {
            if (value.equals("True")) {
                data = new BOOLEAN(true);
            } else if (value.equals("False")) {
                data = new BOOLEAN(false);
            } else {
                throw new SecsParseException("Invalid BOOLEAN value: " + value);
            }
        } else if (type.equals("A")) {
            data = new A(value);
        } else if (type.equals("U2")) {
            try {
                data = new U2(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new SecsParseException("Invalid U2 value: " + value);
            }
        } else if (type.equals("U4")) {
            try {
                data = new U4(Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new SecsParseException("Invalid U4 value: " + value);
            }
        } else {
            throw new SecsParseException("Invalid data type: " + type);
        }
        
        return data;
    }
    
    //TODO: Refactor merging parseL(String) into parse(String).
    private static L parseL(String text) throws SecsParseException {
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

    private static Data<?> parseText(byte[] data, int offset) throws SecsParseException {
        if (data.length < 2) {
            throw new SecsParseException("Invalid data length: " + data.length);
        }
        
        int formatByte = data[offset];
        int formatCode = formatByte & 0xfc;
        int noOfLengthBytes = formatByte & 0x03;
        if (noOfLengthBytes < 1 || noOfLengthBytes > LENGTH_LENGTH) {
            throw new SecsParseException("Invalid number of length bytes: " + noOfLengthBytes);
        }
        if (data.length < noOfLengthBytes + 1) {
            throw new SecsParseException("Incomplete message data");
        }
        int length = data[offset + 1];
        if (noOfLengthBytes > 1) {
            length |= (data[offset + 2] << 8);
        }
        if (noOfLengthBytes > 2) {
            length |= (data[offset + 3] << 16);
        }
        if (noOfLengthBytes > 3) {
            length |= (data[offset + 4] << 24);
        }
        if (data.length < offset + 2 + length) {
            throw new SecsParseException("Incomplete message data");
        }
        
        Data<?> dataItem = null;
        offset += (1 + noOfLengthBytes);
        switch (formatCode) {
            case L.FORMAT_CODE:
                dataItem = parseL(data, offset, length);
                break;
            case BOOLEAN.FORMAT_CODE:
                dataItem = parseBoolean(data, offset, length);
                break;
            case B.FORMAT_CODE:
                dataItem = parseB(data, offset, length);
                break;
            case A.FORMAT_CODE:
                dataItem = parseA(data, offset, length);
                break;
            case I1.FORMAT_CODE:
                dataItem = parseI1(data, offset, length);
                break;
            case I2.FORMAT_CODE:
                dataItem = parseI2(data, offset, length);
                break;
            case I4.FORMAT_CODE:
                dataItem = parseI4(data, offset, length);
                break;
            case I8.FORMAT_CODE:
                dataItem = parseI8(data, offset, length);
                break;
            case U1.FORMAT_CODE:
                dataItem = parseU1(data, offset, length);
                break;
            case U2.FORMAT_CODE:
                dataItem = parseU2(data, offset, length);
                break;
            case U4.FORMAT_CODE:
                dataItem = parseU4(data, offset, length);
                break;
            case U8.FORMAT_CODE:
                dataItem = parseU8(data, offset, length);
                break;
            case F4.FORMAT_CODE:
                dataItem = parseF4(data, offset, length);
                break;
            case F8.FORMAT_CODE:
                dataItem = parseF8(data, offset, length);
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid format code in message data: %02x", formatCode));
        }
        return dataItem;
    }
    
    private static L parseL(byte[] data, int offset, int length) throws SecsParseException {
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

    private static BOOLEAN parseBoolean(byte[] data, int offset, int length) throws SecsParseException {
        if (length != BOOLEAN.LENGTH) {
            throw new SecsParseException("Invalid BOOLEAN length: " + length);
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

    private static I1 parseI1(byte[] data, int offset, int length) {
        int noOfValues = length / I1.SIZE;
        I1 i1 = new I1();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[I1.SIZE];
            System.arraycopy(data, offset + i * I1.SIZE, valueData, 0, I1.SIZE);
            i1.addValue(valueData);
        }
        return i1;
    }
    
    private static I2 parseI2(byte[] data, int offset, int length) {
        int noOfValues = length / I2.SIZE;
        I2 i2 = new I2();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[I2.SIZE];
            System.arraycopy(data, offset + i * I2.SIZE, valueData, 0, I2.SIZE);
            i2.addValue(valueData);
        }
        return i2;
    }
    
    private static I4 parseI4(byte[] data, int offset, int length) {
        int noOfValues = length / I4.SIZE;
        I4 i4 = new I4();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[I4.SIZE];
            System.arraycopy(data, offset + i * I4.SIZE, valueData, 0, I4.SIZE);
            i4.addValue(valueData);
        }
        return i4;
    }
    
    private static I8 parseI8(byte[] data, int offset, int length) {
        int noOfValues = length / I8.SIZE;
        I8 i8 = new I8();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[I8.SIZE];
            System.arraycopy(data, offset + i * I8.SIZE, valueData, 0, I8.SIZE);
            i8.addValue(valueData);
        }
        return i8;
    }
    
    private static U1 parseU1(byte[] data, int offset, int length) {
        U1 u1 = new U1();
        for (int i = 0; i < length; i++) {
            byte[] valueData = new byte[U1.SIZE];
            System.arraycopy(data, offset + i * U1.SIZE, valueData, 0, U1.SIZE);
            u1.addValue(valueData);
        }
        return u1;
    }
    
    private static U2 parseU2(byte[] data, int offset, int length) {
        int noOfValues = length / U2.SIZE;
        U2 u2 = new U2();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[U2.SIZE];
            System.arraycopy(data, offset + i * U2.SIZE, valueData, 0, U2.SIZE);
            u2.addValue(valueData);
        }
        return u2;
    }
    
    private static U4 parseU4(byte[] data, int offset, int length) {
        int noOfValues = length / U4.SIZE;
        U4 u4 = new U4();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[U4.SIZE];
            System.arraycopy(data, offset + i * U4.SIZE, valueData, 0, U4.SIZE);
            u4.addValue(valueData);
        }
        return u4;
    }
    
    private static U8 parseU8(byte[] data, int offset, int length) {
        int noOfValues = length / U8.SIZE;
        U8 u8 = new U8();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[U8.SIZE];
            System.arraycopy(data, offset + i * U8.SIZE, valueData, 0, U8.SIZE);
            u8.addValue(valueData);
        }
        return u8;
    }
    
    private static F4 parseF4(byte[] data, int offset, int length) {
        int noOfValues = length / F4.SIZE;
        F4 f4 = new F4();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[F4.SIZE];
            System.arraycopy(data, offset + i * F4.SIZE, valueData, 0, F4.SIZE);
            f4.addValue(valueData);
        }
        return f4;
    }
    
    private static F8 parseF8(byte[] data, int offset, int length) {
        int noOfValues = length / F8.SIZE;
        F8 f8 = new F8();
        for (int i = 0; i < noOfValues; i++) {
            byte[] valueData = new byte[F8.SIZE];
            System.arraycopy(data, offset + i * F8.SIZE, valueData, 0, F8.SIZE);
            f8.addValue(valueData);
        }
        return f8;
    }
    
}
