package org.ozsoft.secs;

import org.junit.Test;

public class SecsConnectionTest {
    
    private static final String HOST = "localhost";
    
    @Test
    public void test() {
        SecsServer server = new SecsServer();
        server.start();
        sleep(1);
        
        SecsClient client = new SecsClient(HOST);
        client.connect();
        sleep(1);
        
        server.stop();
        sleep(1);
    }
    
    private static void sleep(int duration) {
        try {
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }

}
