package org.example.coop.fire;

public class RacerThread extends Thread{

    FireFlag fireFlag;
    RacerThread(FireFlag fireFlag) {
        this.fireFlag = fireFlag;
    }

    @Override
    public void run() {
        try {
            fireFlag.waitForFire();
            System.out.println("Start to run" + Thread.currentThread().getName());
        } catch (InterruptedException e) {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        FireFlag flag = new FireFlag();
        for (int i=0;i<10;i++) {
            RacerThread racerThread = new RacerThread(flag);
            racerThread.start();
        }
        Thread.sleep(1000);
        flag.fire();
    }
}
