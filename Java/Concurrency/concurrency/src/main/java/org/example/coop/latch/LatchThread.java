package org.example.coop.latch;

public class LatchThread extends Thread{

    private MyLatch myLatch;

    LatchThread(MyLatch myLatch) {
        this.myLatch = myLatch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((int)(Math.random()*100));
            myLatch.countDown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyLatch latch = new MyLatch(10);
        for (int i=0; i<10;i++) {
            Thread thread = new LatchThread(latch);
            thread.start();
        }
        latch.waitForZero();
        System.out.println("Count down to zero.");
    }
}
