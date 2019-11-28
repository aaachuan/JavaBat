# AutoBoxing and UnBoxing

JDK 1.5开始，引入自动装箱和拆箱机制，目的是在变量的赋值和方法的调用，让基本数据类型和其对应包装对象类型的转换更加简单直接。整个过程由Java编译器完成。
```
Integer x = 1; // 自动装箱，调用Integer.valueOf(1)，即Integer x = Integer.valueOf(1);
int y = x;    //  自动拆箱，调用Integer.intValue()，即int y = x.intValue();
```
变量赋值和方法调用时的简单例子：
```
ArrayList<Integer> intList = new ArrayList<Integer>();
intList.add(1); //autoboxing - primitive to object
intList.add(2); //autoboxing

ThreadLocal<Integer> intLocal = new ThreadLocal<Integer>();
intLocal.set(4); //autoboxing

int number = intList.get(0); // unboxing
int local = intLocal.get(); // unboxing in Java
```

当然，在自动装箱使用不恰当的情况下，也会影响程序的性能。

```
Integer sum = 0;
 for(int i=1000; i<5000; i++){
   sum+=i;
}
```
`sum+=i`即`sum = sum + i`。因为`sum`和`i`类型不一致，所以运算时内部发生的是：
```
int result = sum.intValue() + i;
Integer sum = new Integer(result);
```
因为sum为Integer类型，在上面的循环中会创建将近4000个无用的Integer对象，在这样庞大的循环中，会降低程序的性能并且加重了垃圾回收的工作量。
```
/*
		 * 自动装箱和拆箱的弊端
		 */
		long st1 = System.nanoTime();
		
		Integer sum1 = 0;
		 for(int i=1000; i<5000; i++){
		   sum1 += i;
		}
		
		long et1 = System.nanoTime();
		long t1 = et1 - st1;
		System.out.println(t1 + "ns");
		
		long st2 = System.nanoTime();
		
		int sum2 = 0;
		 for(int i=1000; i<5000; i++){
		   sum2 += i;
		}
		 
		long et2 = System.nanoTime();
		long t2 = et2 - et1;
		System.out.println(t2 + "ns");
    
```
测试输出两者对比的运行时间分别是：
```
1040405ns
619520ns
```
多次调试试验，Integer的运行时间比int都要长。

但是把循环稍微改下，从1-100循环，发现后者反而要比前者的运行时间长！
```
28743ns
649291ns
```
这个原因很大概率和valueOf()的IntegerCache有关。（挖个坑）

另一个需要避免的问题就是混乱使用对象和原始数据值，一个具体的例子就是当我们在一个原始数据值与一个对象进行比较时，如果这个对象没有进行初始化或者为Null，在自动拆箱过程中obj.xxxValue，会抛出NullPointerException,如下面的代码
```
Integer count = null;

		//NullPointerException on unboxing
		if( count <= 0){
		  System.out.println("Count is not started yet");
		}
```

Ref:

[Java官方文档:Autoboxing and Unboxing](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html)
