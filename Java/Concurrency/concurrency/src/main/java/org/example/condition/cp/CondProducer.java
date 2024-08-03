package org.example.condition.cp;


public class CondProducer extends Thread{
    CondBlockingQueue<String> queue;
    public CondProducer(CondBlockingQueue<String> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        int num = 0;
        try {
          for(;;) {
              String task = String.valueOf(num);
              queue.put(task);
              System.out.println("Produce task " + task);
              num++;
              //Thread.sleep((int) (Math.random()*100));
              //Thread.sleep(5000);
          }
        } catch (InterruptedException e) {

        }
    }
}
