# Abstract class and Interface

## Abstract class
包含抽象方法的类叫做抽象类。
- 如果一个类包含一个或多个抽象方法，那么类本身也必须限定为抽象的，否则，编译器会报错。
- 抽象类可以不包含抽象方法，但如果类中包含抽象方法，就必须将该类声明为抽象类。
- 抽象方法必须为public或者protected（因为如果为private，则不能被子类继承，子类便无法实现该方法）。
- 抽象类不能使用final声明，因为抽象类必须有子类，而final定义的类不能有子类。

抽象类不能被实例化，只能被继承。
```
package com.javabasic.abstractclass;

public abstract class AbstractClassExample {

	protected int x;
	private int y;
	
	public abstract void func1();
	
	public void func2() {
		System.out.println("func2");
	}
}
```
```
package com.javabasic.abstractclass;

public class AbstractExtendClassExample extends AbstractClassExample {

	@Override
	public void func1() {
		System.out.println("func1");

	}
	
	public static void main(String[] args) {
		AbstractClassExample ac1 = new AbstractClassExample(); //编译报错
		ac1.func2();
	}

}
```
```
Exception in thread "main" java.lang.Error: Unresolved compilation problem: 
	Cannot instantiate the type AbstractClassExample

	at com.javabasic.abstractclass.AbstractExtendClassExample.main(AbstractExtendClassExample.java:12)

```
如此就能生效：
```
AbstractClassExample ac1 = new AbstractExtendClassExample();
```
## interface
接口是抽象类的延伸，在 Java 8 之前，它可以看成是一个完全抽象的类，也就是说它不能有任何的方法实现。

从 Java 8 开始，接口也可以拥有默认的方法实现，这是因为不支持默认方法的接口的维护成本太高了。在 Java 8 之前，如果一个接口想要添加新的方法，那么要修改所有实现了该接口的类，让它们都实现新增的方法。

接口的成员（字段 + 方法）默认都是 public 的，并且不允许定义为 private 或者 protected。从 Java 9 开始，允许将方法定义为 private，这样就能定义某些复用的代码又不会把方法暴露出去。

接口的字段默认都是 static 和 final 的。

可以选择显式地声明接口中的方法为 public，但是即使不这么做，它们也是 public 的。所以当实现一个接口时，来自接口中的方法必须被定义为 public。否则，它们只有包访问权限，这样在继承时，它们的可访问权限就被降低了，这是 Java 编译器所不允许的。
```
public interface InterfaceExample {

    void func1();

    default void func2(){
        System.out.println("func2");
    }

    int x = 123;
    // int y;               // Variable 'y' might not have been initialized
    public int z = 0;       // Modifier 'public' is redundant for interface fields
    // private int k = 0;   // Modifier 'private' not allowed here
    // protected int l = 0; // Modifier 'protected' not allowed here
    // private void fun3(); // Modifier 'private' not allowed here
}
```
```
public class InterfaceImplementExample implements InterfaceExample {
    @Override
    public void func1() {
        System.out.println("func1");
    }
}
```
```
// InterfaceExample ie1 = new InterfaceExample(); // 'InterfaceExample' is abstract; cannot be instantiated
InterfaceExample ie2 = new InterfaceImplementExample();
ie2.func1();
System.out.println(InterfaceExample.x);
```
