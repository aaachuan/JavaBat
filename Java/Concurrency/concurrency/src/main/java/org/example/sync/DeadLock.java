package org.example.sync;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadLock {
    private static Object A = new Object();
    private static Object B = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (A) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (B) {

                }
            }
        }).start();

        new Thread(() -> {
            synchronized (B) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (A) {

                }
            }
        }).start();
        Thread.sleep(3000);
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreadIds = threadMXBean.findDeadlockedThreads();

        if (deadlockedThreadIds != null) {
            ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreadIds);
            System.out.println("Deadlocked Threads:");
            for (ThreadInfo threadInfo : threadInfos) {
                System.out.println(threadInfo.getThreadName());
            }
        } else {
            System.out.println("No deadlocked threads detected.");
        }
    }
}
