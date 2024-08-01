package org.example.coop;

public class WaitThread extends Thread {

    private volatile boolean fire = false;

    @Override
    public void run() {
        synchronized (this) {
            while (!fire) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("fired");
    }

    public synchronized void fire(){
        this.fire = true;
        notify();
    }


    public static void main(String[] args) throws InterruptedException {
        WaitThread waitThread = new WaitThread();
        waitThread.start();
        Thread.sleep(1000);
        System.out.println("fire");
        waitThread.fire();
    }

}
