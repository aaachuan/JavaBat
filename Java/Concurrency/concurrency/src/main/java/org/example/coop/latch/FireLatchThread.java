package org.example.coop.latch;

public class FireLatchThread extends Thread{
    private MyLatch myLatch;

    FireLatchThread(MyLatch myLatch) {
        this.myLatch = myLatch;
    }

    @Override
    public void run() {
        try {
            myLatch.waitForZero();
            System.out.println(Thread.currentThread().getName() + " fire");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyLatch latch = new MyLatch(1);
        for (int i=0; i<10;i++) {
            Thread thread = new FireLatchThread(latch);
            thread.start();
        }
        Thread.sleep(1000);
        latch.countDown();
        System.out.println("Count down to zero.");
    }
}
