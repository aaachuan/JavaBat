# IntegerCache

首先比较Integer的两种赋值变量创建对象的形式：
```
    Integer x = new Integer(123);
    Integer y = new Integer(123);
    System.out.println("x==y?" + (x==y));    // false
		
    Integer z = Integer.valueOf(123);
    Integer k = Integer.valueOf(123);
    System.out.println("z==k?" + (z==k));   // true
```
new Integer(123) 与 Integer.valueOf(123) 的区别在于：
- new Integer(123) 每次都会新建一个对象；
- Integer.valueOf(123) 会使用缓存池中的对象，多次调用会取得同一个对象的引用。（当然，这和数值有一定的关联）

把数值稍微修改下：
```
    Integer x = new Integer(128);
		Integer y = new Integer(128);
		System.out.println("x==y?" + (x==y));    // false
		Integer z = Integer.valueOf(128);
		Integer k = Integer.valueOf(128);
		System.out.println("z==k?" + (z==k));   // false
```
两个都是false。

看下Integer.valueOf()源码：
```
public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
```
原来这个valueOf()是首先判断值是否在IntegerCache里面，如果有就直接返回IntegerCache的内容。

在Java中，会对-128到127的Integer对象进行缓存，IntegerCache的默认范围为-128~127.
```
 /**
     * Cache to support the object identity semantics of autoboxing for values between
     * -128 and 127 (inclusive) as required by JLS.
     *
     * The cache is initialized on first usage.  The size of the cache
     * may be controlled by the {@code -XX:AutoBoxCacheMax=<size>} option.
     * During VM initialization, java.lang.Integer.IntegerCache.high property
     * may be set and saved in the private system properties in the
     * sun.misc.VM class.
     */

    private static class IntegerCache {
        static final int low = -128;
        static final int high;
        static final Integer cache[];

        static {
            // high value may be configured by property
            int h = 127;
            String integerCacheHighPropValue =
                sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
            if (integerCacheHighPropValue != null) {
                try {
                    int i = parseInt(integerCacheHighPropValue);
                    i = Math.max(i, 127);
                    // Maximum array size is Integer.MAX_VALUE
                    h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
                } catch( NumberFormatException nfe) {
                    // If the property cannot be parsed into an int, ignore it.
                }
            }
            high = h;

            cache = new Integer[(high - low) + 1];
            int j = low;
            for(int k = 0; k < cache.length; k++)
                cache[k] = new Integer(j++);

            // range [-128, 127] must be interned (JLS7 5.1.7)
            assert IntegerCache.high >= 127;
        }
```
在 jdk 1.8 所有的数值类缓冲池中，Integer 的缓冲池 IntegerCache 很特殊，这个缓冲池的下界是 - 128，上界默认是 127，但是这个上界是可调的，在启动 jvm 的时候，通过 -XX:AutoBoxCacheMax=<size> 来指定这个缓冲池的大小，该选项在 JVM 初始化的时候会设定一个名为 java.lang.IntegerCache.high 系统属性，然后 IntegerCache 初始化的时候就会读取该系统属性来决定上界。

[StackOverflow : Differences between new Integer(123), Integer.valueOf(123) and just 123](https://stackoverflow.com/questions/9030817/differences-between-new-integer123-integer-valueof123-and-just-123)
```
    Integer u = new Integer(10);
    Integer v = 10;
    System.out.println("u==v?" + (u==v));    //false
```

valueOf方法会自动调用IntegerCache这个类,IntegerCache初始化后内存中就有Integer缓冲池cache[]了。所以new Integer(10)表示在堆内存中创建对象，而Integer v = 10;这句是自动装箱,得到的是Integer缓冲池中的对象,是这句代码return IntegerCache.cache[10 + 128],明显a和b的地址是不一样的,不是同一个对象。java使用该机制是为了达到最小化数据输入和输出的目的,这是一种优化措施,提高效率。
