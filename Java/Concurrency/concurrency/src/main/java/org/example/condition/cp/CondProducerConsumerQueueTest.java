package org.example.condition.cp;


/**
 *
 */
public class CondProducerConsumerQueueTest {
    public static void main(String[] args) {
        CondBlockingQueue<String> queue = new CondBlockingQueue<>(5);
        new CondProducer(queue).start();
        for (int i = 0; i < 100; i++) {
            new CondConsumer(queue, i).start();
        }
    }
}
