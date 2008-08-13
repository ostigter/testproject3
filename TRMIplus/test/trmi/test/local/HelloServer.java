package trmi.test.local;

/**
 * Example interface of a remote server using transparant RMI.
 * 
 * @author Oscar Stigter
 */
public interface HelloServer {

    String SERVER_ID = "HelloServer";

    void start();
    
    void stop();
    
    String sayHello();

}
