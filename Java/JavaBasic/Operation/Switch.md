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

