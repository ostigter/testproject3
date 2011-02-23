package org.ozsoft.timer;

public class Timer implements Runnable {
    
    private final TimerListener listener;
    
    private final long interval;
    
    private boolean isRunning = false;
    
    private Thread thread;
    
    public Timer(TimerListener listener, long interval) {
        this.listener = listener;
        this.interval = interval;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public void start() {
        if (isRunning) {
            throw new IllegalStateException("Timer already started");
        }
        
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                // Ignore.
            }
            thread = null;
        }
    }
    
    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // Ignore.
            }
            
            if (isRunning) {
                listener.timerElapsed();
            }
        }
    }
    
}
