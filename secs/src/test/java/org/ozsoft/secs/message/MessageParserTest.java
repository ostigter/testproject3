package org.ozsoft.secs.message;

import org.junit.Assert;
import org.junit.Test;
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

/**
 * Test suite for the <code>MessageParser<code>.
 * 
 * @author Oscar Stigter
 */
public class MessageParserTest {
    
    /**
     * Tests the parsing of empty data items.
     */
    @Test
    public void emptyData() {
        try {
            MessageParser.parseData(null);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Empty data item", e.getMessage());
        }
        
        try {
            MessageParser.parseData("");
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Empty data item", e.getMessage());
        }
    }

    /**
     * Tests the parsing of B data items.
     */
    @Test
    public void dataB() throws SecsException {
        Data<?> data = MessageParser.parseData("B {01 02 03}");
        Assert.assertTrue(data instanceof B);
        B b = (B) data;
        Assert.assertEquals(3, b.length());
        Assert.assertEquals(0x01, b.get(0));
        Assert.assertEquals(0x02, b.get(1));
        Assert.assertEquals(0x03, b.get(2));
    }

    /**
     * Tests the parsing of BOOLEAN data items.
     */
    @Test
    public void dataBOOLEAN() throws SecsException {
        Data<?> data = MessageParser.parseData("BOOLEAN {True}");
        Assert.assertTrue(data instanceof BOOLEAN);
        BOOLEAN b = (BOOLEAN) data;
        Assert.assertTrue(b.getValue());

        data = MessageParser.parseData("BOOLEAN {False}");
        Assert.assertTrue(data instanceof BOOLEAN);
        b = (BOOLEAN) data;
        Assert.assertFalse(b.getValue());
    }

    /**
     * Tests the parsing of A data items.
     */
    @Test
    public void dataA() throws SecsException {
        Data<?> data = MessageParser.parseData("A {Test}");
        Assert.assertTrue(data instanceof A);
        A a = (A) data;
        Assert.assertEquals(4, a.length());
        Assert.assertEquals("Test", a.getValue());
    }

    /**
     * Tests the parsing of U2 data items.
     */
    @Test
    public void dataU2() throws SecsException {
        Data<?> data = MessageParser.parseData("U2 {65535}");
        Assert.assertTrue(data instanceof U2);
        U2 u2 = (U2) data;
        Assert.assertEquals(0xffff, (int) u2.getValue());
    }

    /**
     * Tests the parsing of U4 data items.
     */
    @Test
    public void dataU4() throws SecsException {
        Data<?> data = MessageParser.parseData("U4 {4294967295}");
        Assert.assertTrue(data instanceof U4);
        U4 u4 = (U4) data;
        Assert.assertEquals(0xffffffffL, (long) u4.getValue());
    }

    /**
     * Tests the parsing of L data items.
     */
    @Test
    public void dataL() throws SecsException {
        Data<?> data = MessageParser.parseData("L {}");
        Assert.assertTrue(data instanceof L);
        L l = (L) data;
        Assert.assertEquals(0, l.length());

        data = MessageParser.parseData("L {A {V1} A {V2} A {V3}}");
        Assert.assertTrue(data instanceof L);
        l = (L) data;
        Assert.assertEquals(3, l.length());
        Assert.assertEquals("V1", l.getItem(0).getValue());
        Assert.assertEquals("V2", l.getItem(1).getValue());
        Assert.assertEquals("V3", l.getItem(2).getValue());

        data = MessageParser.parseData("L {L {A {V1} A {V2}} L {A {V3} A {V4}}}");
        Assert.assertTrue(data instanceof L);
        L l1 = (L) data;
        Assert.assertEquals(2, l1.length());
        Assert.assertEquals("V1", l1.getItem(0).getValue());
        Assert.assertEquals("V2", l1.getItem(1).getValue());
        L l2 = (L) data;
        Assert.assertEquals(2, l2.length());
        Assert.assertEquals("V3", l1.getItem(0).getValue());
        Assert.assertEquals("V4", l1.getItem(1).getValue());
    }

