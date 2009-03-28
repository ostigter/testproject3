package org.ozsoft.genie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * A client to call operations on a specific Genie service.
 * 
 * @author Oscar Stigter
 */
public class GenieClient {
	
	private static final Logger LOG = Logger.getLogger(GenieClient.class);
	
	private final String service;
	private final String host;
	private final int port;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	/**
	 * Creates and connects a Genie client for the specified service running on
	 * the specified host and the default port.
	 * 
	 * @param host
	 *            The server's hostname.
	 * @param service
	 *            The service name.
	 * 
	 * @throws GenieException
	 *             In case the host could not be reached, or no Genie server is
	 *             running on the specified host and/or port.
	 */
	public GenieClient(String host, String service) throws GenieException {
		this(host, GenieConstants.DEFAULT_PORT, service);
	}
	
	/**
	 * Creates and connects a Genie client for the specified service running on
	 * the specified host and port.
	 * 
	 * @param host
	 *            The server's hostname.
	 * @param port
	 *            The server's port.
	 * @param service
	 *            The service name.
	 * 
	 * @throws GenieException
	 *             In case the host could not be reached, or no Genie server is
	 *             running on the specified host and/or port.
	 */
	public GenieClient(String host, int port, String service)
			throws GenieException {
		this.host = host;
		this.port = port;
		this.service = service;
		LOG.debug("Created");
	}
	
	/**
	 * Calls an operation of the service this client is connected to.
	 * 
	 * @param operation
	 *            The operation name.
	 * @param args
	 *            Any operation arguments.
	 * 
	 * @return The operation's return value.
	 * 
	 * @throws GenieException
	 *             If the service or operation is not found, the number of
	 *             arguments is invalid, the arguments are of the wrong type,
	 *             or an I/O error occurred.
	 */
	public Object call(String operation, Object... args) throws GenieException {
		try {
			// Connect to the service.
			LOG.debug(String.format(
					"Connecting to service '%s' on host '%s', port %d",
					service, host, port));
			socket = new Socket(host, port);
			LOG.debug("Connected");
		} catch (IOException e) {
			String msg = String.format(
					"Could not connect to server on host '%s', port %d",
					host, port);
			LOG.error(msg, e);
			throw new GenieException(msg, e);
		}
		
		LOG.debug(String.format("Calling operation '%s'", operation));
		
		long time = System.currentTimeMillis();
		
		// Send request.
		GenieRequest request = new GenieRequest();
		request.setService(service);
		request.setOperation(operation);
		request.setArgs(args);
		try {
			LOG.debug("Sending request");
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(request);
			oos.flush();
		} catch (IOException e) {
			String msg = String.format(
					"Error sending request: %s", e.getMessage());
			LOG.error(msg, e);
			throw new GenieException(msg, e);
		}
		
		// Receive response.
		GenieResponse response = null;
		try {
			LOG.debug("Receiving response");
			ois = new ObjectInputStream(socket.getInputStream());
			Object obj = ois.readObject();
			if (obj == null) {
				throw new GenieException(
						"Error receiving response: Object is null");
			}
			if (!(obj instanceof GenieResponse)) {
				String msg = "Error receiving response: Object not a GenieResponse";
				LOG.error(msg);
				throw new GenieException(msg);
			}
			response = (GenieResponse) obj;
		} catch (Exception e) {
			String msg = String.format(
					"Error receiving response: %s", e.getMessage());
			LOG.error(msg, e);
			throw new GenieException(msg, e);
		}
		
		String errorMessage = response.getErrorMessage();
		if (errorMessage != null) {
			throw new GenieException(errorMessage);
		}
		
		try {
			ois.close();
		} catch (IOException e) {
			// Best effort, ignore.
		}
		try {
			oos.close();
		} catch (IOException e) {
			// Best effort, ignore.
		}
		try {
			socket.close();
		} catch (IOException e) {
			// Best effort, ignore.
		}
		
		LOG.debug("Disconnected");
		
		long duration = System.currentTimeMillis() - time;
		LOG.debug(String.format("Service call finished in %d ms", duration));
		
		return response.getReturnValue();
	}

}
