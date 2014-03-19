package org.ozsoft.copytool.task;

public abstract class Task implements Runnable {

    private final TaskListener listener;

    private final Thread thread;

    private boolean isRunning = false;

    private int progress = -1;

    public Task(TaskListener listener) {
        this.listener = listener;
        thread = new Thread(this);
    }

    public final boolean isRunning() {
        return isRunning;
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

    public final void waitUntilCompleted() {
        if (isRunning) {
            while (isRunning()) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
            complete();
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

    protected final void setProgress(long current, long total) {
        if (current > total) {
            current = total;
        }
        setProgress((double) current, (double) total);
    }

    protected final void setProgress(double current, double total) {
        if (current > total) {
            current = total;
        }
        setProgress((int) Math.floor(current / total * 100.0));
    }

    protected final void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Invalid progress: " + progress);
        }
        if (listener != null && progress != this.progress) {
            listener.taskUpdated(progress);
            this.progress = progress;
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
