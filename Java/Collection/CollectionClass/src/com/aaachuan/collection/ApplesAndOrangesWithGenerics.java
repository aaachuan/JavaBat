package com.aaachuan.collection;

import java.util.ArrayList;

public class ApplesAndOrangesWithGenerics {
    public static void main(String[] args) {
        ArrayList<Apple> apple = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            apple.add(new Apple());
        }
        for (Apple o: apple) {
            System.out.println(o.getId());
        }
    }
}
