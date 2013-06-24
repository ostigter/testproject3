package org.ozsoft.secs.message;

import org.junit.Assert;
import org.junit.Test;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.format.U2;

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

    @Test
    public void dataMessageA() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x10, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x41, 0x04, 'T', 'e', 's', 't'};
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
        Assert.assertTrue(text instanceof A);
        A a = (A) text;
        Assert.assertEquals(4, a.length());
        Assert.assertEquals("Test", a.getValue());
    }

    @Test
    public void dataMessageU2() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x0e, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, (byte) 0xa9, 0x02, 0x01, 0x02};
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
        Assert.assertTrue(text instanceof U2);
        U2 u2 = (U2) text;
        Assert.assertEquals(2, u2.length());
        Assert.assertEquals(0x0102, (int) u2.getValue());
    }

    @Test
    public void dataMessageLSimple() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x14, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x02, 0x41, 0x02, 'V', '1', 0x41, 0x02, 'V', '2'};
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
        Assert.assertEquals(2, l.length());
        Assert.assertEquals("V1", l.getItem(0).getValue());
        Assert.assertEquals("V2", l.getItem(1).getValue());
    }

    @Test
    public void dataMessageLMixed() throws SecsException {
        byte[] data = new byte[] {0x00, 0x00, 0x00, 0x1a, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x02, 0x41, 0x02, 'V', '1', 0x01, 0x02, 0x41, 0x02, 'V', '2', 0x41, 0x02, 'V', '3'};
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
        L l1 = (L) text;
        Assert.assertEquals(2, l1.length());
        Assert.assertEquals("V1", l1.getItem(0).getValue());
        L l2 = (L) l1.getItem(1);
        Assert.assertEquals(2, l2.length());
        Assert.assertEquals("V2", l2.getItem(0).getValue());
        Assert.assertEquals("V3", l2.getItem(1).getValue());
    }

    @Test
    public void dataMessageLNested() throws SecsException {
        //TODO
    }

}
