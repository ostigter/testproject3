package trmi.test.remote;

/**
 * Example interface of a remote server using transparant RMI.
 * 
 * @author Oscar Stigter
 */
public interface HelloServer {

    String getName();

    void start();

    void stop();

    String sayHello();

}
