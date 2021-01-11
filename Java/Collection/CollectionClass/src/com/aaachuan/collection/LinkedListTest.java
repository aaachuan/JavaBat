package com.aaachuan.collection;


import java.util.LinkedList;

public class LinkedListTest {
    public static void main(String[] args) {
        LinkedList idList = new LinkedList();
        Id tmp = new Id("2",7.0);
        idList.add(tmp);
        int pos = idList.indexOf(new Id("2",0.0));
        System.out.println(pos);
        Id result = (Id) idList.get(pos);
        System.out.println(result.value);

        LinkedList list = new LinkedList();
        list.add(1);
        list.add(2);
        list.add(3);

        list.remove(-1);

        System.out.println(list);

    }

}


class Id {
    private String id;
    double value;
    Id(String id, double value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id obj = (Id) o;
        return id == obj.id;
    }

}
