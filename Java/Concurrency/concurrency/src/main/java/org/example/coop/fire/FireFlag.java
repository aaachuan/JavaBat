package org.example.coop.fire;

public class FireFlag {
    private volatile static boolean flag = false;

    public synchronized void waitForFire() throws InterruptedException {
        while (!flag) {
            wait();
        }
    }

    public synchronized void fire() {
        flag = true;
        notifyAll();
    }

}
