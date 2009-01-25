package ozmud.server;


import java.io.IOException;


public interface Connection {
	

	/**
	 * Sets whether ANSI color support is enabled.
	 * 
	 * @param colorsEnabled  If true enabled, otherwise disabled
	 */
	void setColorsEnabled(boolean colorsEnabled);


	/**
	 * Returns true if the connection is open, otherwise false.
	 * 
	 * @return True if the connection is open, otherwise false
	 */
	boolean isOpen();
	
	
	/**
	 * Adds a listener for incoming messages.
	 * 
	 * @param listener  the listener
	 */
	void addListener(ConnectionListener listener);

	
	/**
	 * Removes a listener.
	 * 
	 * @param listener  the listener
	 */
	void removeListener(ConnectionListener listener);
	
	
	/**
	 * Returns true if the connection is listening for incoming messages.
	 * 
	 * @return True if the connection is listening for incoming messages
	 */
	boolean isReceiving();
	
	
	/**
	 * Set whether to send incoming messages to the listeners.
	 * 
	 * @param isReceiving  If true, incoming messages are send to the listeners
	 */
	void setReceiving(boolean isReceiving);
	
	
	/**
	 * Sends a message.
	 * The messages may include ANSI color codes. 
	 * 
	 * @param message  The message
	 * 
	 * @throws  IOException  In case of an I/O error
	 */
	void send(String message) throws IOException;
	
	
	/**
	 * Returns true if incoming data is available, otherwise false.
	 * 
	 * @return True if incoming data is available, otherwise false
	 * 
	 * @throws  IOException  In case of an I/O error
	 */
	boolean dataAvailable() throws IOException;
	
	
	/**
	 * Receives an incoming message.
	 * 
	 * @param asCommand
	 *            If true, the message will be handled like a command, i.e.
	 *            the call blocks until a message is received and any LF or CR
	 *            characters are filtered out.
	 * 
	 * @return the message
	 * 
	 * @throws  IOException  In case of an I/O error
	 */
	String receive(boolean asCommand) throws IOException ;
	
	
	/**
	 * Closes the connection.
	 */
	void close();
	

}
