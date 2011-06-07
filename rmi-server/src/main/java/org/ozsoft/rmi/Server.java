package org.ozsoft.rmi;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Generic RMI server.
 */
public class Server {
    
    private final String serviceId;
    
    private final Remote service;
    
    private final int port;
    
    private ServerState state = ServerState.STOPPED;
    
    /** The RMI registry. */
    private Registry registry;
    
    public Server(String serviceId, Remote service, int port) {
        this.serviceId = serviceId;
        this.service = service;
        this.port = port;
    }
    
    public final ServerState getState() {
        return state;
    }
    
    public final void start() throws ServerException {
        if (state != ServerState.STOPPED) {
            throw new ServerException("Server already running");
        }
        
        state = ServerState.STARTING;
        
        try {
            registry = LocateRegistry.createRegistry(port);
            UnicastRemoteObject.exportObject(service, 0);
            registry.bind(serviceId, service);
            state = ServerState.STARTED;
        } catch (Exception e) {
            throw new ServerException("Could not start server: " + e.getMessage(), e);
        }
    }
    
    public final void stop() throws ServerException {
        if (state != ServerState.STARTED) {
            throw new ServerException("Server not running");
        }
        
        state = ServerState.STOPPING;
        
        if (registry != null) {
            try {
                registry.unbind(serviceId);
                UnicastRemoteObject.unexportObject(service, true);
            } catch (Exception e) {
                throw new ServerException("Could not stop server: " + e.getMessage(), e);
            } finally {
                state = ServerState.STOPPED;
                registry = null;
            }
        }
    }
    
}
