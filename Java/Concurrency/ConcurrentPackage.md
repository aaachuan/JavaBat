# 并发包的基石

## 原子变量和CAS

### AtomicInteger
```
    private volatile int value;
    /**
     * Creates a new AtomicInteger with the given initial value.
     *
     * @param initialValue the initial value
     */
    public AtomicInteger(int initialValue) {
        value = initialValue;
    }

    /**
     * Creates a new AtomicInteger with initial value {@code 0}.
     */
    public AtomicInteger() {
    }
```
之所以称为原子变量，是因为它包含一些以原子方式实现组合操作的方法：
```
public final int getAndSet(int newValue)
public final int getAndIncrement()
public final int getAndDecrement()
public final int getAndAdd(int delta)
public final int incrementAndGet()
public final int decrementAndGet()
public final int addAndGet(int delta)
```
这些方法的实现都依赖于：
```

    /**
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }
```
以incrementAndGet为例，JDK7和JDK8的一些实现封装略有差异：
- JDK7
```
    public final int getAndIncrement() {
        for (; ; ) {
            int current = get();
            int next = current + 1;
            if (compareAndSet(current, next))//CAS
                return current;
        }
    }


public final boolean compareAndSet(int expect, int update) {
    return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
}

```
- JDK8封装了自旋锁，直接封装unsafe方法中保证原子性，unsafe是JDK私有的我们不能调用。(JDK7与8的区别主要在于JAVA8把循环都放到了unsafe类里面去。)
```
    public final int getAndAddInt(Object obj, long valueOffset, int delta) {
        int expect;
        //自旋
        do {
            //获取主存的值
            expect = this.getIntVolatile(obj, valueOffset);
        //CAS操作
        } while(!this.compareAndSwapInt(obj, valueOffset, expect, expect + delta));
 
        //返回旧值
        return expect;
    }
```
synchronized是悲观的，代表一种阻塞式算法，得不到锁时，进入锁等待队列，等待其他线程唤醒，有上下文切换开销。原子变量是乐观的，代表一种非阻塞式的更新逻辑，更新冲突时重试，不会阻塞，不会有上下文切换开销。对于大部分比较简单的操作，无论是在低并发还是高并发情况下，这种乐观非阻塞方式的性能都远高于悲观阻塞式方式。

### 实现锁
```
/**
 * 使用AtomicInteger实现自旋锁
 */
public class SpinLock {

	private AtomicInteger state = new AtomicInteger(0);

	/**
	 * 自旋等待直到获得许可
	 */
	public void lock(){
		for (;;){
			//CAS指令要锁总线，效率很差。所以我们通过一个if判断避免了多次使用CAS指令。
			if (state.get() == 1) {
				continue;
			} else if(state.compareAndSet(0, 1)){
				return;
			}
		}
	}

	public void unlock(){
		state.set(0);
	}
}
```
自旋锁实现起来非常简单，如果关键区的执行时间很短，往往自旋等待会是一种比较高效的做法，它可以避免线程的频繁切换和调度。但如果关键区的执行时间很长，那这种做法就会大量地浪费CPU资源。

```
/**
 * 使用AtomicInteger实现可等待锁
 */
public class BlockLock implements Lock {
    
    private AtomicInteger state = new AtomicInteger(0);
    private ConcurrentLinkedQueue<Thread> waiters = new ConcurrentLinkedQueue<>();

    @Override
    public void lock() {
        if (state.compareAndSet(0, 1)) {
            return;
        }
        //放到等待队列
        waiters.add(Thread.currentThread());

        for (;;) {
            if (state.get() == 0) {
                if (state.compareAndSet(0, 1)) {
                    waiters.remove(Thread.currentThread());
                    return;
                }
            } else {
                LockSupport.park();     //挂起线程
            }
        }
    }

    @Override
    public void unlock() {
        state.set(0);
        //唤醒等待队列的第一个线程
        Thread waiterHead = waiters.peek();
        if(waiterHead != null){
            LockSupport.unpark(waiterHead);     //唤醒线程
        }
    }
}
```
如果关键区的执行时间很长，自旋的锁会大量地浪费CPU资源，我们可以这样改进：当一个线程拿不到锁的时候，就让这个线程先休眠等待。这样，CPU就不会白白地空转了。大致步骤如下：
- 需要一个容器，如果线程抢不到锁，就把线程挂起来，并记录到这个容器里。
- 当一个线程放弃了锁，得从容器里找出一个挂起的线程，把它恢复了。

