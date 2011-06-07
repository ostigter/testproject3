package org.example.rmi;

import org.ozsoft.rmi.Server;
import org.ozsoft.rmi.ServerException;

public class ServerTest {
    
    private static final String SERVICE_ID = "Greeter";
    
    private static final int PORT = 5000;
    
    public static void main(String[] args) {
        Greeter greeter = new GreeterImpl();
        Server server = new Server(SERVICE_ID, greeter, PORT);
        try {
            server.start();
            server.stop();
        } catch (ServerException e) {
            System.err.println(e);
        }
    }

}
