package com.aaachuan.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContainsTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("PHP");
        list.add("Java");
       //list.add("JavaSE");


//        int count = 0;
//        Iterator<String> it = list.iterator();
//        while (it.hasNext()) {
//            String s = it.next();
//            if (!s.equals("JavaSE"))
//                count++;
//            if (count==list.size()) {
//                list.add("JavaSE");
//                break;
//            }
//        }



//        for (int i = 0; i < list.size(); i++) {
//            String s = list.get(i);
//            if (!s.equals("JavaSE"))
//                count++;
//            if (count==list.size()) {
//                list.add("JavaSE");
//                break;
//            }
//        }

        boolean flag = false;
        for (String s: list) {
            if (s.equals("JavaSE")) {
                flag = true;
                break;
            }
        }
        if (!flag)
            list.add("JavaSE");
        System.out.println(list);


        List<String> list1 = new ArrayList<>(1000000);
//        list1.add("hello");
//        list1.add("PHP");
//        list1.add("Java");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            list1.add(i + "");
        }
       long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);


        List<String> list2 = new ArrayList<>();
//        list2.add("hello");
//        list2.add("PHP");
//        list2.add("Java");
        startTime = System.currentTimeMillis();
        for (int i = 1000000; i > 0; i--) {
            list2.add(i + "");
        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        //https://www.bilibili.com/video/BV1gE411A7H8?p=43
        //频繁扩容性能急剧下降的案例不严谨
    }

}
