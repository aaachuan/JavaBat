package com.aaachuan.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.equals("2"))
                //list.remove("2");
            //删除的集合在集合的倒数第二个元素时不会产生并发修改异常

                it.remove();
        }
        System.out.println(list);
    }
}
