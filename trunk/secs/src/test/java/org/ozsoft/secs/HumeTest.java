package org.ozsoft.secs;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test suite to manually verify this SECS/GEM library can communicate with Hume's. <br />
 * <br />
 * 
 * The test code acts as active entity, connecting to Hume as the passive
 * entity.
 * 
 * @author Oscar Stigter
 */
public class HumeTest {

    /** Host of the equipment with the Hume SECS/GEM library. */
    private static final String HOST = "172.19.234.96";

    /** Port number of the Hume SECS/GEM library. */
    private static final int PORT = 5000;

    @Test
    @Ignore
    public void test() {
        SecsEquipment equipment = new SecsEquipment();
        try {
            equipment.setConnectMode(ConnectMode.ACTIVE);
            equipment.setHost(HOST);
            equipment.setPort(PORT);

            equipment.setEnabled(true);
            while (equipment.getCommunicationState() != CommunicationState.COMMUNICATING) {
                sleep(10L);
            }

            sleep(5000L);

            equipment.setEnabled(false);
            while (equipment.getCommunicationState() != CommunicationState.NOT_ENABLED) {
                sleep(10L);
            }

        } catch (SecsException e) {
            Assert.fail("ERROR: " + e.getMessage());
        }
    }

    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }

}
