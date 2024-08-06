package org.example.tool;

import java.util.concurrent.CountDownLatch;

/**
 * 与RacerWithCountDownLatch配合看，很明显就是主从之间的await()与countDown()关系互换
 * 带来的不同的控制开始与结束的结果。
 */
public class MasterWorkerDemo {
    static class Worker extends Thread {
        CountDownLatch latch;
        public Worker(CountDownLatch latch) {
            this.latch = latch;
        }
        @Override
        public void run() {
            try {
                Thread.sleep((int) (Math.random() * 1000));
                if (Math.random() < 0.02) {
                    throw new RuntimeException("bad luck");
                }
            } catch (InterruptedException e) {

            } finally {
                this.latch.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int workerNum = 100;
        CountDownLatch latch = new CountDownLatch(workerNum);
        Worker[] workers = new Worker[workerNum];
        for (int i=0; i<workerNum; i++) {
            workers[i] = new Worker(latch);
            workers[i].start();
        }
        latch.await();
        System.out.println("collect worker results");
    }
}
