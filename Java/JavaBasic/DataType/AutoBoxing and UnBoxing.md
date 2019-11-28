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

