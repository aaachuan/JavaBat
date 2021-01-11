package com.aaachuan.collection;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class CollectionMoreTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Collection collection = new ArrayList();
        Student s1 = new Student("曹操",56);
        Student s2 = new Student("刘备",49);
        Student s3 = new Student("孙权",75);
        collection.add(s1);
        collection.add(s2);
        collection.add(s3);
        //collection.add(s2);
        System.out.println(collection.size());
        System.out.println(collection);
        //collection.remove(s2);
        collection.remove(new Student("刘备", 48));
        System.out.println(collection.size());
        System.out.println(collection);
//        collection.clear();
//        System.out.println(collection.size());
//        System.out.println(collection);

        System.out.println("-------for..each遍历-------");
        for (Object obj: collection) {
            Student student = (Student) obj;
            System.out.println(student);
        }

        System.out.println("-------Iterator遍历-------");
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Student s = (Student) iterator.next();
            System.out.println(s);
            //collection.remove(s);
            //iterator.remove();
        }

        System.out.println(collection.contains(new Student("刘备", 49)));
        System.out.println(collection.isEmpty());


        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:\\IdeaProjects\\CollectionClass\\serz"));
        oos.writeObject(collection);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:\\IdeaProjects\\CollectionClass\\serz"));
        ArrayList<Student> studentArrayList = (ArrayList<Student>) ois.readObject();
        ois.close();
        for ( Student student: studentArrayList) {
            System.out.println(student);
        }

    }
}

class Student implements Serializable {
    private String name;
    private int age;

    public Student(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

//    @Override
//    public String toString() {
//        return "Student{" +
//                "name='" + name + '\'' +
//                ", age=" + age +
//                '}';
//        // 字符串拼接占用对象，常量有优化，变量会创建StringBuiler对象（待考证）
//    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student{name='");
        sb.append(this.name);
        sb.append("', age=");
        sb.append(this.age);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
