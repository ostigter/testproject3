package org.ozsoft.secs;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test suite to manually test a passive equipment (server). <br />
 * <br />
 * 
 * While the test is running, the tester should use another SECS/GEM equipment
 * to connect to the test equipment.
 * 
 * @author Oscar Stigter
 */
public class ServerTest {

    @Test
    @Ignore
    public void test() {
        SecsEquipment equipment = new SecsEquipment();
        try {
            equipment.setActive(false);
            equipment.setPort(5555);

            equipment.setEnabled(true);
            while (equipment.getCommunicationState() != CommunicationState.COMMUNICATING) {
                sleep(10L);
            }

            sleep(30000L);

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
