package org.example.coop.cp;


/**
 * 只使用notify()而没有使用notifyALL()导致多消费者WAITING饥饿等待
 */
public class ProducerConsumerQueueTest {
    public static void main(String[] args) {
        MyBlockingQueue<String> queue = new MyBlockingQueue<>(5);
        new Producer(queue).start();
        for (int i=0;i<100;i++) {
            new Consumer(queue,i).start();
        }

    }
}