### ABA问题
ABA问题在于约定的比较后才进行更新的“比较”条件被破坏了。若T1与T2均要将A更新成B，此时应该有一个会更新失败，但是由于T3的参与（B更新成A），导致T1、T3和T2出现全部更新成功的结果。
Java中提供了AtomicStampedReference来解决该问题。每次compareAndSwap后给数据的版本号加1，下次compareAndSwap的时候不仅比较数据，也比较版本号，值相同，版本号不同也不能执行成功。
```
private static class Pair<T> {
    final T reference;
    final int stamp;
    private Pair(T reference, int stamp) {
        this.reference = reference;
        this.stamp = stamp;
    }
}
public boolean compareAndSet(V   expectedReference,
                             V   newReference,
                             int expectedStamp,
                             int newStamp) {
    Pair<V> current = pair;
    return
        expectedReference == current.reference &&
        expectedStamp == current.stamp &&
        ((newReference == current.reference &&
          newStamp == current.stamp) ||
         casPair(current, Pair.of(newReference, newStamp)));
}
private boolean casPair(Pair<V> cmp, Pair<V> val) {
    return UNSAFE.compareAndSwapObject(this, pairOffset, cmp, val);
}
```
- 首先判断传入的参数是否符合 Pair 的预期，从数据和版本号两个方面来判断，有一个不符合就打回；
- 如果传入的参数与Pair中的一样，直接返回true，不用更新；
- 使用casPair来比较交换当前的Pair与传入参数构成的Pair；
- casPair又调用compareAndSwapObject来交互Pair属性。

## 显示锁
### 接口Lock
```
public interface Lock {

    void lock();

    void lockInterruptibly() throws InterruptedException;

    boolean tryLock();
    
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    void unlock();

    Condition newCondition();
}
```
相比synchronized，显示锁支持以非阻塞方式获取锁、可以响应中断、可以限时，使得更灵活。

### 可重入锁ReentrantLock
除了解决竞态条件、保证内存可见性，还有可重入，即一个线程在持有一个锁的前提下，可以继续获得该锁。
```

    /**
     * Creates an instance of {@code ReentrantLock}.
     * This is equivalent to using {@code ReentrantLock(false)}.
     */
    public ReentrantLock() {
        sync = new NonfairSync();
    }

    /**
     * Creates an instance of {@code ReentrantLock} with the
     * given fairness policy.
     *
     * @param fair {@code true} if this lock should use a fair ordering policy
     */
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
```
公平指等待时间最长的线程优先获得锁。保证公平会影响性能，一般也不需要，所以默认不保证，synchronized锁也是不保证公平的。

tryLock避免死锁：在持有一个锁获取另一个锁而获取不到的时候，可以释放已持有的锁，给其他线程获取锁的机会，然后重试获取所有锁。

##### LockSupport
```
public static void park()
public static void parkNanos(long nanos)
public static void parkUntil(long deadline)
public static void unpark(Thread thread)
```
park使当前线程放弃CPU，进入WAITING状态，操作系统不再进行调度。不同于Thread.yield()，yield()只是告诉操作系统可以先让其他线程运行，但自身仍是可运行状态。
```
public class LockSupportTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            LockSupport.park();
            System.out.println("exit");
        });
        t.start();
        System.out.println("main thread continue while start t thread");
        Thread.sleep(1000);
        LockSupport.unpark(t);
    };
}
```
park响应中断，中断发生时park会返回，线程的中断状态会被设置。另外，park可能会无缘无故地返回，程序应该重新检查park等待的条件是否满足。

#### AQS
封装状态：
```
private volatile int state;
protected final int getState()
protected final void setState(int newState)
protected final boolean compareAndSetState(int expect, int update)
```
用于实现锁时，AQS可以保存锁的当前持有线程：
```
private transient Thread exclusiveOwnerThread;
protected final void setExclusiveOwnerThread(Thread t)
protected final Thread getExclusiveOwnerThread()
```
AQS内部维护了一个等待队列，借助CAS方法实现了无阻塞算法进行更新。

