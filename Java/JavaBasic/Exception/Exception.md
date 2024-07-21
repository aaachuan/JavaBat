# Exception

Throwable是所有异常的基类，分为两种：Error和Exception。

## Error
Error一般为系统错误或资源耗尽，常见的为内存溢出报错OutOfMemoryError和栈深度溢出StackOverflowError等。该类无法处理的错误由JVM抛出，应用程序端不该抛出或者处理。

## Exception
### ```checked Exception```

**定义**：受检异常指检异常是在编译时被检查的异常。程序必须显式地捕获或声明可能抛出的受检异常，否则代码将无法编译通过。

**特点**：

1.必须要么捕获（使用 try-catch 块），要么通过方法签名中的 throws 关键字进行声明。

2.通常用于可预见的、可以恢复的异常情况。
  
**常见的受检异常**: 
- IOException
- SQLException
- ClassNotFoundException

### ```unchecked Exception```

**定义**：非受检异常是在运行时被检查的异常。程序不需要显式地捕获或声明这些异常。

**特点**：

1.编译时不强制要求处理（不需要 try-catch 块或 throws 声明）。

2.通常用于编程错误，如逻辑错误、非法参数等。
 
**常见的非受检异常**: 
- NullPointerException
- ArrayIndexOutOfBoundsException
- IllegalArgumentException
- ArithmeticException
  
这些异常均为RuntimeException的子类。

## try-catch-finally
- try代码块：一旦异常直接跳转至catch，没有catch则直接跳转至finally
- catch代码块：若异常则进行处理或者throw向上抛出
- finally代码块：无论是否有异常，终会执行，如果finally代码块没有执行，则有可能是：
	- 没有进入try代码块
	- 进入try代码块，但是代码运行进入死循环或者死锁
	- 进入try代码块，但是执行了System.exit()
```
    public static int finallyNotWork() {
        int temp = 10000;
        try {
            throw new Exception();
        } catch (Exception e) {
            return ++temp;
        } finally {
            temp = 99999;
        }
    }
```
该方法返回最终是10001，而非99999。可以查看其字节码：
```
   L5
    LINENUMBER 9 L5
//变量+1操作
    IINC 0 1
    ILOAD 0
//return结果存储在slot_2中
    ISTORE 2
   L2
    LINENUMBER 11 L2
//finally结果存储在slot_0中
    LDC 99999
    ISTORE 0
   L6
    LINENUMBER 9 L6
//方法返回提取的是slot_2中
    ILOAD 2
    IRETURN
```
注意：
- finally中不要使用return语句。
- 资源关闭优先使用try-with-resources。
注意如下:
```
Lock lock = new XxxLock();
preDo();
try {
	//无论是否加锁成功，unlock都会执行
	lock.lock();
	doSomething();
} finally {
	lock.unlock();
}
```
lock()方法可能会抛出unchecked异常，这种情况下，会触发finally中的unlock()方法。对未加锁的对象解锁同样会抛出unchecked异常，如IllegalMonitorStateException，虽然是因为加锁失败导致的异常，但是unlock()方法抛出的会将其覆盖。所以，lock.lock();应移到try代码块的上方。
