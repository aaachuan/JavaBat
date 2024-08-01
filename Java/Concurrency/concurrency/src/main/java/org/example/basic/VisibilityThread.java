package org.example.basic;

public class VisibilityThread {
    private static volatile boolean shutdown = Boolean.FALSE;
    static class HelloToThread implements Runnable {

        @Override
        public void run() {
            while (!shutdown){}
            System.out.println("exit hello");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new HelloToThread()).start();
        Thread.sleep(3000);

        shutdown = Boolean.TRUE;

        System.out.println("exit main");
    }
}
