package locking;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom implementation of an re-entrant read/write lock.
 * 
 * @author Oscar Stigter
 */
public class CustomLock {

    private final Map<Thread, Integer> readingThreads;
    private int writeAccesses = 0;
    private int writeRequests = 0;
    private Thread writingThread = null;

    public CustomLock() {
        readingThreads = new HashMap<Thread, Integer>();
    }

    public synchronized void lockRead() {
        Thread callingThread = Thread.currentThread();
        while (!canGrantReadAccess(callingThread)) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Ignore.
            }
        }

        readingThreads.put(callingThread,
                (getReadAccessCount(callingThread) + 1));
    }

    private boolean canGrantReadAccess(Thread callingThread) {
        if (isWriter(callingThread))
            return true;
        if (hasWriter())
            return false;
        if (isReader(callingThread))
            return true;
        if (hasWriteRequests())
            return false;
        return true;
    }

    public synchronized void unlockRead() {
        Thread callingThread = Thread.currentThread();
        if (!isReader(callingThread)) {
            throw new IllegalMonitorStateException("Calling Thread does not"
                    + " hold a read lock on this Lock");
        }
        int accessCount = getReadAccessCount(callingThread);
        if (accessCount == 1) {
            readingThreads.remove(callingThread);
        } else {
            readingThreads.put(callingThread, (accessCount - 1));
        }
        notifyAll();
    }

    public synchronized void lockWrite() {
        writeRequests++;
        Thread callingThread = Thread.currentThread();
        while (!canGrantWriteAccess(callingThread)) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
        writeRequests--;
        writeAccesses++;
        writingThread = callingThread;
    }

    public synchronized void unlockWrite() {
        if (!isWriter(Thread.currentThread())) {
            throw new IllegalMonitorStateException(
                    "Calling Thread does not hold the write lock on this Lock");
        }

        writeAccesses--;
        if (writeAccesses == 0) {
            writingThread = null;
        }
        notifyAll();
    }

    private boolean canGrantWriteAccess(Thread callingThread) {
        if (isOnlyReader(callingThread))
            return true;
        if (hasReaders())
            return false;
        if (writingThread == null)
            return true;
        if (!isWriter(callingThread))
            return false;
        return true;
    }

    private int getReadAccessCount(Thread callingThread) {
        Integer accessCount = readingThreads.get(callingThread);
        if (accessCount == null)
            return 0;
        return accessCount.intValue();
    }

    private boolean hasReaders() {
        return readingThreads.size() > 0;
    }

    private boolean isReader(Thread callingThread) {
        return readingThreads.get(callingThread) != null;
    }

    private boolean isOnlyReader(Thread callingThread) {
        return readingThreads.size() == 1
                && readingThreads.get(callingThread) != null;
    }

    private boolean hasWriter() {
        return writingThread != null;
    }

    private boolean isWriter(Thread callingThread) {
        return writingThread == callingThread;
    }

    private boolean hasWriteRequests() {
        return this.writeRequests > 0;
    }

}
