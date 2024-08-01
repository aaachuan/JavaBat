package org.example.basic;

public class CounterThread implements Runnable{

    private static int c = 0;
    @Override
    public void run() {
        for (int i=0; i < 1000; i++){
            c++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int num = 1000;
        Thread[] threads = new Thread[num];
        for (int i=0; i<num; i++) {
            threads[i] = new Thread(new CounterThread());
            threads[i].start();
        }
        for (int i=0; i<num; i++) {
            threads[i].join();
        }
        System.out.println(c);
    }
}
