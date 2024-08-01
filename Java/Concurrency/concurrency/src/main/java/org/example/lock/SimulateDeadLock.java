package org.example.lock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class SimulateDeadLock {
    public static void main(String[] args) throws InterruptedException {
        Account a1 = new Account(100);
        Account a2 = new Account(100);

        new Thread(()->{
            try {
                AccountMgr.transferring(a1,a2,10);
            } catch (AccountMgr.NoEnoughMoneyException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(()->{
            try {
                AccountMgr.transferring(a2,a1,10);
            } catch (AccountMgr.NoEnoughMoneyException e) {
                throw new RuntimeException(e);
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
