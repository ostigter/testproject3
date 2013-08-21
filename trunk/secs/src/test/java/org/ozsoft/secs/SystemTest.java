package org.ozsoft.secs;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.secs.message.S99F1;
import org.ozsoft.secs.message.S99F2;

/**
 * Test suite verifying the communication between two local SECS equipment.
 * 
 * @author Oscar Stigter
 */
public class SystemTest {
    
    /** Timeout before communication is considered to have failed. */
    private static final long CONNECTION_TIMEOUT = 500L;

    /**
     * Tests the communication between two local SECS equipment.
     * 
     * @throws SecsException
     *             If the SECS communications fails.
     */
    @Test
    public void test() throws SecsException {
        // Create passive entity listening on default port.
        SecsEquipment passiveEntity = new SecsEquipment();
        passiveEntity.setConnectMode(ConnectMode.PASSIVE);
        Assert.assertFalse(passiveEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, passiveEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());
        
        // Register test primary message on passive entity.
        passiveEntity.addMessageType(S99F1.class);

        // Start passive entity.
        passiveEntity.setEnabled(true);
        sleep(CONNECTION_TIMEOUT);
        Assert.assertTrue(passiveEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_COMMUNICATING, passiveEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());

        // Create active entity connecting to default host and port.
        SecsEquipment activeEntity = new SecsEquipment();
        activeEntity.setConnectMode(ConnectMode.ACTIVE);
        Assert.assertFalse(activeEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, activeEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, activeEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, activeEntity.getControlState());

        // Register test reply message on active entity.
        activeEntity.addMessageType(S99F2.class);

        // Enable active entity, connecting to passive entity.
        activeEntity.setEnabled(true);
        sleep(CONNECTION_TIMEOUT);
        Assert.assertTrue(passiveEntity.isEnabled());
        Assert.assertTrue(activeEntity.isEnabled());
        Assert.assertEquals(ConnectionState.SELECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(ConnectionState.SELECTED, activeEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.COMMUNICATING, passiveEntity.getCommunicationState());
        Assert.assertEquals(CommunicationState.COMMUNICATING, activeEntity.getCommunicationState());
//        Assert.assertEquals(ControlState.ONLINE_REMOTE, server.getControlState());
//        Assert.assertEquals(ControlState.ONLINE_REMOTE, client.getControlState());
        
        // Send S99F1 message from active to passive entity.
        S99F1 s99f1 = new S99F1();
        s99f1.setName("Mr. Smith");
        SecsReplyMessage replyMessage = activeEntity.sendMessageAndWait(s99f1);
        Assert.assertEquals("Incorrect stream", 99, replyMessage.getStream());
        Assert.assertEquals("Incorrect function", 2, replyMessage.getFunction());
        Assert.assertTrue("Reply message not S99F2", replyMessage instanceof S99F2);
        S99F2 s99f2 = (S99F2) replyMessage;
        Assert.assertEquals("Incorrect GRACK value", S99F2.GRACK_ACCEPT, s99f2.getGrAck());
        Assert.assertEquals("Incorrect GREETING value", "Hello, Mr. Smith!", s99f2.getGreeting());

        // Disable active entity. 
        activeEntity.setEnabled(false);
         sleep(CONNECTION_TIMEOUT);
         Assert.assertFalse(activeEntity.isEnabled());
         Assert.assertEquals(ConnectionState.NOT_CONNECTED, activeEntity.getConnectionState());
         Assert.assertEquals(CommunicationState.NOT_ENABLED, activeEntity.getCommunicationState());
         Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, activeEntity.getControlState());
         Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
         Assert.assertEquals(CommunicationState.NOT_COMMUNICATING, passiveEntity.getCommunicationState());
         Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());
        
         // Disable passive entity. 
         passiveEntity.setEnabled(false);
         Assert.assertFalse(passiveEntity.isEnabled());
         Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
         Assert.assertEquals(ConnectionState.NOT_CONNECTED, activeEntity.getConnectionState());
         Assert.assertEquals(CommunicationState.NOT_ENABLED, passiveEntity.getCommunicationState());
         Assert.assertEquals(CommunicationState.NOT_ENABLED, activeEntity.getCommunicationState());
         Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());
         Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, activeEntity.getControlState());
    }
    
    /**
     * Suspends the current thread for a specific duration.
     * 
     * @param duration
     *            The duration in miliseconds.
     */
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }

}
