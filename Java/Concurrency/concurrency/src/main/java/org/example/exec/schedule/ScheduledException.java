package org.example.exec.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledThreadPoolExecutor 和 FutureTask 默认行为是捕获所有异常，并存储在 FutureTask 中，而不直接打印到控制台。
 */
public class ScheduledException {
    static class TaskA implements Runnable {
        @Override
        public void run() {
            System.out.println("task A");
        }
    }

    static class TaskB implements Runnable {
        @Override
        public void run() {

            System.out.println("task B");
                throw new RuntimeException();
        }
    }

    public static void main(String[] args) {


        //ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        //ScheduledExecutorService timer = Executors.newScheduledThreadPool(2); // 使用多线程池
        CustomScheduledExecutorService timer =new CustomScheduledExecutorService(1);
        //timer.scheduleWithFixedDelay(new TaskA(), 0,1, TimeUnit.SECONDS);
        timer.scheduleWithFixedDelay(new TaskB(), 2,1, TimeUnit.SECONDS);



    }
}