    /**
     * Tests the parsing of incomplete messages.
     */
    @Test
    public void incompleteMessage() {
        byte[] data = new byte[] {};
        try {
            MessageParser.parse(data, data.length);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (message length: 0)", e.getMessage());
        }

        data = new byte[] { 0x00, 0x00, 0x00, 0x0a };
        try {
            MessageParser.parse(data, data.length);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (message length: 4)", e.getMessage());
        }

        data = new byte[] { 0x00, 0x00, 0x00, 0x0a, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        try {
            MessageParser.parse(data, data.length);
            Assert.fail("Missed exception");
        } catch (SecsException e) {
            Assert.assertEquals("Incomplete message (declared length: 14; actual length: 13)", e.getMessage());
        }
    }

    /**
     * Tests the parsing of SELECT_REQ messages.
     */
    @Test
    public void selectReq() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0a, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x01, 0x11, 0x12, 0x13, 0x14 };
        Message message = MessageParser.parse(data, data.length);
        Assert.assertTrue(message instanceof ControlMessage);
        Assert.assertEquals(0xffff, (int) message.getSessionId().getValue());
        Assert.assertEquals(0x00, message.getHeaderByte2());
        Assert.assertEquals(0x00, message.getHeaderByte3());
        Assert.assertEquals(PType.SECS_II, message.getPType());
        Assert.assertEquals(SType.SELECT_REQ, message.getSType());
        Assert.assertEquals(0x11121314L, (long) message.getSystemBytes().getValue());
    }

    /**
     * Tests the parsing of DESELECT_REQ messages.
     */
    @Test
    public void deselectReq() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0a, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x02, 0x11, 0x12, 0x13, 0x14 };
        Message message = MessageParser.parse(data, data.length);
        Assert.assertTrue(message instanceof ControlMessage);
        Assert.assertEquals(0xffff, (int) message.getSessionId().getValue());
        Assert.assertEquals(0x00, message.getHeaderByte2());
        Assert.assertEquals(0x00, message.getHeaderByte3());
        Assert.assertEquals(PType.SECS_II, message.getPType());
        Assert.assertEquals(SType.SELECT_RSP, message.getSType());
        Assert.assertEquals(0x11121314L, (long) message.getSystemBytes().getValue());
    }

    /**
     * Tests the parsing of empty data messages (header only).
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageEmpty() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0a, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14};
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
        Assert.assertNull(text);
    }

    /**
     * Tests the parsing of data messages with B items. <br />
     * <br />
     * 
     * Message:
     * <pre>
     *   B {21 22 23}
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageB() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0f, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x21, 0x03, 0x21, 0x22,
                0x23 };
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

    /**
     * Tests the parsing of data messages with BOOLEAN items. <br />
     * <br />
     * 
     * Message:
     * <pre>
     *   BOOLEAN {True}
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageBOOLEAN() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0d, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x11, 0x01, 0x01 };
        DataMessage message = (DataMessage) MessageParser.parse(data, data.length);
        Data<?> text = message.getText();
        Assert.assertTrue(text instanceof BOOLEAN);
        BOOLEAN b = (BOOLEAN) text;
        Assert.assertEquals(1, b.length());
        Assert.assertTrue(b.getValue());
    }

    /**
     * Tests the parsing of data messages with A items. <br />
     * <br />
     * 
     * Message:
     * <pre>
     *   A {'Test'}
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageA() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x10, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x41, 0x04, 'T', 'e',
                's', 't' };
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

    /**
     * Tests the parsing of data messages with U2 items. <br />
     * <br />
     * 
     * Message:
     * <pre>
     *   U2 {258}
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageU2() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x0e, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, (byte) 0xa9, 0x02,
                0x01, 0x02 };
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

    /**
     * Tests single-level lists. <br />
     * <br />
     * 
     * Message:
     * 
     * <pre>
     * L {
     *   A {'V1'}
     *   A {'V2'}
     * }
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageLSimple() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x14, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x02, 0x41, 0x02,
                'V', '1', 0x41, 0x02, 'V', '2' };
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

    /**
     * Tests single-level lists. <br />
     * <br />
     * 
     * Message:
     * 
     * <pre>
     * L {
     *   A {'V1'}
     *   L {
     *     A {'V2'}
     *     A {'V3'}
     *   }
     * }
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageLMixed() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x1a, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x02, 0x41, 0x02,
                'V', '1', 0x01, 0x02, 0x41, 0x02, 'V', '2', 0x41, 0x02, 'V', '3' };
        DataMessage message = (DataMessage) MessageParser.parse(data, data.length);
        Data<?> text = message.getText();
        Assert.assertTrue(text instanceof L);
        L l1 = (L) text;
        Assert.assertEquals(2, l1.length());
        Assert.assertEquals("V1", l1.getItem(0).getValue());
        L l2 = (L) l1.getItem(1);
        Assert.assertEquals(2, l2.length());
        Assert.assertEquals("V2", l2.getItem(0).getValue());
        Assert.assertEquals("V3", l2.getItem(1).getValue());
    }

    /**
     * Tests nested lists. <br />
     * <br />
     * 
     * Message:
     * 
     * <pre>
     * L {
     *   L {
     *     B {11 12 13}
     *     B {21 22 23}
     *   }
     *   L {
     *     B {31 32 33}
     *     B {41 42 43}
     *   }
     * }
     * </pre>
     * 
     * @throws SecsException
     *             In case of an invalid message.
     */
    @Test
    public void dataMessageLNested() throws SecsException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x24, 0x00, 0x01, (byte) 0x81, 0x0d, 0x00, 0x00, 0x11, 0x12, 0x13, 0x14, 0x01, 0x02, 0x01, 0x02,
                0x21, 0x03, 0x11, 0x12, 0x13, 0x21, 0x03, 0x21, 0x22, 0x23, 0x01, 0x02, 0x21, 0x03, 0x31, 0x32, 0x33, 0x21, 0x03, 0x41, 0x42, 0x43 };
        DataMessage message = (DataMessage) MessageParser.parse(data, data.length);
        Data<?> text = message.getText();
        Assert.assertTrue(text instanceof L);
        L l1 = (L) text;
        Assert.assertEquals(2, l1.length());
        L l2 = (L) l1.getItem(0);
        Assert.assertEquals(2, l2.length());
        B b1 = (B) l2.getItem(0);
        Assert.assertEquals(0x11, b1.get(0));
        Assert.assertEquals(0x12, b1.get(1));
        Assert.assertEquals(0x13, b1.get(2));
        B b2 = (B) l2.getItem(1);
        Assert.assertEquals(0x21, b2.get(0));
        Assert.assertEquals(0x22, b2.get(1));
        Assert.assertEquals(0x23, b2.get(2));
        L l3 = (L) l1.getItem(1);
        Assert.assertEquals(2, l3.length());
        B b3 = (B) l3.getItem(0);
        Assert.assertEquals(0x31, b3.get(0));
        Assert.assertEquals(0x32, b3.get(1));
        Assert.assertEquals(0x33, b3.get(2));
        B b4 = (B) l3.getItem(1);
        Assert.assertEquals(0x41, b4.get(0));
        Assert.assertEquals(0x42, b4.get(1));
        Assert.assertEquals(0x43, b4.get(2));
    }

}
