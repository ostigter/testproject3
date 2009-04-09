package org.ozsoft.courier;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/**
 * Base class of all handlers.
 * 
 * @author Oscar Stigter
 */
public abstract class Handler implements Runnable {
    
    /** The log. */
    private static final Logger LOG = Logger.getLogger(Handler.class);
    
    /** The actions. */
    private final List<Action> actions;
    
    /** The worker thread. */
    private Thread thread;
    
    /** Whether the working thread is running. */
    private boolean isRunning;
    
    /**
     * Constructor.
     */
    public Handler() {
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
        Context context = new Context();
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
