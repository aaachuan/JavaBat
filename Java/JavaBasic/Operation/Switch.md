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
反编译：
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
