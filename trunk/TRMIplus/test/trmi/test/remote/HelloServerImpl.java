package trmi.test.remote;

import java.rmi.registry.LocateRegistry;

import trmi.Naming;
import trmi.RemoteSecurityManager;

/**
 * Example implementation of a remote server with transparant RMI.
 * 
 * @author Oscar Stigter
 */
public class HelloServerImpl implements HelloServer {
    
    private static final int REGISTRY_PORT = 1099;  // RMI default
    
    private static final String MESSAGE = "Hello World!";
    
    private final String name;
    
    private boolean alive = true;
    
    private boolean running = false;
    
    public static void main(String[] args) {
    	if (args.length != 1) {
    		System.err.println("No process name specified!");
    		System.exit(1);
    	}

    	new HelloServerImpl(args[0]);
    }
    
    public HelloServerImpl(String name) {
    	this.name = name;
    	
    	try {
        	LocateRegistry.getRegistry(REGISTRY_PORT);
        	
            RemoteSecurityManager.setup();
            
    		Naming.bind(name, this, new Class[] { HelloServer.class });
    		
        	System.out.println("Server '" + name + "': Created.");
        	
        	while (alive) {
            	try {
            		Thread.sleep(50);
            	} catch (InterruptedException e) {
            		// Ignore.
            	}
        	}

            Naming.unbind(name);
            
        	System.out.println("Server '" + name + "': Destroyed.");
    	} catch (Exception e) {
    		System.err.println("ERROR: " + e);
    	}
    }
    
    public String getName() {
    	return name;
    }
    
    public void start() {
    	if (!running) {
    		running = true;
	        System.out.println("Server: Started.");
	        
	        while (running) {
	        	try {
	        		Thread.sleep(50);
	        	} catch (InterruptedException e) {
	        		// Ignore.
	        	}
	        }
	        
	        System.out.println("Server: Stopped.");
    	}
    }
    
    public void stop() {
        if (running) {
        	System.out.println("Server '" + name + "': Stopping...");
        	running = false;
        } else {
            System.err.println(
                    "WARNING: stop() called but server not running");
        }
    }

    public String sayHello() {
        System.out.println("Server '" + name + "': sayHello() called.");
        return MESSAGE;
    }

}
