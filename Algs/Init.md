```
package com.aaachuan.algs4.example;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BinarySearch {
	
	public static int rank(int key, int[] a) {
		int lo = 0;
		int hi = a.length - 1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			if (key < a[mid]) hi = mid - 1;
			else if (key > a[mid]) lo = mid + 1;
			else return mid;
		}
		
		return -1;
	}

	public static void main(String[] args) {
		int[] whitelist = In.readInts(args[0]);
		
		Arrays.sort(whitelist);
		
		while (!StdIn.isEmpty()) {
			int key = StdIn.readInt();
			if (rank(key, whitelist) == -1)
				StdOut.println(key);
		}

	}

}

```
```
Note that the I/O libraries from stdlib.jar are now contained in algs4.jar, so you no longer need stdlib.jar.（请注意，stdlib.jar的I / O库现在包含在algs4.jar中，因此您不再需要stdlib.jar）
```
stdlib.jar的包名是default，所以编码时包都是导入不进来的。。。

[关于eclipse的重定向输入文件](https://stackoverflow.com/questions/31234646/redirect-stdin-from-a-file-in-eclipse-with-run-configuration/31285746)

