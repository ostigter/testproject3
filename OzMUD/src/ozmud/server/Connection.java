package ozmud.server;


public interface Connection {
	

	boolean isOpen();
	
	
	void send(String message);
	
	
	boolean dataAvailable();
	
	
	String receive();
	
	
	void addListener(ConnectionListener listener);

	
	void removeListener(ConnectionListener listener);

	
	void close();
	

}
