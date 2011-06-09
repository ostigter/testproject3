package org.ozsoft.rmi;

import org.junit.Test;

public class ServerTest {

    private static final String SERVICE_ID = "Greeter";
    
    private static final int PORT = 5000;
    
    @Test
    public void test() throws ServerException {
        Greeter greeter = new GreeterImpl();
        Server server = new Server(SERVICE_ID, greeter, PORT);
        server.start();
        server.stop();
    }
    
}
