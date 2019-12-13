# Java并发编程的艺术
## Chap 1
### 多线程不一定快
比较并发和串行，当累加操作没有达到一定量级时，并发执行的速度有时候反而比串行慢。主要是因为线程有创建和上下文切换的开销。
```
package com.javabasic.concurrency;


public class ConcurrencyTest {

	private static final long count = 10000l;
	
	public static void main(String[] args) throws InterruptedException {
		
		concurrency();
		serial();

	}
	
	private static void concurrency() throws InterruptedException {
		long start = System.currentTimeMillis();
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int a = 0;
				for (long i = 0; i < count; i++) {
					a += 5;
				}
				
			}
		});
		
		thread.start();
		
		int b = 0;
		for (long i = 0; i < count; i++) {
			b--;
		}
		
		long time = System.currentTimeMillis() - start;
		thread.join();
		System.out.println("concurrency : " + time + "ms,b=" + b);
		
	}
	
	private static void serial() {
		long start = System.currentTimeMillis();
		
		int a = 0;
		for (long i = 0; i < count; i++) {
			a += 5;
		}
		
		int b = 0;
		for (long i = 0; i < count; i++) {
			b--;
		}
		
		long time = System.currentTimeMillis() - start;
		System.out.println("serial : " + time + "ms,b=" + b + ",a=" +a);
		
		
	}
}
```
### 死锁
线程互相等待对方释放锁：
```
package com.javabasic.concurrency;

public class DeadLockDemo {
	
	private static String A = "A";
	
	private static String B = "B";

	public static void main(String[] args) {
		new DeadLockDemo().deadLock();

	}
	
	private void deadLock() {
		
		new Thread( () -> {
		
			synchronized (A) {
				try {
					Thread.currentThread().sleep(1000);
				} catch ( InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (B) {
					System.out.println("1");
				}
			}
			
		}).start();
		
		
		new Thread( () -> {
			
			synchronized (B) {
				
				synchronized (A) {
					System.out.println("2");
				}
			}
			
		}).start();
		 
	}

}

```
当把上面`try...catch`块代码注释掉时，并不会发生死锁，因为在相当短的时间里，线程已经完成并释放锁资源。示例代码使用Lambda表达式改写了下`Thread(new Runnable...)`，简洁明了许多。

避免死锁的方法：
- 避免一个线程同时获得多个锁。
- 避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源。
- 尝试使用定时锁，如使用`lock.tryLock(timeout)`来替代内部锁机制。
- 数据库锁的加锁和解锁都必须在一个数据库连接里面，否则会出现解锁失败。


