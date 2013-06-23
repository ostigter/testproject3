package org.ozsoft.secs.message;

import org.junit.Assert;
import org.junit.Test;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;

public class MessageParserTest {
    
    @Test
    public void incompleteMessage() {
        byte[] data = new byte[] {};
        try {
            MessageParser.parse(data, data.length);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (message length: 0)", e.getMessage());
        }
        
        data = new byte[] {0x00, 0x00, 0x00, 0x0a};
        try {
            MessageParser.parse(data, data.length);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (message length: 4)", e.getMessage());
        }
        
        data = new byte[] {0x00, 0x00, 0x00, 0x0a, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        try {
            MessageParser.parse(data, data.length);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (declared length: 14; actual length: 13)", e.getMessage());
        }
    }

    @Test
    public void selectReq() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x0a, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x01, 0x11, 0x12, 0x13, 0x14};
        Message message = MessageParser.parse(data, data.length);
        Assert.assertTrue(message instanceof ControlMessage);
        Assert.assertEquals(0xffff, (int) message.getSessionId().getValue());
        Assert.assertEquals(0x00, message.getHeaderByte2());
        Assert.assertEquals(0x00, message.getHeaderByte3());
        Assert.assertEquals(PType.SECS_II, message.getPType());
        Assert.assertEquals(SType.SELECT_REQ, message.getSType());
        Assert.assertEquals(0x11121314L, (long) message.getSystemBytes().getValue());
    }

    @Test
    public void deselectReq() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x0a, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x02, 0x11, 0x12, 0x13, 0x14};
        Message message = MessageParser.parse(data, data.length);
        Assert.assertTrue(message instanceof ControlMessage);
        Assert.assertEquals(0xffff, (int) message.getSessionId().getValue());
        Assert.assertEquals(0x00, message.getHeaderByte2());
        Assert.assertEquals(0x00, message.getHeaderByte3());
        Assert.assertEquals(PType.SECS_II, message.getPType());
        Assert.assertEquals(SType.SELECT_RSP, message.getSType());
        Assert.assertEquals(0x11121314L, (long) message.getSystemBytes().getValue());
    }
    
    @Test
    public void dataMessage() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x0c, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x00};
        Message message = MessageParser.parse(data, data.length);
        Assert.assertEquals(0x0001, (int) message.getSessionId().getValue());
        Assert.assertEquals(-127, message.getHeaderByte2());
        Assert.assertEquals(0x0d, message.getHeaderByte3());
        Assert.assertEquals(PType.SECS_II, message.getPType());
        Assert.assertEquals(SType.DATA, message.getSType());
        Assert.assertEquals(0x11121314L, (long) message.getSystemBytes().getValue());
        Assert.assertTrue(message instanceof DataMessage);
        DataMessage dataMessage = (DataMessage) message;
        Assert.assertEquals(1, dataMessage.getStream());
        Assert.assertEquals(13, dataMessage.getFunction());
        Assert.assertEquals("S1F13", dataMessage.getType());
        Data<?> text = dataMessage.getText();
        Assert.assertTrue(text instanceof L);
        L l = (L) text;
        Assert.assertEquals(0, l.length());
    }

    @Test
    public void dataMessageB() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x0f, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x21, 0x03, 0x21, 0x22, 0x23};
        Message message = MessageParser.parse(data, data.length);
        Assert.assertEquals(0x0001, (int) message.getSessionId().getValue());
        Assert.assertEquals(-127, message.getHeaderByte2());
        Assert.assertEquals(0x0d, message.getHeaderByte3());
        Assert.assertEquals(PType.SECS_II, message.getPType());
        Assert.assertEquals(SType.DATA, message.getSType());
        Assert.assertEquals(0x11121314L, (long) message.getSystemBytes().getValue());
        Assert.assertTrue(message instanceof DataMessage);
        DataMessage dataMessage = (DataMessage) message;
        Assert.assertEquals(1, dataMessage.getStream());
        Assert.assertEquals(13, dataMessage.getFunction());
        Assert.assertEquals("S1F13", dataMessage.getType());
        Data<?> text = dataMessage.getText();
        Assert.assertTrue(text instanceof B);
        B b = (B) text;
        Assert.assertEquals(3, b.length());
        Assert.assertEquals(0x21, b.get(0));
        Assert.assertEquals(0x22, b.get(1));
        Assert.assertEquals(0x23, b.get(2));
    }

    @Test
    public void dataMessageBOOLEAN() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x0f, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x21, 0x03, 0x21, 0x22, 0x23};
        Message message = MessageParser.parse(data, data.length);
        Assert.assertEquals(0x0001, (int) message.getSessionId().getValue());
        Assert.assertEquals(-127, message.getHeaderByte2());
        Assert.assertEquals(0x0d, message.getHeaderByte3());
        Assert.assertEquals(PType.SECS_II, message.getPType());
        Assert.assertEquals(SType.DATA, message.getSType());
        Assert.assertEquals(0x11121314L, (long) message.getSystemBytes().getValue());
        Assert.assertTrue(message instanceof DataMessage);
        DataMessage dataMessage = (DataMessage) message;
        Assert.assertEquals(1, dataMessage.getStream());
        Assert.assertEquals(13, dataMessage.getFunction());
        Assert.assertEquals("S1F13", dataMessage.getType());
        Data<?> text = dataMessage.getText();
        Assert.assertTrue(text instanceof B);
        B b = (B) text;
        Assert.assertEquals(3, b.length());
        Assert.assertEquals(0x21, b.get(0));
        Assert.assertEquals(0x22, b.get(1));
        Assert.assertEquals(0x23, b.get(2));
    }

}
