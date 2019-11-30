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

String对象创建的方式有两种，一种是字面量形式，形如`String s = "ac"`，另一种是使用new标准的构造对象，`String s = new String("ac")`。两种方式其中内存占用和性能是有差别的，JVM为减少重复创建对象，引入了String pool。

### 字面量创建

```
String s1 = "ac";
String s2 = "ac";
System.out.println(s1 == s2); // true
```
一开始`String s1 = "ac"`，JVM首先在String pool中找，发现不存在，会创建这个字符串对象，将刚创建的字符串reference放入String pool，并将引用返回给变量s1。紧接着，同样`String s2 = "ac"`的时候，JVM继续查找String pool，发现"ac"字符串对象存在，直接将"ac"的reference返回给s2.这里不会重新创建字符串对象。

### new关键字创建
```
String s3 = new String("bb");
String s4 = new String("bb");
System.out.println(s3 == s4); //false
```
当使用new来构造字符串对象的时候，不管String pool中有没有相同内容对象的reference，新的字符串对象都会创建。输出结果为false表示两个变量指向的是不同的对象。

### intern()
```
    /**
     * Returns a canonical representation for the string object.
     * <p>
     * A pool of strings, initially empty, is maintained privately by the
     * class {@code String}.
     * <p>
     * When the intern method is invoked, if the pool already contains a
     * string equal to this {@code String} object as determined by
     * the {@link #equals(Object)} method, then the string from the pool is
     * returned. Otherwise, this {@code String} object is added to the
     * pool and a reference to this {@code String} object is returned.
     * <p>
     * It follows that for any two strings {@code s} and {@code t},
     * {@code s.intern() == t.intern()} is {@code true}
     * if and only if {@code s.equals(t)} is {@code true}.
     * <p>
     * All literal strings and string-valued constant expressions are
     * interned. String literals are defined in section 3.10.5 of the
     * <cite>The Java&trade; Language Specification</cite>.
     *
     * @return  a string that has the same contents as this string, but is
     *          guaranteed to be from a pool of unique strings.
     */
    public native String intern();
```
```
String s4 = s3.intern();
System.out.println(s1 == s4); // true
System.out.println(s3 == s4); // false
```
调用intern后，首先检查字符串常量池中是否有该对象的reference，如果存在，则将这个reference返回给变量，否则将reference加入并返回给变量。

### key point
String pool实现的条件就是String对象是不可变的，安全保证多个变量共享一个对象。

还有一个比较重要的是String pool中存放的是reference还是Object？留坑。

```
String s1 = "ac";
String s2 = "a" + "c";
String s3 = "a" + new String("c");
		
System.out.println(s1 == s2); // true
System.out.println(s1 == s3); // false
```
`javap -c StringTest`反编译字节码文件：
```
Compiled from "StringTest.java"
public class StringTest {
  public StringTest();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":
()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: ldc           #2                  // String ac
       2: astore_1
       3: ldc           #2                  // String ac
       5: astore_2
       6: new           #3                  // class java/lang/StringBuilder
       9: dup
      10: invokespecial #4                  // Method java/lang/StringBuilder."<
init>":()V
      13: ldc           #5                  // String a
      15: invokevirtual #6                  // Method java/lang/StringBuilder.ap
pend:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      18: new           #7                  // class java/lang/String
      21: dup
      22: ldc           #8                  // String c
      24: invokespecial #9                  // Method java/lang/String."<init>":
(Ljava/lang/String;)V
      27: invokevirtual #6                  // Method java/lang/StringBuilder.ap
pend:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      30: invokevirtual #10                 // Method java/lang/StringBuilder.to
String:()Ljava/lang/String;
      33: astore_3
      34: getstatic     #11                 // Field java/lang/System.out:Ljava/
io/PrintStream;
      37: aload_1
      38: aload_2
      39: if_acmpne     46
      42: iconst_1
      43: goto          47
      46: iconst_0
      47: invokevirtual #12                 // Method java/io/PrintStream.printl
n:(Z)V
      50: getstatic     #11                 // Field java/lang/System.out:Ljava/
io/PrintStream;
      53: aload_1
      54: aload_3
      55: if_acmpne     62
      58: iconst_1
      59: goto          63
      62: iconst_0
      63: invokevirtual #12                 // Method java/io/PrintStream.printl
n:(Z)V
      66: return
}
```
照道理，s3的过程应该是String s3 = new StringBuilder("a").append(new String("c")).toString();但是这边反编译却是有两次append...

渣渣

Ref：

[What is the Java string pool and how is “s” different from new String(“s”)?](https://stackoverflow.com/questions/2486191/what-is-the-java-string-pool-and-how-is-s-different-from-new-strings)

[深入解析String#intern](https://tech.meituan.com/2014/03/06/in-depth-understanding-string-intern.html)
