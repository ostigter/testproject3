package org.ozsoft.task.example;

import org.ozsoft.task.Task;
import org.ozsoft.task.TaskListener;

public class ExampleTask extends Task {

    public ExampleTask(TaskListener listener) {
        super(listener);
    }
    
    @Override
    protected void doStart() {
        System.out.println("DummyTask: Started");
    }
    
    @Override
    public void run() {
        System.out.println("DummyTask: Running");
        for (int i = 1; isRunning() && i <= 10; i++) {
            try {
                Thread.sleep(500L);
                setProgress(i * 10);
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
        complete();
    }
    
    @Override
    protected void doComplete() {
        System.out.println("DummyTask: Completed");
    }

    @Override
    protected void doCancel() {
        System.out.println("DummyTask: Canceled");
    }

}
