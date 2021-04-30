# Object method

## Object source code
```
public class Object {

    private static native void registerNatives();
    static {
        registerNatives();
    }
    
    public final native Class<?> getClass();
    
    public native int hashCode();
    
    public boolean equals(Object obj) {
        return (this == obj);
    }
    
    protected native Object clone() throws CloneNotSupportedException;
    
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
    
    public final native void notify();
    
    public final native void notifyAll();
    
    public final native void wait(long timeout) throws InterruptedException;

    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }
    
    public final void wait() throws InterruptedException {
        wait(0);
    }
    
    protected void finalize() throws Throwable { }
```
Object类主要是为了扩展，非final方法都有一些通用约定，设计成被覆盖(equals、hashCode、toString、clone和finalize)。

## equals()
当你创建一个类的时候，它自动继承自 Objcet 类。如果你不覆写 equals() ，你将会获得 Objcet 对象的 equals() 函数。默认情况下，这个函数会比较对象的引用。
```
package com.javabasic.object;

public class EqualsTest {

	public static void main(String[] args) {
		Integer x = new Integer(1);
		Integer y = new Integer(1);
		
		System.out.println(x.equals(y)); //true 
		System.out.println(x == y); //false
		
		
		EqualsTest u = new EqualsTest();
		EqualsTest v = new EqualsTest();
		System.out.println(u.equals(v)); //false

	}

}
```
`Integer`重写了`equals()`方法：
```
 /**
     * Compares this object to the specified object.  The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is an {@code Integer} object that
     * contains the same {@code int} value as this object.
     *
     * @param   obj   the object to compare with.
     * @return  {@code true} if the objects are the same;
     *          {@code false} otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return value == ((Integer)obj).intValue();
        }
        return false;
    }
```
equals()方法的特性：
- 反身性
```
x.equals(x); // true
```
- 对称性
```
x.equals(y) == y.equals(x); // true
```
- 传递性
```
if (x.equals(y) && y.equals(z))
    x.equals(z); // true;
```
- 一致性
```
x.equals(y) == x.equals(y); // true--多次调用equals()方法结果不变
```
- null比较
```
x.equals(null); // false;
```
a simple example:
```
package com.javabasic.object;

public class EqualExample {

	private int x;
	private int y;
	private int z;
	
	public EqualExample(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object o) {
		// 检查是否为同一个对象的引用
		if (this == o) return true;
		// 检查是否是同一个类型
		if (o == null || getClass() != o.getClass()) return false;
		// 将Object对象进行转型
		EqualExample that = (EqualExample) o;
	    // 判断每个关键域 
	    if (x != that.x) return false;
	    if (y != that.y) return false;
	    return z == that.z;
	}
	
	
	public static void main(String[] args) {
		
		EqualExample ee = new EqualExample(1, 2, 3);
		
		//EqualExample newee = ee;
		
		//EqualExample newee = null;
		
		//Object newee = ee;
		//System.out.println(newee.getClass().getName()); //com.javabasic.object.EqualExample
		EqualExample newee = new EqualExample(1, 2, 3);
		System.out.println(ee.equals(newee));
		
		
	}

}

```
Java 7提供了更为合适的对象工具类Objects(java.util.Objects)
```
This class consists of static utility methods for operating on objects. These utilities include null-safe or null-tolerant methods for computing the hash code of an object, returning a string for an object, and comparing two objects.
Since:
1.7
```
例如空值判断可以由
```
if (obj!=null){
    // 判断不等于空
}
if (obj==null){
    // 判断等于空
}
```
变成
```
if (Objects.nonNull(obj)){
    // 判断不等于空
}
if (Objects.isNull(obj)){
    // 判断等于空
}
```
还有
```
public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }
```
或者重载方法自定义空值报错
```
       public Foo(Bar bar, Baz baz) {
           this.bar = Objects.requireNonNull(bar, "bar must not be null");
           this.baz = Objects.requireNonNull(baz, "baz must not be null");
       }
       
```
话说回obj.equals(other)，如果obj为null也会抛出空指针异常，所以
```
public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
 }
```
但是不建议使用该方法判别基本数据类型，实际上也不应该:
```
Objects.equals(2,2L) //false
```
所以根据`Objects.equals(Object a,Object b)`方法，我们可以写出
```
	@Override
	public boolean equals(Object o) {
		return o instanceof EqualExample &&
		Objects.equals(x,((EqualExample)o).x) &&
		Objects.equals(y,((EqualExample)o).y) &&
		Objects.equals(z,((EqualExample)o).z);
	}
```
## hashCode()
