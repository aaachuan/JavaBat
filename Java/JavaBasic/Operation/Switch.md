# Switch
## Switch引入

switch的case语句可以处理int，short，byte，char类型的值，但是不能处理long类型。因为short，byte，char都会转换成int进行处理。
```
package com.javabasic.operation;

public class SwitchTest {

	public static void main(String[] args) {
		int i = 2;
		switch (i) {
		case 1:
			System.out.println(1);
			break;
		case 2:
			System.out.println(2);
			//break;
		default:
			System.out.println("default");
			break;
		}

	}

}
```

In Java SE 7 and later, you can use a String object in the switch statement's expression.

[The switch Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html)

## String的Switch细节
Java 7开始支持String的switch，通过反编译可以看出是通过hashCode()和equals()方法处理判断的。hashCode()的返回值为int，这点可以和上面的case不能处理long类型相互联系起来，equals()方法的必要性在于hashcode有可能发生碰撞。
```
public class SwitchTest {
    public static void main(String[] args) {
        String mode = args[0];

        switch (mode) {
            case "ACTIVE":
                System.out.println("Application is running on Active mode");
                break;
            case "PASSIVE":
                System.out.println("Application is running on Passive mode");
            case "SAFE":
                System.out.println("Application is running on Safe mode");
        }

    }
}
```
自己电脑的反编译结果：
```
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
public class SwitchTest {
    public SwitchTest() {
    }

    public static void main(String[] args) {
        String mode = args[0];
        byte var3 = -1;
        switch(mode.hashCode()) {
        case -74056953:
            if (mode.equals("PASSIVE")) {
                var3 = 1;
            }
            break;
        case 2537357:
            if (mode.equals("SAFE")) {
                var3 = 2;
            }
            break;
        case 1925346054:
            if (mode.equals("ACTIVE")) {
                var3 = 0;
            }
        }

        switch(var3) {
        case 0:
            System.out.println("Application is running on Active mode");
            break;
        case 1:
            System.out.println("Application is running on Passive mode");
        case 2:
            System.out.println("Application is running on Safe mode");
        }

    }
}
```
另外，枚举使用的是ordinal()方法确定序号，这个可以从字节码看出：
```
       4: invokevirtual #10                 // Method com/example/core/service/domain/enums/StatusEnum.ordinal:()I
```
## tableswitch和lookupswitch
case密集和case稀疏的两个例子：
```
public class SwitchTest {
    public void switch1(int i) {
        int j;
        switch (i) {
            case 1:
                j = 1;
                break;
            case 2:
                j = 2;
                break;
            case 5:
                j = 3;
                break;
            default:
                break;
        }
    }

    public void switch2(int i) {
        int j;
        switch (i) {
            case 1:
                j = 1;
                break;
            case 5:
                j = 2;
                break;
            case 10000:
                j = 3;
                break;
            default:
                break;
        }
    }
}
```
查看字节码：
```
public class com.aaachuan.swit.SwitchTest {
  public com.aaachuan.swit.SwitchTest();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public void switch1(int);
    Code:
       0: iload_1
       1: tableswitch   { // 1 to 5
                     1: 36
                     2: 41
                     3: 51
                     4: 51
                     5: 46
               default: 51
          }
      36: iconst_1
      37: istore_2
      38: goto          51
      41: iconst_2
      42: istore_2
      43: goto          51
      46: iconst_3
      47: istore_2
      48: goto          51
      51: return

  public void switch2(int);
    Code:
       0: iload_1
       1: lookupswitch  { // 3
                     1: 36
                     5: 41
                 10000: 46
               default: 51
          }
      36: iconst_1
      37: istore_2
      38: goto          51
      41: iconst_2
      42: istore_2
      43: goto          51
      46: iconst_3
      47: istore_2
      48: goto          51
      51: return
}
```
当jvm遇到tableswitch指令时，它会检测switch(key)中的key值是否在low~high之间，如果不是，直接执行default部分，如果在这个范围之内，它使用key-low这个项指定的地点跳转。可见，tableswitch的效率是非常高的。通过这种方式可以获得O(1)的时间复杂度。

当case中的值不连续时，编译成lookupswitch，解释执行时需要从头到尾遍历找到case对应的代码行。因为case对应的值不是连续的，如果仍然用表来保存case对应的行号，会浪费大量空间。另外，当jvm遇到lookupswitch指令时，必须依次检测每一个项目看是否和switch(key) 中的key匹配，如果遇到匹配的直接跳转，如果遇到比key值大的，执行default,因为case是按照升序排序。另外一点，升序排列也允许jvm实现这条指令时进行优化，比如采用二分搜索的方式取代线性扫描等。此时查找最好的性能是O(log n)。

在[langtools/src/share/classes/com/sun/tools/javac/jvm/Gen.java](http://hg.openjdk.java.net/jdk8/jdk8/langtools/file/30db5e0aaf83/src/share/classes/com/sun/tools/javac/jvm/Gen.java#l1153)看到以下代码,之后可以看看的：
```
            // Determine whether to issue a tableswitch or a lookupswitch
            // instruction.
            long table_space_cost = 4 + ((long) hi - lo + 1); // words
            long table_time_cost = 3; // comparisons
            long lookup_space_cost = 3 + 2 * (long) nlabels;
            long lookup_time_cost = nlabels;
            int opcode =
                nlabels > 0 &&
                table_space_cost + 3 * table_time_cost <=
                lookup_space_cost + 3 * lookup_time_cost
                ?
                tableswitch : lookupswitch;
```
## switch还是if...else?
CPU分支预测...
后面验证完再写
