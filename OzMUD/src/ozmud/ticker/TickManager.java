package ozmud.ticker;


import java.util.HashMap;
import java.util.Map;


/**
 * Manages a collection of named tickers.
 * 
 * @author Oscar Stigter
 */
public class TickManager {
	

	private final Map<String, Ticker> tickers;
	
	
	public TickManager() {
		tickers = new HashMap<String, Ticker>();
	}
	
	
	public synchronized void addTicker(String name, int interval) {
		if (!tickers.containsKey(name)) {
			Ticker ticker = new Ticker(name, interval);
			tickers.put(name, ticker);
			ticker.start();
		}
	}
	
	
	public synchronized void addTickListener(TickListener listener, String tickerName) {
		Ticker ticker = tickers.get(tickerName);
		if (ticker != null) {
			ticker.addTickListener(listener);
		} else {
			throw new IllegalStateException("Ticker not found: " + tickerName);
		}
	}
	
	
	public synchronized void removeTickListener(TickListener listener, String tickerName) {
		Ticker ticker = tickers.get(tickerName);
		if (ticker != null) {
			ticker.removeTickListener(listener);
		} else {
			throw new IllegalStateException("Ticker not found: " + tickerName);
		}
	}
	

	public synchronized void clear() {
		for (Ticker ticker : tickers.values()) {
			ticker.stop();
		}
		tickers.clear();
	}


}
