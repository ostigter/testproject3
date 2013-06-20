package org.ozsoft.secs.message;

import org.junit.Assert;
import org.junit.Test;
import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.SecsException;

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
        Assert.assertEquals(0xffff, (int) message.getSessionId().getValue());
        Assert.assertEquals(0x00, message.getHeaderByte2());
        Assert.assertEquals(0x00, message.getHeaderByte3());
        Assert.assertEquals(PType.SECS_II, message.getPType());
        Assert.assertEquals(SType.SELECT_RSP, message.getSType());
        Assert.assertEquals(0x11121314L, (long) message.getSystemBytes().getValue());
    }

}
