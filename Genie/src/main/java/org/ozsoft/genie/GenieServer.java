package org.ozsoft.genie;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * The server. 
 * 
 * Implemented as separate thread.
 * 
 * @author Oscar Stigter
 */
public class GenieServer implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(GenieServer.class);
	
	/** Socket timeout to avoid blocked waiting for connections. */
	private static final int SOCKET_TIMEOUT = 100;

	/** The server port. */
	private final int port;
	
	/** The service handlers mapped by the service name. */
	private final Map<String, Object> services;
	
	/** The server thread. */
	private Thread thread;
	
	/** The server socket. */
	private ServerSocket serverSocket;
	
	/** Whether the server is running. */
	private boolean isRunning = false; 
	
	/**
	 * Constructor using the default server port.
	 */
	public GenieServer() {
		this(GenieConstants.DEFAULT_PORT);
	}
	
	/**
	 * Constructor using a specific server port.
	 * 
	 * @param port
	 *            The server port.
	 */
	public GenieServer(int port) {
		this.port = port;
		services = new HashMap<String, Object>();
		LOG.debug("Created");
	}
	
	/**
	 * Binds a service.
	 * 
	 * @param name
	 *            The service name.
	 * @param handler
	 *            The service handler.
	 *            
	 * @throws GenieException
	 *             If a service with the specified name is already bound.
	 */
	public void bind(String name, Object handler) throws GenieException {
		if (services.containsKey(name)) {
			String msg = String.format("Service '%s' already bound", name);
			LOG.error(msg);
			throw new GenieException(msg);
		}
		services.put(name, handler);
		LOG.debug(String.format("Bound service '%s'", name));
	}
	
	/**
	 * Unbinds a service.
	 * 
	 * @param name
	 *            The service name.
	 *            
	 * @throws GenieException
	 *             If the service with the specified name is not bound.
	 */
	public void unbind(String name) throws GenieException {
		if (!services.containsKey(name)) {
			String msg = String.format("Service '%s 'not bound", name);
			LOG.error(msg);
			throw new GenieException(msg);
		}
		services.remove(name);
		LOG.debug(String.format("Unbound service '%s'", name));
	}
	
	/**
	 * Starts the server.
	 * 
	 * @throws GenieException
	 *             If the service is already running or could not be bound to
	 *             the port.
	 */
	public void start() throws GenieException {
		if (isRunning) {
			throw new GenieException("Server is already running");
		}
		
		LOG.debug("Starting");
		
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(SOCKET_TIMEOUT);
			thread = new Thread(this, "GenieServer");
			thread.start();
		} catch (IOException e) {
			String msg = String.format("Could not bind to port %d", port);
			throw new GenieException(msg, e);
		}
	}
	
	/**
	 * Stops the server.
	 */
	public void stop() {
		if (isRunning) {
			LOG.debug("Stopping");
			isRunning = false;
			thread.interrupt();
		}
	}
	
	/**
	 * The server thread's main loop handling service calls.
	 * Each service call is handled by a separate, multithreaded handler.
	 */
	public void run() {
		isRunning = true;
		LOG.debug(String.format("Started, listening on port %d", port));
		while (isRunning) {
			try {
				// Handle incoming service requests.
				new GenieRequestHandler(
						serverSocket.accept(), services).start();
			} catch (SocketTimeoutException e) {
				// Connection timeout expired; keep listening.
			} catch (IOException e) {
				System.err.format("Error handling request: %s", e.getMessage());
			}
		}
		LOG.debug("Stopped");
	}

}
