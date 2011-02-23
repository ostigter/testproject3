package org.ozsoft.timer;

import junit.framework.Assert;

import org.junit.Test;

public class TimerTest {
    
    @Test
    public void timer() {
        TimerListenerStub listener = new TimerListenerStub();
        Timer timer = new Timer(listener, 1000);
        timer.start();
        sleep(3500);
        Assert.assertEquals(3, listener.getElapseCount());
        timer.stop();
        
        listener.reset();
        sleep(2000);
        Assert.assertEquals(0, listener.getElapseCount());
        
        timer.start();
        sleep(2500);
        Assert.assertEquals(2, listener.getElapseCount());
        timer.stop();
    }
    
    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // Ignore.
        }
    }
    
    private static class TimerListenerStub implements TimerListener {
        
        private int elapseCount = 0;

        public int getElapseCount() {
            return elapseCount;
        }
        
        public void reset() {
            elapseCount = 0;
        }
        
        @Override
        public void timerElapsed() {
            elapseCount++;
        }
        
    }

}
