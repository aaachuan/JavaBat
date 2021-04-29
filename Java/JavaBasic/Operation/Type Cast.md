# Type Cast
## 基本数据类型
java定义若干使用于表达式的类型提升规则，如所有的byte型. short型和char型将被提升到int型(例外: final修饰的short, char变量相加后不会被自动提升)
```
	short s1 = 1;
	//	s1 = (s1 + 1);
```
使用 += 或者 ++ 运算符会执行隐式类型转换
```
s1 += 1;
s1++;
```
上面的语句相当于将 s1 + 1 的计算结果进行了向下转型:
```
s1 = (short) (s1 + 1);
```
当i,j分属于不同数据类型时，`i = i + j`与`i += j`是不一样的，后者会自动提升，编译成功。

[Why don't Java's +=, -=, *=, /= compound assignment operators require casting?](https://stackoverflow.com/questions/8710619/why-dont-javas-compound-assignment-operators-require-casting)

## 封装对象类型
这部分会涉及到向上转型、向下转型和多态等主题。根据自己的理解，基础数据类型没有类型的继承关系，所以关于向下转型导致降低数据类型精度的说法，向x转型置于这里是不认可的。
### 向上转型
向上转型指的是子类到父类的转换，这个行为由编译器隐式执行。

On Java 8的例子：
```
// reuse/Wind.java
// Inheritance & upcasting
class Instrument {
    public void play() {}
    
    static void tune(Instrument i) {
        // ...
        i.play();
    }
}

// Wind objects are instruments
// because they have the same interface:
public class Wind extends Instrument {
    public static void main(String[] args) {
        Wind flute = new Wind();
        Instrument.tune(flute); // Upcasting
    }
}
```
调用时，tune()方法参数中的`Wind`类(更具体的类)提升成`Instrument`类(更一般的类)，转型期间类只可能失去方法，不会增加方法，所以向上转型是安全的。多态理解起来就是一种为实现更好的类型容纳而做的抽象(不知道这么理解对不对...)。

说到多态很容易联想到`前期绑定`和`动态绑定`。绑定只方法调用和方法主体的关联。绑定发生在程序运行前称为前期绑定，所谓的面向过程语言不需选择而默认的就是这种绑定方式，如C语言。后期绑定即在运行时判断对象的类型二调用类型的方法。即编译器仍不知道对象的类型，但方法调用机制能找到正确的方法体并调用。这里的关键就是对象中一定存在某种类型信息。
```
Java 中除了 static 和 final 方法（private 方法也是隐式的 final）外，其他所有方法都是后期绑定。
为什么将一个对象指明为 final ？正如前一章所述，它可以防止方法被重写。但更重要的一点可能是，它有效地”关闭了“动态绑定，或者说告诉编译器不需要对其进行动态绑定。这可以让编译器为 final 方法生成更高效的代码。然而，大部分情况下这样做不会对程序的整体性能带来什么改变，因此最好是为了设计使用 final，而不是为了提升性能而使用。
```
当然，向上转型到接口更常见。
### 向下转型
即父类到子类的转换:(Sub)Super，这种情况一般发生在父类为获得特定于子类的成员访问权。在转换的过程中可以使用instanceof运算符来检查对象是否属于特定类型。如果真实对象与向下转型的类型不匹配，运行时会抛出ClassCastException。

强制转换的另一种方法是使用Class方法强制转换对象的方法：
```
public void test() {
    Animal animal = new Cat();
    if (Cat.class.isInstance(animal)) {
        Cat cat = Cat.class.cast(animal);
        cat.meow();
    }
}
```
通常使用具有泛型类型的cast()和isInstance()方法。
### 动态转换
因为强制转换的鸡肋，泛型便出来了，泛型使得编译器有足够的信息。类型在编译类型是未知的，我们将称之为动态转换。
```
Object obj; // may be an integer
if (Integer.class.isInstance(obj)) {
	Integer objAsInt = Integer.class.cast(obj);
	// do something with 'objAsInt'
}
```
```
Object obj; // may be an integer
Class<T> type = // may be Integer.class
if (type.isInstance(obj)) {
	T objAsType = type.cast(obj);
	// do something with 'objAsType'
}
```
Java 8
```
Optional<?> obj; // may contain an Integer
Optional<Integer> objAsInt = obj
		.filter(Integer.class::isInstance)
		.map(Integer.class::cast);
```
