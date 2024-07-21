# Exception

Throwable是所有异常的基类，分为两种：Error和Exception。

## Error
Error一般为系统错误或资源耗尽，常见的为内存溢出报错OutOfMemoryError和栈深度溢出StackOverflowError等。该类无法处理的错误由JVM抛出，应用程序端不该抛出或者处理。

## Exception
- ```checked Exception```
**定义**：受检异常指检异常是在编译时被检查的异常。程序必须显式地捕获或声明可能抛出的受检异常，否则代码将无法编译通过。
**特点**：
1.必须要么捕获（使用 try-catch 块），要么通过方法签名中的 throws 关键字进行声明。
2.通常用于可预见的、可以恢复的异常情况。
**常见的受检异常**: 
	- IOException
	- SQLException
	- ClassNotFoundException

- ```unchecked Exception```
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
1.try代码块：一旦异常直接跳转至catch，没有catch则直接跳转至finally
2.catch代码块：若异常则进行处理或者throw向上抛出
3.finally代码块：无论是否有异常，终会执行，如果finally代码块没有执行，则有可能是：
	- 没有进入try代码块
	- 进入try代码块，但是代码运行进入死循环或者死锁
	- 进入try代码块，但是执行了System.exit()

