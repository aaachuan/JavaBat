# Immutable

## Overview
```
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];

    /** Cache the hash code for the string */
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;
    ...
 }
```
String被声明为final，所以它不可被别的类继承。（Integer等包装类也是）

同时，value[]数组也被声明为final，它初始化后也不能再引用别的数组，而String内部也没有改变value[]数组的方法，所以可以保证String是不可变的。

> 在早期的JVM实现版本中，被final修饰的方法会被转为内嵌调用以提升执行效率。而从Java SE5/6开始，就渐渐摈弃这种方式了。因此在现在的Java SE版本中，不需要考虑用final去提升方法调用效率。只有在确定不想让该方法被覆盖时，才将方法设置为final。

上面的代码是JDK8的，实际上可以看出String是用char数组来保存字符串。但是在JDK9，String改用byte数组来存储，并使用`coder`来标示编码。
```
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {

    /**
     * The value is used for character storage.
     *
     * @implNote This field is trusted by the VM, and is a subject to
     * constant folding if String instance is constant. Overwriting this
     * field after construction will cause problems.
     *
     * Additionally, it is marked with {@link Stable} to trust the contents
     * of the array. No other facility in JDK provides this functionality (yet).
     * {@link Stable} is safe here, because value is never null.
     */
    @Stable
    private final byte[] value;

    /**
     * The identifier of the encoding used to encode the bytes in
     * {@code value}. The supported values in this implementation are
     *
     * LATIN1
     * UTF16
     *
     * @implNote This field is trusted by the VM, and is a subject to
     * constant folding if String instance is constant. Overwriting this
     * field after construction will cause problems.
     */
    private final byte coder;

    /** Cache the hash code for the string */
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;
    ...
 }
```

## Immutable的好处

### String pool的需要
JVM为了提高性能和减少内存的开销，在实例化字符串的时候进行了一些优化：使用String pool常量池。如果一个对象已经被创建过，那么新的引用会指向String pool的对象。由于String的不可变性可以十分肯定String pool中一定不存在两个相同的字符串。
``` 
                                               _________________
              __________                      |      HEAP       |
             |          |                     |                 |
             | String1  | ------------------> |    _________    |
             |__________|              -----> |   |  "abc"  |   |
           String Reference            |      |   |_________|   |
              __________               |      |                 |
             |          |              |      |   String Object |   
             | String2  | --------------      |                 |
             |__________|                     |                 |
                                              |_________________|
 
``` 
