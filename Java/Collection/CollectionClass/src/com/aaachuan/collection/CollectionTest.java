package com.aaachuan.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionTest {
    public static void main(String[] args) {
        Collection collection = new ArrayList(0);
        collection.add("魏");
        collection.add("蜀");
        collection.add("吴");
        System.out.println(collection.size());
        System.out.println(collection);
        //collection.remove("蜀");
//        collection.clear();
//        System.out.println(collection.size());
//        System.out.println(collection);
        System.out.println("-------for..each遍历-------");
        for (Object obj: collection) {
            System.out.println(obj);
        }
        System.out.println("-------Iterator遍历-------");
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            System.out.println(s);
            //collection.remove(s);
            //iterator.remove();
        }
        //System.out.println(collection.size());
        System.out.println(collection.contains("蜀"));
        System.out.println(collection.isEmpty());
    }
}
