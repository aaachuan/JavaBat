# Parameter Passing

java中的参数都是by-value传递的。
```
package com.javabasic.operation;

import java.awt.Point;

public class ParameterPassingTest {

	public static void main(String[] args) {
		
		Point p = new Point(0,0);
		
		int i = 10;
		
		System.out.println("Before modifyPoint " + "p =" + p + "and i =" + i);
		
		modifyPoint(p, i);
		
		System.out.println("After modifyPoint " + "p =" + p + "and i =" + i);

	}

	public static void modifyPoint(Point pt, int j) {
		
		pt.setLocation(5, 5);
		
		j = 15;
		
		System.out.println("During modifyPoint " + "pt =" + pt + "and j =" + j);
		
	}

}

```
Output:
```
Before modifyPoint p =java.awt.Point[x=0,y=0]and i =10
During modifyPoint pt =java.awt.Point[x=5,y=5]and j =15
After modifyPoint p =java.awt.Point[x=5,y=5]and i =10
```
modifyPoint改变了p的值，但是没有改变i的值。

在main()中，i被赋值为10，由于参数通过by-value方式传递，所以modifyPoint收到的是i的一个副本，然后将其修改为15返回，但是这并不影响main()内的i的原值。而p是一个Object reference，java以by-value的方式传递Object reference，传递的不是Point对象的副本，而是Point对象的reference的副本，所以p和pt指向同一个Point对象，修改返回就改变了p的对象内的值。

另一种情况将modifyPoint稍微修改下：
```
public static void modifyPoint(Point pt, int j) {
		
	//	pt.setLocation(5, 5);
		
		pt = new Point(5,5);
		
		j = 15;
		
		System.out.println("During modifyPoint " + "pt =" + pt + "and j =" + j);
		
	}
```
Output:
```
Before modifyPoint p =java.awt.Point[x=0,y=0]and i =10
During modifyPoint pt =java.awt.Point[x=5,y=5]and j =15
After modifyPoint p =java.awt.Point[x=0,y=0]and i =10

```
这次p的值也没有改变。因为这时p的Object reference的副本指向的是另外一个新创建的Object，在它改变其内容时对原来的另一方并没有影响。

如果不想在传递modifyPoint时改变Point对象，有两种策略：
- 对modifyPoint传递一个Point对象的clone
- 让Point对象成为immutable

又说到immutable，又很容量联想到String的immutable的线程安全性。线程安全问题的根本原因在于多个线程同时访问一个共享资源。设计对象为immutable的方法有：
- 类内部字段private和final修饰。
- 不提供set方法。
- 可以将set方法返回一个new对象。
- 通过构造器初始化所有成员变量，引用类型的成员变量必须进行深拷贝(deep copy)。
对于第三点，类似String类的replace()方法：
```
  public String replace(char oldChar, char newChar) {
        if (oldChar != newChar) {
            int len = value.length;
            int i = -1;
            char[] val = value; /* avoid getfield opcode */

            while (++i < len) {
                if (val[i] == oldChar) {
                    break;
                }
            }
            if (i < len) {
                char buf[] = new char[len];
                for (int j = 0; j < i; j++) {
                    buf[j] = val[j];
                }
                while (i < len) {
                    char c = val[i];
                    buf[i] = (c == oldChar) ? newChar : c;
                    i++;
                }
                return new String(buf, true);
            }
        }
        return this;
    }
```
深拷贝的例子：Guava包提供的ImmutableList是真正意义上的不可变集合，它实际上是对入参list进行了深拷贝。

Ref:

[Practical Java: Programming Language Guide](https://dl.acm.org/citation.cfm?id=518796)
