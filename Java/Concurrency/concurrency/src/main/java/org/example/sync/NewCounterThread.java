package org.example.sync;

public class NewCounterThread implements Runnable{
    Counter c;

    NewCounterThread(Counter c) {
        this.c = c;
    }

    @Override
    public void run() {
        for (int i=0; i < 1000; i++){
            c.incr();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        int num = 1000;
        Counter c = new Counter();
        Thread[] threads = new Thread[num];
        for (int i=0; i<num; i++) {
            threads[i] = new Thread(new NewCounterThread(c));
            threads[i].start();
        }
        for (int i=0; i<num; i++) {
            threads[i].join();
        }
        System.out.println(c.get());
    }


}
