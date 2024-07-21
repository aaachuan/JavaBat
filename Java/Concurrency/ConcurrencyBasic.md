# 并发基础知识

## 线程的基本概念
1、创建线程
- 继承Thread类
- 实现Runnable接口

覆盖run()方法，通过start()方法启动线程。【模板模式、策略模式】
注意，Thread类本身实现Runnable接口：
```
public
class Thread implements Runnable {


    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain #Thread(ThreadGroup,Runnable,String) Thread}
     * {@code (null, target, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     *
     * @param  target
     *         the object whose {@code run} method is invoked when this thread
     *         is started. If {@code null}, this classes {@code run} method does
     *         nothing.
     */
    public Thread(Runnable target) {
        init(null, target, "Thread-" + nextThreadNum(), 0);
    }

    /**
     * Causes this thread to begin execution; the Java Virtual Machine
     * calls the <code>run</code> method of this thread.
     * <p>
     * The result is that two threads are running concurrently: the
     * current thread (which returns from the call to the
     * <code>start</code> method) and the other thread (which executes its
     * <code>run</code> method).
     * <p>
     * It is never legal to start a thread more than once.
     * In particular, a thread may not be restarted once it has completed
     * execution.
     *
     * @exception  IllegalThreadStateException  if the thread was already
     *               started.
     * @see        #run()
     * @see        #stop()
     */
    public synchronized void start() {
        /**
         * This method is not invoked for the main method thread or "system"
         * group threads created/set up by the VM. Any new functionality added
         * to this method in the future may have to also be added to the VM.
         *
         * A zero status value corresponds to state "NEW".
         */
        if (threadStatus != 0)
            throw new IllegalThreadStateException();

        /* Notify the group that this thread is about to be started
         * so that it can be added to the group's list of threads
         * and the group's unstarted count can be decremented. */
        group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                /* do nothing. If start0 threw a Throwable then
                  it will be passed up the call stack */
            }
        }
    }

    private native void start0();

    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> run object, then that
     * <code>Runnable</code> object's <code>run</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see     #start()
     * @see     #stop()
     * @see     #Thread(ThreadGroup, Runnable, String)
     */
    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
}

```
- start方法用synchronized修饰，为同步方法；
- 虽然为同步方法，但不能避免多次调用问题，用threadStatus来记录线程状态，如果线程被多次start会抛出异常；threadStatus的状态由JVM控制。
- 使用Runnable时，主线程无法捕获子线程中的异常状态。线程的异常，应在线程内部解决。

看一段自己写的实验代码：
```
package com.aaachuan.io;

public class ThreadTest extends Thread {
    public static void main(String[] args) {
        ThreadTest test = new ThreadTest();
        test.start();
        test.start();
    }

    @Override
    public void run() {
        int num = 2;
        Thread[] threads = new Thread[num];
        for (int i = 0; i < num; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> System.out.println(finalI));
            threads[i].start();
        }
    }
}

```
输出为：
```
Exception in thread "main" java.lang.IllegalThreadStateException
	at java.lang.Thread.start(Thread.java:705)
	at com.aaachuan.io.ThreadTest.main(ThreadTest.java:7)
0
1
```

Runnable接口（支持Java8`()->{}`写法）：
```
@FunctionalInterface
public interface Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
    public abstract void run();
}
```
`Thread.start()`将线程启动后，`Thread.run()`方法将会被JVM进行回调。
2、线程的状态
```
public enum State {
        /**
         * Thread state for a thread which has not yet started.
         */
        NEW,

        /**
         * Thread state for a runnable thread.  A thread in the runnable
         * state is executing in the Java virtual machine but it may
         * be waiting for other resources from the operating system
         * such as processor.
         */
        RUNNABLE,

        /**
         * Thread state for a thread blocked waiting for a monitor lock.
         * A thread in the blocked state is waiting for a monitor lock
         * to enter a synchronized block/method or
         * reenter a synchronized block/method after calling
         * {@link Object#wait() Object.wait}.
         */
        BLOCKED,

        /**
         * Thread state for a waiting thread.
         * A thread is in the waiting state due to calling one of the
         * following methods:
         * <ul>
         *   <li>{@link Object#wait() Object.wait} with no timeout</li>
         *   <li>{@link #join() Thread.join} with no timeout</li>
         *   <li>{@link LockSupport#park() LockSupport.park}</li>
         * </ul>
         *
         * <p>A thread in the waiting state is waiting for another thread to
         * perform a particular action.
         *
         * For example, a thread that has called <tt>Object.wait()</tt>
         * on an object is waiting for another thread to call
         * <tt>Object.notify()</tt> or <tt>Object.notifyAll()</tt> on
         * that object. A thread that has called <tt>Thread.join()</tt>
         * is waiting for a specified thread to terminate.
         */
        WAITING,

        /**
         * Thread state for a waiting thread with a specified waiting time.
         * A thread is in the timed waiting state due to calling one of
         * the following methods with a specified positive waiting time:
         * <ul>
         *   <li>{@link #sleep Thread.sleep}</li>
         *   <li>{@link Object#wait(long) Object.wait} with timeout</li>
         *   <li>{@link #join(long) Thread.join} with timeout</li>
         *   <li>{@link LockSupport#parkNanos LockSupport.parkNanos}</li>
         *   <li>{@link LockSupport#parkUntil LockSupport.parkUntil}</li>
         * </ul>
         */
        TIMED_WAITING,

        /**
         * Thread state for a terminated thread.
         * The thread has completed execution.
         */
        TERMINATED;
    }
```
3、共享内存可能出现的问题
- 竞态条件（解决：使用synchronized、显式锁、原子变量）
- 内存可见性（解决：使用volatile、synchronized或显式锁同步）

