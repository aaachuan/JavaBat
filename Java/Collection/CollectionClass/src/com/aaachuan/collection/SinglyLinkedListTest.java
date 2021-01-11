package com.aaachuan.collection;

import java.util.LinkedList;

public class SinglyLinkedListTest {
    public static void main(String[] args) {
        SinglyLinkedList<String> singlyLinkedList = new SinglyLinkedList<>();
        singlyLinkedList.add("1");
        singlyLinkedList.add("2");
        singlyLinkedList.add("3");
        singlyLinkedList.add("4");
        System.out.println(singlyLinkedList);
       //singlyLinkedList.removeLast();
       // System.out.println(s);
       //System.out.println(singlyLinkedList);
        System.out.println(singlyLinkedList.contains(null));
        System.out.println(singlyLinkedList.get(0));

    }
}
