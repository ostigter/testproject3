package org.ozsoft.secs;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Automated Test suite verifying the communication between two equipment.
 * 
 * @author Oscar Stigter
 */
public class SystemTest {
    
    private static final long CONNECTION_TIMEOUT = 1000L;

    @Test
    public void test() throws SecsException {
        // Create passive entity listening on default port.
        SecsEquipment passiveEntity = new SecsEquipment();
        passiveEntity.setActive(false);
        Assert.assertFalse(passiveEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, passiveEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());

        // Start passive entity.
        passiveEntity.setEnabled(true);
        sleep(CONNECTION_TIMEOUT);
        Assert.assertTrue(passiveEntity.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, passiveEntity.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_COMMUNICATING, passiveEntity.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, passiveEntity.getControlState());

        // Create active entity connecting to default host and port.
        SecsEquipment activeEntity = new SecsEquipment();
        activeEntity.setActive(true);
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

    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }

}
