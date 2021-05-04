# 泛型
## 泛型引入
```
public class Box<T> {
    // T stands for "Type"
    private T t;
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
Java中的泛型，只在编译阶段有效。在编译过程中，正确检验泛型结果后，会将泛型的相关信息擦除，并且在对象进入和离开方法的边界处添加类型检查和类型转换的方法。也就是说，泛型信息不会进入到运行时阶段。
- 泛型的类型参数只能是类类型，不能是基本类型。
- 不能对确切的泛型类型使用instanceof操作。
