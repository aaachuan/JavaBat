# Abstract class and Interface

## Abstract class
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
