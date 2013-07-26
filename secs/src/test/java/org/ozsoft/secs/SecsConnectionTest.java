package org.ozsoft.secs;

import junit.framework.Assert;

import org.junit.Test;

public class SecsConnectionTest {
    
    private static final long CONNECTION_TIMEOUT = 1000L;

    @Test
    public void test() throws SecsException {
        // Create passive equipment listening on default port (server).
        SecsEquipment server = new SecsEquipment();
        server.setActive(false);
        Assert.assertFalse(server.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, server.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, server.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, server.getControlState());

        // Start server.
        server.setEnabled(true);
        sleep(500L);
        Assert.assertTrue(server.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, server.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_COMMUNICATING, server.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, server.getControlState());

        // Create active equipment connecting to default host and port (client).
        SecsEquipment client = new SecsEquipment();
        client.setActive(true);
        Assert.assertFalse(client.isEnabled());
        Assert.assertEquals(ConnectionState.NOT_CONNECTED, client.getConnectionState());
        Assert.assertEquals(CommunicationState.NOT_ENABLED, client.getCommunicationState());
        Assert.assertEquals(ControlState.EQUIPMENT_OFFLINE, client.getControlState());

        // Enable client, connecting to server.
        client.setEnabled(true);
        sleep(CONNECTION_TIMEOUT);
        Assert.assertTrue(server.isEnabled());
        Assert.assertTrue(client.isEnabled());
        Assert.assertEquals(ConnectionState.SELECTED, server.getConnectionState());
        Assert.assertEquals(ConnectionState.SELECTED, client.getConnectionState());
        Assert.assertEquals(CommunicationState.COMMUNICATING, server.getCommunicationState());
        Assert.assertEquals(CommunicationState.COMMUNICATING, client.getCommunicationState());
//        Assert.assertEquals(ControlState.ONLINE_REMOTE, server.getControlState());
//        Assert.assertEquals(ControlState.ONLINE_REMOTE, client.getControlState());

        // client.setEnabled(false);
        // sleep(1);
        //
        // server.setEnabled(false);
        // sleep(1);
    }

    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }

}
