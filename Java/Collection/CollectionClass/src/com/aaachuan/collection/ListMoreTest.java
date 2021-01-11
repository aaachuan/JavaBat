package com.aaachuan.collection;

import java.util.ArrayList;
import java.util.List;

public class ListMoreTest {
    public static void main(String[] args) {
        List list = new ArrayList();
        list.add(20);
        list.add(30);
        list.add(40);
        list.add(50);
        System.out.println(list.size());
        System.out.println(list);
//        list.remove(0);
//        list.remove((Object) 40);
//        list.remove(new Integer(30)); //整数缓存
//        System.out.println(list.size());
//        System.out.println(list);
        List sublist = list.subList(1, 3);
        System.out.println(sublist);

    }
}
