package org.example.condition.cp;

public class CondConsumer extends Thread{
    CondBlockingQueue<String> queue;
    int i;
    public CondConsumer(CondBlockingQueue<String> queue,int i) {
        this.queue = queue;
        this.i = i;
    }
    @Override
    public void run() {
        try {
            for(;;) {
                String task = queue.take();
                System.out.println("Consumer" + i +" Handle task "+ task);
                Thread.sleep((int) (Math.random()*100));
                //Thread.sleep(100);

            }
        } catch (InterruptedException e) {

        }
    }
}
