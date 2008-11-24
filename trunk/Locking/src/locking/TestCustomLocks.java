package locking;

/**
 * Test driver for the custom re-entrant read/write lock implementation.
 * 
 * @author Oscar Stigter
 */
public class TestCustomLocks {

    private static final int THREAD_COUNT = 100;
    private static final int ITERATION_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        System.out.println("Running...");

        long time = System.currentTimeMillis();

        CustomLock lock = new CustomLock();

        Runner[] threads = new Runner[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Runner(lock);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        long duration = System.currentTimeMillis() - time;
        System.out.printf("Finished in %d ms.", duration);
    }

    static class Runner extends Thread {

        private final CustomLock lock;

        public Runner(CustomLock lock) {
            this.lock = lock;
        }

        public void run() {
            for (int i = 0; i < ITERATION_COUNT; i++) {
                lock.lockRead();
                lock.unlockRead();
                lock.lockWrite();
                lock.unlockWrite();
            }
        }

    } // Runner

} // Main
