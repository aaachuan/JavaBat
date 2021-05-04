# Key Word#final
## final数据
告诉编译器有一块数据是恒定不变的，即常量：
- 一个永不改变的编译时常量
- 一个在运行时初始化就不会改变的值

一个被 static 和 final 同时修饰的属性只会占用一段不能改变的存储空间。

final在用于修饰基本类型和引用类型时有区别：
- 对于基本类型，final 使数值不变；
- 对于引用类型，final 使引用不变，也就不能引用其它对象，但是被引用的对象本身是可以修改的。
```
final int x = 1;
// x = 2;  // cannot assign value to final variable 'x'
final A y = new A();
y.a = 1;
```
Onjava8的例子： 
```
// reuse/FinalData.java
// The effect of final on fields
import java.util.*;

class Value {
    int i; // package access
    
    Value(int i) {
        this.i = i;
    }
}

public class FinalData {
    private static Random rand = new Random(47);
    private String id;
    
    public FinalData(String id) {
        this.id = id;
    }
    // Can be compile-time constants:
    private final int valueOne = 9;
    private static final int VALUE_TWO = 99;
    // Typical public constant:
    public static final int VALUE_THREE = 39;
    // Cannot be compile-time constants:
    private final int i4 = rand.nextInt(20);
    static final int INT_5 = rand.nextInt(20);
    private Value v1 = new Value(11);
    private final Value v2 = new Value(22);
    private static final Value VAL_3 = new Value(33);
    // Arrays:
    private final int[] a = {1, 2, 3, 4, 5, 6};
    
    @Override
    public String toString() {
        return id + ": " + "i4 = " + i4 + ", INT_5 = " + INT_5;
    }
    
    public static void main(String[] args) {
        FinalData fd1 = new FinalData("fd1");
        //- fd1.valueOne++; // Error: can't change value
        fd1.v2.i++; // Object isn't constant
        fd1.v1 = new Value(9); // OK -- not final
        for (int i = 0; i < fd1.a.length; i++) {
            fd1.a[i]++; // Object isn't constant
        }
        //- fd1.v2 = new Value(0); // Error: Can't
        //- fd1.VAL_3 = new Value(1); // change reference
        //- fd1.a = new int[3];
        System.out.println(fd1);
        System.out.println("Creating new FinalData");
        FinalData fd2 = new FinalData("fd2");
        System.out.println(fd1);
        System.out.println(fd2);
    }
}
```
Output:
```
fd1: i4 = 15, INT_5 = 18
Creating new FinalData
fd1: i4 = 15, INT_5 = 18
fd2: i4 = 13, INT_5 = 18
```
按照惯例，带有恒定初始值的 final static 基本变量（即编译时常量）命名全部使用大写，单词之间用下划线分隔。（源于 C 语言中定义常量的方式。）

注意到 fd1 和 fd2 的 i4 值不同，但 INT_5 的值并没有因为创建了第二个 FinalData 对象而改变，这是因为它是 static 的，在加载时已经被初始化，并不是每次创建新对象时都初始化。
# final参数
在参数列表中，将参数声明为 final 意味着在方法中不能改变参数指向的对象或基本变量。这个特性主要用于传递数据给匿名内部类。
# final方法
声明方法不能被子类重写。

private 方法隐式地被指定为 final，如果在子类中定义的方法和基类中的一个 private 方法签名相同，此时子类的方法不是重写基类方法，而是在子类中定义了一个新的方法。

# final类
声明类不允许被继承。之所以这么做，是因为类的设计就是永远不需要改动，或者是出于安全考虑不希望它有子类。
