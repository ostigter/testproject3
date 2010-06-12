package org.ozsoft.genie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * A server thread handling the execution of a single service call. <br />
 * <br />
 * 
 * After the service call has been handled, the thread is finished.
 * 
 * @author Oscar Stigter
 */
public class GenieRequestHandler extends Thread {
	
	private static final Logger LOG = Logger.getLogger(GenieRequestHandler.class);
	
	private final Socket socket;
	
	private final Map<String, Object> services;
	
	public GenieRequestHandler(Socket socket, Map<String, Object> services) {
		this.socket = socket;
		this.services = services;
		LOG.debug("Created");
	}
	
	@Override
	public void run() {
		GenieResponse response = new GenieResponse();
		
		// Receive request.
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			LOG.debug("Receiving request");
			Object obj = ois.readObject();
			if (obj == null) {
				response.setErrorMessage("Received null object");
			} else if (!(obj instanceof GenieRequest)) {
				response.setErrorMessage("Received object not a GenieRequest");
			} else {
				GenieRequest request = (GenieRequest) obj;
				// Look up the service (handler).
				String service = request.getService();
				Object handler = services.get(service);
				if (handler == null) {
					response.setErrorMessage("Service not found: " + service);
				} else {
					// Look up the operation (method).
					String operation = request.getOperation();
					Method method = getOperationMethod(handler, operation);
					if (method == null) {
						response.setErrorMessage("Operation not found: " + operation);
					} else {
						// Get the arguments.
						Object[] args = request.getArgs();
						if (method.getParameterTypes().length != args.length) {
							response.setErrorMessage("Invalid number of operation arguments");
						} else {
							// Execute the operation (calling the method).
							try {
								Object returnValue = method.invoke(handler, args);
								response.setReturnValue(returnValue);
							} catch (Exception e) {
								response.setErrorMessage("Error invoking operation: " + e);
							}
						}
					}
				}
			}

			// Send the response.
			LOG.debug("Sending response");
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(response);
			oos.flush();
		} catch (Exception e) {
			LOG.error("Error handling request", e);
		}

		LOG.debug("Finished");
	}

	private static Method getOperationMethod(Object serviceHandler, String operation) {
		for (Method method : serviceHandler.getClass().getMethods()) {
			if (method.getName().equals(operation)) {
				// Return first matching method (no overloading support!).
				return method;
			}
		}
		
		// Not found.
		return null;
	}

}
