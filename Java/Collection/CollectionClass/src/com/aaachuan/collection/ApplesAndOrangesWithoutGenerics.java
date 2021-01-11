package com.aaachuan.collection;

import java.util.ArrayList;

//https://lingcoder.github.io/OnJava8/#/book/12-Collections?id=%e6%b3%9b%e5%9e%8b%e5%92%8c%e7%b1%bb%e5%9e%8b%e5%ae%89%e5%85%a8%e7%9a%84%e9%9b%86%e5%90%88
public class ApplesAndOrangesWithoutGenerics {
    public static void main(String[] args) {
        ArrayList apple = new ArrayList();
        for (int i = 0; i < 3; i++) {
            apple.add(new Apple());
        }

        apple.add(new Orange());
        for (Object o :apple) {
            ((Apple)o).getId();
        }
    }
}

class Apple {
    private static long counter;
    private final long id = counter++;

    public long getId() {
        return id;
    }
}

class Orange {}

