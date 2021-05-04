# 泛型
## 泛型引入
```
public class Box<T> {
    // T stands for "Type"
    private T t;
    public Box(T t) {
        set(t);
    }
    public void set(T t) { this.t = t; }
    public T get() { return t; }
}
```
泛型只在编译阶段有效，泛型类型在逻辑上看以看成是多个不同的类型，实际上都是相同的基本类型。
```
import java.util.ArrayList;
import java.util.List;

public class GenericTest {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<String>();
        List<Integer> integerList = new ArrayList<Integer>();

        Class classStringList = stringList.getClass();
        Class classIntegerList = integerList.getClass();

        if(classStringList.equals(classIntegerList)) {
            System.out.println("泛型类型相同");
        }
    }

}
```
Java中的泛型，只在编译阶段有效。在编译过程中，正确检验泛型结果后，会将泛型的相关信息擦除，并且在对象进入和离开方法的边界处添加类型检查和类型转换的方法。

也就是说，泛型信息不会进入到运行时阶段。
## 泛型类
```
public class BoxTest {
    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<Integer>(123456);
        Box<String> stringBox = new Box<String>("key_value");

        System.out.println(integerBox.get());
        System.out.println(stringBox.get());
    }
}
```
- 泛型的类型参数只能是类类型，不能是基本类型。
- 不能对确切的泛型类型使用instanceof操作。
```
        if (integerBox instanceof Box<Integer>) { //Illegal generic type for instanceof
            
        }
        
        if (integerBox instanceof Box) {
            System.out.println("instanceof Box"); //Ouput
        }
```
## 泛型接口
泛型接口与泛型类的定义及使用基本相同。泛型接口常被用在各种类的生产器中。
```
//泛型接口
public interface BoxGeneric<T> {
    public T next();
}
```
未传入泛型实参时，与泛型类的定义相同，在声明类的时候，需将泛型的声明也一起加到类中
```
public class BoxGenericImpl<T> implements BoxGeneric<T> {
    @Override
    public T next() {
        return null;
    }
}
```
不允许
```
public class BoxGenericImpl implements BoxGeneric<T>
```
当实现泛型接口的类传入泛型实参时（以String为例）,BoxGeneric<T>中，可以为T传入多种实参，形成多种类型的BoxGeneric接口。这时，BoxGeneric<T>和public T next();中的T要替换。
```
public class BoxGenericImpl implements BoxGeneric<String> {
    private String[] fruits = new String[]{"Apple", "Banana", "Pear"};
    @Override
    public String next() {
        return fruits[new Random().nextInt(3)];
    }
}
```
## 泛型通配符、
同一种泛型可以对应多个版本，不同版本的泛型类实例是不兼容的，即使泛型参数有父子类关系，例如Box<Number>和Box<Integer>：
```
    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<Integer>(123);
        Box<Number> numberBox = new Box<Number>(456);
        showKeyValue(integerBox);
    }
    
    public static void showKeyValue(Box<Number> obj) {
        System.out.println("key value is " + obj.get());
    }    
```
编译会报错，即使```public final class Integer extends Number implements Comparable<Integer>```

为解决这种情况，泛型通配符应运而生。
```
    public static void showKeyValue(Box<?> obj) {
        System.out.println("key value is " + obj.get());
    }    
```
## 泛型方法
泛型类，是在实例化类的时候指明泛型的具体类型；泛型方法，是在调用方法的时候指明泛型的具体类型。
