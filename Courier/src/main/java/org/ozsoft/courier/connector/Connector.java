package org.ozsoft.courier.connector;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.ozsoft.courier.Context;
import org.ozsoft.courier.CourierException;
import org.ozsoft.courier.NamespaceResolver;
import org.ozsoft.courier.action.Action;
import org.w3c.dom.Node;

/**
 * Base class of a handler.
 * 
 * @author Oscar Stigter
 */
public abstract class Connector implements Runnable {
    
    /** The log. */
    private static final Logger LOG = Logger.getLogger(Connector.class);
    
    /** The namespace resolver. */
    private final NamespaceResolver namespaceResolver;
    
    /** The actions. */
    private final List<Action> actions;
    
    /** The worker thread. */
    private Thread thread;
    
    /** Whether the working thread is running. */
    private boolean isRunning;
    
    /**
     * Constructor.
     */
    public Connector(NamespaceResolver namespaceResolver) {
    	this.namespaceResolver = namespaceResolver;
        actions = new LinkedList<Action>();
    }
    
    /**
     * Adds an action.
     * 
     * @param action
     *            The action.
     */
    public void addAction(Action action) {
        actions.add(action);
    }
    
    /**
     * Returns the actions.
     * 
     * @return The actions.
     */
    public List<Action> getActions() {
        return actions;
    }
    
    /**
     * Starts the worker thread.
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    /**
     * Stops the worker thread.
     */
    public void stop() {
        if (isRunning) {
            isRunning = false;
            if (thread != null) {
                thread.interrupt();
            }
        }
    }
    
    /**
     * Returns whether the working thread is running.
     * 
     * @return True if running, otherwise false.
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Handles a message.
     * 
     * @param message
     *            The message.
     */
    protected void handleMessage(Node message) {
        Context context = new Context(namespaceResolver);
        context.setMessage(message);
        try {
            for (Action action : getActions()) {
                action.execute(context);
            }
        } catch (CourierException e) {
            LOG.error("Error executing action", e);
        }
    }

}
