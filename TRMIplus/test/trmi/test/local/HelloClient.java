package trmi.test.local;

import trmi.Naming;

/**
 * Example of a remote client using transparant RMI.
 * 
 * @author Oscar Stigter
 */
public class HelloClient {

    public void getGreeting() throws Exception {
        System.out.println("Client: Looking up server...");
        HelloServer server =
                (HelloServer) Naming.lookup(HelloServer.SERVER_ID);
        System.out.println("Client: Requesting message...");
        String message = server.sayHello();
        System.out.println("Client: Server said '" + message + "'.");
    }

}
