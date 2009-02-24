package ozmud.ticker;


import java.util.HashSet;
import java.util.Set;


/**
 * Named ticker that sends 'ticks' on a specific interval to its listeners.
 *  
 * @author Oscar Stigter
 */
public class Ticker implements Runnable {
	

	/** Base tick interval in miliseconds. */
	private static final long TICK_DURATION = 1000L;
	
	private final String name;
	
	private final int interval;
	
	private final Set<TickListener> listeners;
	
	private Thread thread;
	
	private boolean isRunning = false;
	

	public Ticker(String name, int interval) {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("Null or empty name");
		}
		if (interval < 1) {
			throw new IllegalArgumentException("Interval < 1");
		}
		
		this.name = name;
		this.interval = interval;
		listeners = new HashSet<TickListener>();
	}
	

	public String getName() {
		return name;
	}
	

	public int getInterval() {
		return interval;
	}
	

	public void addTickListener(TickListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Null listener");
		}
		
		listeners.add(listener);
	}
	

	public void removeTickListener(TickListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Null listener");
		}
		
		listeners.remove(listener);
	}
	

	public void start() {
		thread = new Thread(this);
		thread.setDaemon(true);
		isRunning = true;
		thread.start();
	}
	

	public void stop() {
		isRunning = false;
		thread.interrupt();
		thread = null;
	}
	

	public void run() {
		while (isRunning) {
			try {
				Thread.sleep(interval * TICK_DURATION);
				fireListeners();
			} catch (InterruptedException e) {
				// Ignore.
			}
		}
	}
	
	
	private void fireListeners() {
		for (TickListener listener : listeners) {
			listener.tick(name);
		}
	}


}
