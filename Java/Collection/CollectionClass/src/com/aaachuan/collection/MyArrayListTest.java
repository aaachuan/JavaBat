package com.aaachuan.collection;

import java.util.LinkedList;

public class MyArrayListTest {
    public static void main(String[] args) {
        MyArrayList<String> myArrayList = new MyArrayList<>();
        myArrayList.add("a");
        myArrayList.add("b");
        myArrayList.add("c");
        System.out.println(myArrayList);
        System.out.println(myArrayList.getSize());
        myArrayList.remove(1);
        System.out.println(myArrayList);
        System.out.println(myArrayList.getSize());
        myArrayList.add("d");
        myArrayList.add("e");
        myArrayList.set(0,"x");
        System.out.println(myArrayList);
        System.out.println(myArrayList.getSize());
        System.out.println(myArrayList.get(3));


    }
}
