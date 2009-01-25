package ozmud.server;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Base class for all connection implementations.
 * 
 * @author Oscar Stigter
 */
public abstract class AbstractConnection implements Connection {
	
	
	/** Indicates whether this connection is open. */
	protected boolean isOpen = false;
	
	/** Connection listeners. */
	private final Set<ConnectionListener> listeners;
	
	private ReceiverThread receiver;
	
	
	/**
	 * Constructor.
	 */
	public AbstractConnection() {
		listeners = new HashSet<ConnectionListener>();
	}
	
	
	/**
	 * Opens the connection.
	 */
	public void open() {
		isOpen = true;
	}
	
	
	/**
	 * Returns true if the connection is open.
	 * 
	 * @return True if the connection is open
	 */
	public boolean isOpen() {
		return isOpen;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.server.Connection#addListener(ozmud.server.ConnectionListener)
	 */
	public void addListener(ConnectionListener listener) {
		listeners.add(listener);
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.server.Connection#removeListener(ozmud.server.ConnectionListener)
	 */
	public void removeListener(ConnectionListener listener) {
		listeners.remove(listener);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.server.Connection#isReceiving()
	 */
	public boolean isReceiving() {
		return (receiver != null);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.server.Connection#setReceiving(boolean)
	 */
	public void setReceiving(boolean isReceiving) {
		if (isReceiving) {
			if (receiver == null) {
				receiver = new ReceiverThread(this);
				receiver.start();
			}
		} else {
			if (receiver != null) {
				receiver.shutdown();
				receiver = null;
			}
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.server.Connection#close()
	 */
	public void close() {
		setReceiving(false);
		isOpen = false;
	}
	
	
	/**
	 * Sends an incoming message to all listeners.
	 * 
	 * @param message  The message
	 */
	private void fireListeners(String message) {
		for (ConnectionListener listener : listeners) {
			listener.messageReceived(message);
		}
	}
	
	
	//------------------------------------------------------------------------
	//  Inner class
	//------------------------------------------------------------------------
	

	/**
	 * Thread listening to incoming messages to be sent to the listeners.
	 */
	private static class ReceiverThread extends Thread {
		
		
		private final AbstractConnection connection;
		
		private boolean isRunning = true;
		
		
		/**
		 * Constructor.
		 * 
		 * @param connection  The connection
		 */
		public ReceiverThread(AbstractConnection connection) {
			this.connection = connection;
		}
		

		/**
		 * The thread's main loop.
		 */
		public void run() {
//			System.out.println("ReceiverThread: Started");
			while (isRunning) {
				try {
					String message = connection.receive(false);
					if (message.length() != 0) {
						connection.fireListeners(message);
					}
				} catch (IOException e) {
					System.err.println("*** IO error while polling for incoming data: " + e.getMessage());
					isRunning = false;
				}
			}
//			System.out.println("ReceiverThread: Shut down.");
		}
		
		
		/**
		 * Shuts down the thread.
		 */
		public void shutdown() {
//			System.out.println("ReceiverThread: Shutting down...");
			isRunning = false;
			interrupt();
		}
		
		
	} // ReceiverThread


} // AbstractConnection