以ReentranrLock为例简要介绍AQS的原理。
```
abstract static class Sync extends AbstractQueuedSynchronizer
static final class NonfairSync extends Sync
static final class FairSync extends Sync

private final Sync sync;
public ReentrantLock() {
 sync = new NonfairSync();
}
```
```
public void lock() {
 sync.lock();
}
```
sync默认类型为NonfairSync，NonfairSync的lock代码为：
```
final void lock() {
 if(compareAndSetState(0, 1))
 setExclusiveOwnerThread(Thread.currentThread());
 else
 acquire(1);
}
```
ReetrantLock使用state表示是否被锁和持有数量，如果当前未被锁定，则立即获得锁，否则调用acquire(1)获得锁。acquire是AQS的方法：
```

    /**
     * Acquires in exclusive mode, ignoring interrupts.  Implemented
     * by invoking at least once {@link #tryAcquire},
     * returning on success.  Otherwise the thread is queued, possibly
     * repeatedly blocking and unblocking, invoking {@link
     * #tryAcquire} until success.  This method can be used
     * to implement method {@link Lock#lock}.
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquire} but is otherwise uninterpreted and
     *        can represent anything you like.
     */
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```
它调用tryAcquire获取锁，tryAcquire必须被子类重写，NonfairSync实现为：
```
protected final boolean tryAcquire(int acquires) {
 return nonfairTryAcquire(acquires);
}
```
```

        /**
         * Performs non-fair tryLock.  tryAcquire is implemented in
         * subclasses, but both need nonfair try for trylock method.
         */
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
```
如果未被锁定，则使用CAS进行锁定；如果已被当前线程锁定，则增加锁定次数。若tryAcquire返回false，则AQS会调用：
```
acquireQueued(addWaiter(Node.EXCLUSIVE), arg)

```
addWaiter会新建一个节点Node，代表当前线程，然后加入内部的等待队列中。
```

    /**
     * Creates and enqueues node for current thread and given mode.
     *
     * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
     * @return the new node
     */
    private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
```
放入等待队列后，调用acquireQueued尝试获得锁。
```

    /**
     * Acquires in exclusive uninterruptible mode for thread already in
     * queue. Used by condition wait methods as well as acquire.
     *
     * @param node the node
     * @param arg the acquire argument
     * @return {@code true} if interrupted while waiting
     */
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```
主体是一个死循环，在每次循环中，首先检查当前节点是不是第一个等待的节点，如果是且能获得到锁，则将当前节点从等待队列中移除并返回，否则最终调用LockSupport.park放弃CPU，进入等待，被唤醒后，检查是否发生了中断，记录中断标志，在最终方法返回时返回中断状态。

以上就是lock方法的基本过程，能获得锁就立即获得，否则加入等待队列，被唤醒后检查自己是否是第一个等待的线程，如果是且能获得锁，则返回，否则继续等待。这个过程若发生中断，lock会记录中断标志位，但不会提前返回或抛出异常。

ReetrantLock的unlock方法代码为:
```
public void unlock() {
 sync.release(1);
}
```
```
public final boolean release(int arg) {
 if(tryRelease(arg)) {
 Node h = head;
 if(h != null && h.waitStatus != 0)
 unparkSuccessor(h);
 return true;
 }
 return false;
}
```
tryRelease方法会修改状态释放锁，unparkSuccessor会调用LockSupport.unpark将第一个等待的线程唤醒。
FairSync和NonfairSync的主要区别是：在获取锁时，即在tryAcquire方法中，如果当前未被锁定，即c==0，FairSync多了一个检查：
```
protected final boolean tryAcquire(int acquires) {
 final Thread current = Thread.currentThread();
 int c = getState();
 if(c == 0) {
 if(!hasQueuedPredecessors() &&
 compareAndSetState(0, acquires)) {
 setExclusiveOwnerThread(current);
 return true;
 }
 }
 ...
```
这个检查为只有不存在其他等待时间更长的线程，它才会尝试获取锁。

保证公平整体性能低的原因不是检查慢，而是会让活跃线程得不到锁，进入等待状态，引起频繁上下文切换，降低整体效率。即使fair参数为true，ReentrantLock中不带参数的tryLock方法也是不保证公平，不会检查是否有其他等待时间更长的线程。

对比ReentrantLock和synchronized

## 显式条件
锁用于解决竞态条件问题，条件是线程间的协作机制。显式锁与synchronized相对应，而显式条件与wait/notify相对应。
```
public interface Condition {
 void await() throws InterruptedException;
 void awaitUninterruptibly();
 long awaitNanos(long nanosTimeout) throws InterruptedException;
 boolean await(long time, TimeUnit unit) throws InterruptedException;
 boolean awaitUntil(Date deadline) throws InterruptedException;
 void signal();
 void signalAll();
}
```

```
static class MyBlockingQueue<E> {
 private Queue<E> queue = null;
 private int limit;
 private Lock lock = new ReentrantLock();
 private Condition notFull = lock.newCondition();
 private Condition notEmpty = lock.newCondition();
 public MyBlockingQueue(int limit) {
 this.limit = limit;
 queue = new ArrayDeque<>(limit);
 }
 public void put(E e) throws InterruptedException {
 lock.lockInterruptibly();
 try{
 while (queue.size() == limit) {
 notFull.await();
 }
 queue.add(e);
 notEmpty.signal();
 }finally{
 lock.unlock();
 }
 }
 public E take() throws InterruptedException {
 lock.lockInterruptibly();
 try{
 while(queue.isEmpty()) {
 notEmpty.await();
 }
 E e = queue.poll();
 notFull.signal();
 return e;
 }finally{
 lock.unlock();
 }
 }
}
```