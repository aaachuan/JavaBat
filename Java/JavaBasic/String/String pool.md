# String pool

## JVM运行时数据区域
JVM运行时数据区域中，Method Area和Heap为所有线程共享的数据区，Program Counter Register和VM Stack及Native Method Stack则是线程私有的。
### 程序计数器(Program Counter Register)

此内存区域是Java虚拟机规范中没有规定任何OutOfMemoryError情况的区域。

### Java虚拟机栈(VM Stack)

VM Stack描述的是Java方法执行的内存模型：每个方法在执行的同时都会创建一个Stack Frame用于存储局部变量表、操作数栈、动态链接、方法出口等信息。每一个方法才调用直至执行完成的过程，就对应着一个Stack Frame在VM Stack中入栈到出栈的过程。

我们平时所指的Java内存区的“栈”应该是指VM Stack中局部变量表部分。局部变量表存放了编译期可知的各种基本数据类型、对象引用（reference）等。

### 本地方法栈(Native Method Stack)

有时候在看source code会发现native关键字的方法，但不能看到其source code，说明它是原生的，有可能这个方法是用C/C++语言实现，让JVM去调用，犹如操作系统的系统调用。Native Method Stack与VM Stack所发挥的作用是非常相似的。

[what is difference between Java Method And Native Method?
](https://stackoverflow.com/questions/18824798/what-is-difference-between-java-method-and-native-method)

### Java堆(Heap)

Heap的唯一目的就是存放对象实例，几乎所有的对象实例都在这里分配内存。

### 方法区(Method Area)

Method Area用于存储已被JVM加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。JVM规范把它描述为Heap的一个逻辑部分。HotSpot中，Method Area还有个别名叫“永久代”(Permanent Generation)，但Permanent Generation只是Method Area的一种实现，**在JDK7中的HotSpot，已经把原本永久代的字符串常量池移出**。JDK8元空间替代了永久代，永久代被移除，也可以理解为元空间是方法区的一种实现。

另外一个比较重要的是运行时常量池(Runtime Constant Pool)，为Method Area的一部分，这个和我这里下面要讨论的相关。Runtime Constant Pool相对于Class文件常量池(静态常量池)的重要特征是动态性，Java并不要求常量一定只有在编译期才能产生，也就是并非预置入Class文件中常量池的内容才能进入方法区运行时常量池，运行期间也可能将新的常量池中，这种特性被开发人员利用得比较多的便是String类的intern()方法。

Ref:

[深入理解Java虚拟机(第2版)](https://book.douban.com/subject/24722612/)

## String pool


