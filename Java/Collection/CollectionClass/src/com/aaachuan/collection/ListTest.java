package com.aaachuan.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListTest {
    public static void main(String[] args) {
        List list = new ArrayList();
        list.add("B");
        list.add("C");
        list.add(0,"A");
        System.out.println(list.size());
        System.out.println(list);

//        list.remove(1);
//        System.out.println(list.size());
//        System.out.println(list);
        System.out.println("-------for循环遍历-------");
        for (int i=0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println("-------for..each遍历-------");
        for (Object obj: list) {
            System.out.println(obj);
        }
        System.out.println("-------iterator遍历-------");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println("-------listIterator遍历-------");
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }
        while (listIterator.hasPrevious()) {
            System.out.println(listIterator.previous());
        }

        System.out.println(list.contains("B"));
        System.out.println(list.isEmpty());
    }
}
