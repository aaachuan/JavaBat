package org.example.basic;

public class HelloRunnable implements Runnable{

    @Override
    public void run() {

        Thread subThread = new Thread(()->{
            System.out.println("lambda hello");
        });
        subThread.start();
        try {
            subThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("hello");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread runThread = new Thread(new HelloRunnable());
        runThread.start();

        runThread.join();

        System.out.println("HELLO");



    }
}
