package org.example.basic;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;

public class ListThreads {
    private static final int _1MB = 1024*1024;

    public static void testAllocation() {
        byte[] a1,a2,a3,a4;
        a1 = new byte[2*_1MB];
        a2 = new byte[2*_1MB];
        a3 = new byte[2*_1MB];
        a4 = new byte[4*_1MB];
    }

    // 辅助方法：通过线程 ID 查找线程对象
    private static Thread findThreadById(long threadId) {
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        for (Thread thread : allThreads.keySet()) {
            if (thread.getId() == threadId) {
                return thread;
            }
        }
        return null;
    }


    public static void main(String[] args) {


            testAllocation();
            //手动触发垃圾回收
            System.gc();

            // 获取当前 Java 进程中的所有线程
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);

            // 遍历所有线程并打印其名称、ID 和状态
            for (ThreadInfo threadInfo : threadInfos) {
                // 获取线程 ID
                long threadId = threadInfo.getThreadId();
                // 获取当前线程对象
                Thread thread = findThreadById(threadId);
                // 判断是否为守护线程
                boolean isDaemon = thread != null && thread.isDaemon();
                System.out.println("Thread name: " + threadInfo.getThreadName() +
                        ", Thread ID: " + threadId +
                        ", Thread state: " + threadInfo.getThreadState() +
                        ", Is daemon: " + isDaemon);
        }

    }
}
