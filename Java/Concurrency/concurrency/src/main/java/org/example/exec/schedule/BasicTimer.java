package org.example.exec.schedule;

import java.util.Timer;
import java.util.TimerTask;

public class BasicTimer extends TimerTask {
    @Override
    public void run() {
        System.out.println("delayed task");
    }

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new BasicTimer(),1000);
        Thread.sleep(3000);
        timer.cancel();
    }
}
