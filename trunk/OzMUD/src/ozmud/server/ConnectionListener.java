package ozmud.server;


/**
 * Connection listener for incoming messages.
 * 
 * @author Oscar Stigter
 */
public interface ConnectionListener {
	
	
	/**
	 * Handles an incoming message.
	 *  
	 * @param message  The message
	 */
	void messageReceived(String message);


}
