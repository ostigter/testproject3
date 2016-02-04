package org.ozsoft.task;

import java.util.HashSet;
import java.util.Set;

/**
 * Asynchronous, cancelable task with progress reporting.
 * 
 * @author Oscar Stigter
 */
public abstract class Task implements Runnable {

    private final Set<TaskListener> listeners = new HashSet<TaskListener>();

    private final Thread thread;

    private boolean isRunning = false;

    private boolean isFinished = false;

    private boolean isCanceled = false;

    public Task() {
        thread = new Thread(this);
    }

    public Task(TaskListener listener) {
        this();
        addListener(listener);
    }

    public final void addListener(TaskListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(TaskListener listener) {
        listeners.remove(listener);
    }

    public final boolean isRunning() {
        return isRunning;
    }

    public final boolean isFinished() {
        return isFinished;
    }

    public final boolean isCanceled() {
        return isCanceled;
    }

    public final void start() {
        if (!isRunning) {
            doStart();
            for (TaskListener listener : listeners) {
                listener.taskStarted();
            }
            isRunning = true;
            thread.start();
        }
    }

    public final void cancel() {
        if (isRunning) {
            isRunning = false;
            isCanceled = true;
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            isFinished = true;
            doCancel();
            for (TaskListener listener : listeners) {
                listener.taskCanceled();
            }
        }
    }

    protected final void setProgress(int progress) {
        for (TaskListener listener : listeners) {
            listener.taskUpdated(progress);
        }
    }

    protected final void complete() {
        if (isRunning) {
            isRunning = false;
            isFinished = true;
            doComplete();
            for (TaskListener listener : listeners) {
                listener.taskCompleted();
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
}
