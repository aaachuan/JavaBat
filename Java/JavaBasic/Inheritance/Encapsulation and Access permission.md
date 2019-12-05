# Encapsulation and Access permission

一个设计良好的模块会隐藏所有的实现细节，把它的API与实现清晰地隔离开来。然后，模块之间只通过它们的API进行通信，一个模块不需要知道其他模块的内部工作情况。这个概念被称为信息隐藏（information hiding）或封装（encapsulation），是软件设计的基本原则之一。

Java中有三个访问权限修饰符：private,protected和public，如果不指明的话，则是默认访问级别，即包级可见。

当一开始设计一个类的公有API时，接下去应该把所有其他的成员都变成私有的。如果是公有的，就失去对这些成员修改行为的控制，客户端可以随意修改。
```
public class AccessExample {
    public String id;
}
```
对于上面的代码，如果想要使用int存储id字段，就需要修改所有的客户端代码。可以使用公有的 getter 和 setter 方法来替换公有字段，这样的话就可以控制对字段的修改行为。这样的好处是在改变内部实现的时候，除了该类的方法之外，不会影响其他地方的代码。
```
public class AccessExample {

    private int id;

    public String getId() {
        return id + "";
    }

    public void setId(String id) {
        this.id = Integer.valueOf(id);
    }
}
```
值得注意的是，如果子类的方法重写了父类的方法，那么子类中该方法的访问级别不允许低于父类的访问级别。这是为了确保可以使用父类实例的地方都可以使用子类实例，也就是确保满足里氏替换原则。

里氏替换原则：
- If for each object o1 of type S there is an object o2 of type T such that for all programs P defined in terms of T,the behavior of P is unchanged when o1 is substituted for o2 then S is a subtype of T.
- Functions that use pointers or references to base classes must be able to useobjects of derived classes without knowing it.

通俗来讲就是，子类可以扩展父类的功能，但不能改变父类原有的功能。即：
- 子类可以实现父类的抽象方法，但不能覆盖父类的非抽象方法。
- 子类中可以增加自己特有的方法。
- 当子类的方法重载父类的方法时，方法的前置条件（即方法的形参）要比父类方法的输入参数更宽松。
- 当子类的方法实现父类的抽象方法时，方法的后置条件（即方法的返回值）要比父类更严格。

几个访问权限修饰符中，`protected`无疑是最特殊的。`protected`的真正内涵有两点：
- 相同package包的情况下，基类的protected成员是包内可见的，并且对子类可见；
- 不同package包的情况下，若子类与基类不在同一包中，那么在子类中，子类实例可以访问其从基类继承而来的protected方法，而不能访问基类实例的protected方法。

```
package com.javabasic.inherit.abc;

public class Father {

	protected void hi() {
		System.out.println("I am your father");
	}
}

```

```
package com.javabasic.inherit.xyz;

import com.javabasic.inherit.abc.Father;


public class Main {

	public static void main(String[] args) {
		Son s = new Son();
		s.hello();
	}

}


class Son extends Father {
	void hello() {
		Father father = new Father();
	//	father.hi();
		this.hi();
		super.hi();
		
		Son son = new Son();
		son.hi();
	}
}
```
因为父子类不在同一个包下，Son类里面不能访问父类实例father的protected方法，但用Father继承而来的Son实例就可以访问。

还有一个值得注意的是，不同包下，在子类中不能通过另一个子类引用访问共同基类的 protected 方法。
