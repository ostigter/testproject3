package org.ozsoft.secs;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.message.S99F1;

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
        
        // Register test message handler on passive entity.
        passiveEntity.addMessageHandler(new S99F1());

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
        L l = new L();
        l.addItem(new A("Mr. Smith"));
        DataMessage replyMessage = activeEntity.sendDataMessage(99, 1, true, l);
        Assert.assertEquals(99, replyMessage.getStream());
        Assert.assertEquals(2, replyMessage.getFunction());
        l = (L) replyMessage.getText();
        Assert.assertEquals(2, l.length());
        int ackCode = ((B) l.getItem(0)).get(0);
        Assert.assertEquals(0x00, ackCode);
        String greeting = ((A) l.getItem(1)).getValue();
        Assert.assertEquals("Hello, Mr. Smith!", greeting);

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
