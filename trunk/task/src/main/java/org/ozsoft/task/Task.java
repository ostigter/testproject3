package org.ozsoft.task;

public abstract class Task implements Runnable {
    
    private TaskListener listener;
    
    private Thread thread;
    
    protected boolean isRunning = false;
    
    public Task(TaskListener listener) {
        this.listener = listener;
        thread = new Thread(this);
    }
    
    public final void start() {
        if (!isRunning) {
            doStart();
            if (listener != null) {
                listener.taskStarted();
            }
            isRunning = true;
            thread.start();
        }
    }
    
    public final void cancel() {
        if (isRunning) {
            isRunning = false;
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            doCancel();
            if (listener != null) {
                listener.taskCanceled();
            }
        }
    }
    
    protected void doStart() {
        // To be overridden by subclass.
    }

    protected void doComplete() {
        // To be overridden by subclass.
    }

    protected void doCancel() {
        // To be overridden by subclass.
    }

    protected void setProgress(int progress) {
        if (listener != null) {
            listener.taskUpdated(progress);
        }
    }
    
    protected final void complete() {
        if (isRunning) {
            isRunning = false;
            doComplete();
            if (listener != null) {
                listener.taskCompleted();
            }
        }
    }
    
}
