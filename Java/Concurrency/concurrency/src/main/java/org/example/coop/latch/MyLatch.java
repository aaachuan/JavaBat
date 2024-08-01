package org.example.coop.latch;

public class MyLatch {
    private volatile  int latch = 0;
    MyLatch(int latch) {
        this.latch = latch;
    }
    public synchronized void countDown() throws InterruptedException {
        latch--;
        if (latch<=0){
            notifyAll();
        }
    }

    public synchronized void waitForZero() throws InterruptedException {
        while (latch > 0){
            wait();
        }
    }

}
