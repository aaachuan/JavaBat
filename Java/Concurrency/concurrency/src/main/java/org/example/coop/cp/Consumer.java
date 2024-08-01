package org.example.coop.cp;

public class Consumer extends Thread{
    MyBlockingQueue<String> queue;
    int i;
    public Consumer(MyBlockingQueue<String> queue,int i) {
        this.queue = queue;
        this.i = i;
    }
    @Override
    public void run() {
        try {
            for(;;) {
                String task = queue.take();
                System.out.println("Consumer" + i +" Handle task "+ task);
                //Thread.sleep((int) (Math.random()*100));
                //Thread.sleep(100);

            }
        } catch (InterruptedException e) {

        }
    }
}

