package org.ozsoft.task.test;

import org.ozsoft.task.TaskListener;
import org.ozsoft.task.example.ExampleTask;

/**
 * Test driver for the {@link Task} class.
 * 
 * @author Oscar Stigter
 */
public class Main implements TaskListener {

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        System.out.println("Main: Started");

        ExampleTask task = new ExampleTask(this);
        System.out.println("Main: Task created");

        System.out.println("Main: Starting task");
        task.start();

        System.out.println("Main: Waiting for the task to progress");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

        System.out.println("Main: Canceling task");
        task.cancel();

        System.out.println("Main: Finished");
    }

    @Override
    public void taskStarted() {
        System.out.println("Main: Task started");
    }

    @Override
    public void taskUpdated(int progress) {
        System.out.format("Main: Progress %d %%\n", progress);
    }

    @Override
    public void taskCompleted() {
        System.out.println("Main: Task completed");
    }

    @Override
    public void taskCanceled() {
        System.out.println("Main: Task canceled");
    }

}
