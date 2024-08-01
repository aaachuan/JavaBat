package org.example.basic;

public class HelloThread extends Thread{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is the new thread");
        System.out.println("hello");
    }

    public static void main(String[] args) {
        Thread thread = new HelloThread();
        /**
         * 新开线程
         */
        thread.start();
        //System.out.println(Thread.currentThread().getName()+ " is the  main thread");
        /**
         * 直接方法调用
         */
        thread.run();
    }
}
