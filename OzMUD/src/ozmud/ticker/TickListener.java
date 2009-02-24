package ozmud.ticker;


/**
 * Listener for 'tick' events.
 * 
 * @author Oscar Stigter
 */
public interface TickListener {
	
	/**
	 * Handles a tick from a ticker.
	 * 
	 * @param name The ticker's name.
	 */
	void tick(String name);

}
