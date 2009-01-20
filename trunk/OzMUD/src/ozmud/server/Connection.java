package ozmud.server;


public interface Connection {
	
	
	void send(String message);
	
	boolean isDataAvailable();
	
	String receive();
	
	void addConnectionListener(ConnectionListener listener);

	void removeConnectionListener(ConnectionListener listener);

}
