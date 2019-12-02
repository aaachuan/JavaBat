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
```
反编译：
```
String mode = args[0];
		switch (mode.hashCode()) {
			case -74056953 :
				if (!mode.equals("PASSIVE")) {
					return;
				}

				System.out.println("Application is running on Passive mode");
				break;
			case 2537357 :
				if (!mode.equals("SAFE")) {
					return;
				}
				break;
			case 1925346054 :
				if (mode.equals("ACTIVE")) {
					System.out.println("Application is running on Active mode");
				}

				return;
			default :
				return;
		}

		System.out.println("Application is running on Safe mode");
```