## synchronized
1、实例方法（ACC_SYNCHRONIZED标记；monitor对象锁）
- 多个线程是可以同时执行同一个synchronized实例方法的，只要它们访问的对象是不同的即可。synchronized实例方法实际保护的是同一个对象的方法调用，即`this`，`this`对象有一个锁和一个等待队列。
- synchronized保护的是对象而非代码，只要访问的是同一个对象的synchronized方法，即使是不同的代码，也会被同步顺序访问。但不保证非synchronized方法被同时执行。

2、静态方法（ACC_SYNCHRONIZED标记；monitor类对象锁）
此时synchronized保护的是类对象，与实例方法的对象不同，不冲突。

3、代码块（monitorenter和monitorexit）
包装代码，synchronized同步的对象可以是任意对象。

4、特性
- 可重入性（记录锁的持有线程和持有数量）
- 保证内存可见性（在释放锁时，所有写入都会写回内存，获得锁后，都会从内存读最新数据）

5、线程安全的同步容器
SynchronizedCollection、SynchronizedList、SynchronizedMap[看源码：加对象锁]。所有方法加了synchronized调用变成原子操作，但是客户端调用时并非绝对安全。
- 客户端额外定义包装成复合操作
- 伪同步（同步错对象）——>所有方法必须使用相同的锁。【由synchronized的使用特性决定的】
- 迭代（同步容器不能解决ConcurrentModificationException，要避免需要在遍历的时候给整个容器对象加锁）

6、线程安全的并发容器（不使用synchronized，同时解决同步容器在并发量大的性能问题）
- CopyOnWriteArrayList
- ConcurrentHashMap
- ConcurrentLinkedQueue
- ConcurrentSkipListSet

## 线程的基本协作机制
1、wait/notify
```
    public final native void wait(long timeout) throws InterruptedException;
   public final void wait() throws InterruptedException {
        wait(0);
    }
```
除了用于锁的等待队列，每个对象还有另一个等待队列，表示条件队列，该队列用于线程间的协作。
```
    public final native void notify();
    public final native void notifyAll();
```

```
public class WaitThread extends Thread{
    private volatile boolean fire = false;

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!fire) {
                    wait();
                }
            }
            System.out.println("fired");
        } catch (InterruptedException e) {

        }
    }

    public synchronized void fire() {
        this.fire = true;
        notify();
    }

    public static void main(String[] args) throws InterruptedException {
        WaitThread waitThread = new WaitThread();
        waitThread.start();
        Thread.sleep(1000);
        System.out.println("fire");
        waitThread.fire();
    }
}
```
wait/notify方法只能在synchronized代码块内被调用，如果调用wait/notify方法时，当前线程没有持有对象锁，会抛出异常java.lang.IllegalMonitorStateException。

wait的具体过程：
1、把当前线程放入条件等待队列，释放对象锁，阻塞等待，线程状态变为WAITING或TIMED_WAITING。
2、等待时间到或被其他线程调用notify/notifyAll从条件队列中移除，这时，要重新竞争对象锁。
- 如果能获得锁，线程状态变成RUNNABLE，并从wait调用中返回。
- 否则，该线程加入对象锁等待队列，线程状态变成BLOCKED，只有在获得锁后才会从wait调用中返回。

线程从wait调用中返回后，不代表等待的条件就一定成立了，需要重新检查其等待的条件：
```
           synchronized (obj) {
               while (<condition does not hold>)
                   obj.wait();
               ... // Perform action appropriate to condition
           }
```
调用notify会把在条件队列中等待的线程唤醒并从队列中移除，但它不会释放对象锁，也就是说，只有在包含notify的synchronized代码块执行完后，等待的线程才会从wait调用中返回。只能有一个条件等待队列，这是Java wait/notify机制的局限性。

## 线程的中断
在Java中，停止一个线程的主要机制是中断，中断并不是强迫终止一个线程，它是一种协作机制，是给线程传递一个取消信号，但是由线程来决定如何以及何时退出。
```
public boolean isInterrupted()//返回对应线程的中断标志位是否为true
public void interrupt()
public static boolean interrupted()//返回对应线程的中断标志位是否为true，但副作用是清空中断标志位。
```
如下状态调用interrupt()时：
- NEW/TERMINATE：没有效果，也不会设置中断标志位。
- RUNNABLE：没有执行IO操作时，只会设置线程的中断标志位。
- WAITING/TIMED_WAITING（join/wait/sleep）：抛出InterruptedException异常，然后清空中断标志位。
- BLOCKED：只会设置线程的中断标志位。

综上，interrupt()只是一种协作机制，并不一定能真正去“中断”线程。所以我们只能从调用interrupt()方法所产生的作用结果反向设置线程的运行条件。比如：

- RUNNABLE：```while(!Thread.currentThread.isInterrupted){//单次循环代码}```；
- WAITING/TIMED_WAITING：向上传递异常；捕获异常重新设置中断标志位

而在使用synchronized关键字获取锁的过程中不响应中断请求，这是synchronized的局限性。（因为在同步代码块中，无法从锁等待队列中出来）此时应该使用显式锁（Lock接口）。

封装的取消/关闭线程：
Future接口```boolean cancel(boolean mayInterruptIfRunning);```
ExecutorService的```void shutdown();```和```List<Runnable> shutdownNow();```