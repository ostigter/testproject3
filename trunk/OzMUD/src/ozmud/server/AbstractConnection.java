package ozmud.server;


import java.util.HashSet;
import java.util.Set;


public abstract class AbstractConnection implements Connection {
	
	
	/** Indicates whether this connection is open. */
	protected boolean isOpen = false;
	
	/** Connection listeners. */
	private final Set<ConnectionListener> listeners;
	
	private final ReceiverThread receiver;
	
	
	/**
	 * Constructor.
	 */
	public AbstractConnection() {
		listeners = new HashSet<ConnectionListener>();
		receiver = new ReceiverThread(this);
	}
	
	
	public void open() {
		receiver.start();
		isOpen = true;
	}
	
	
	public void close() {
		receiver.interrupt();
		try {
			receiver.join();
		} catch (InterruptedException e) {
			// Safe to ignore.
		}
		isOpen = false;
	}
	
	
	public final boolean isOpen() {
		return isOpen;
	}
	
	
	/**
	 * Adds a connection listener.
	 * 
	 * @param  listener  the listener
	 */
	public final void addListener(ConnectionListener listener) {
		listeners.add(listener);
	}


	/**
	 * Removes a connection listener.
	 * 
	 * @param  listener  the listener
	 */
	public final void removeListener(ConnectionListener listener) {
		listeners.remove(listener);
	}
	
	
	/**
	 * Sends an incoming message to all connection listeners.
	 * 
	 * @param  message  the message
	 */
	private void fireListeners(String message) {
		for (ConnectionListener listener : listeners) {
			listener.messageReceived(message);
		}
	}
	
	
	//------------------------------------------------------------------------
	//  Inner class
	//------------------------------------------------------------------------
	

	private static class ReceiverThread extends Thread {
		
		
		private static final int DELAY = 10;
		
		private final AbstractConnection connection;
		
		private boolean isRunning = true;
		
		
		public ReceiverThread(AbstractConnection connection) {
			this.connection = connection;
		}
		

		@Override
		public void run() {
			System.out.println("Receiver: Started");
			while (isRunning) {
				try {
					if (connection.dataAvailable()) {
						String message = connection.receive();
						connection.fireListeners(message);
					}
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					System.out.println("Receiver: Interrupted");
					isRunning = false;
				}
			}
			System.out.println("Receiver: Finished");
		}

		
	} // Receiver


} // AbstractConnection
