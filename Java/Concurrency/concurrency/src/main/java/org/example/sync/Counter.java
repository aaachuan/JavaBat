package org.example.sync;

public class Counter {
    /**
     * synchronized保护的是对象，而非方法。对于同一个Counter对象，
     * 一个线程执行incr()，另一个线程执行get()的时候也是顺序执行。
     * 如下例子保护的是实例对象，而当synchronized修饰静态变量时，保护的是Counter.class
     * 每个对象都有一个锁和等待队列
     */
    private int c;
    public synchronized void incr() {
        c ++;
    }
    public synchronized int get() {
        return c;
    }
}
