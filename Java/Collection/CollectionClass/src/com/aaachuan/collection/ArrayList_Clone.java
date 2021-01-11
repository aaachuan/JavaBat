package com.aaachuan.collection;

import java.util.ArrayList;

public class ArrayList_Clone {
    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("东");
        arrayList.add("南");
        arrayList.add("西");
        arrayList.add("北");
        Object o = arrayList.clone();
        System.out.println(o == arrayList);
        System.out.println(o);
        System.out.println(arrayList);

        System.out.println("this part is about clone things beginning....");
        Students s1 = new Students("鲁智深", 30);
        Students s2 = new Students();
        s2.setName(s1.getName());
        s2.setAge(s1.getAge());
        System.out.println(s1);
        System.out.println(s2);
        s1.setAge(33);
        System.out.println(s1);
        System.out.println(s2);

        System.out.println("this part is about soft clone....");
        Students s3 = new Students("鲁智深", 30);
        Students s4 = (Students) s3.clone();
        System.out.println(s3 == s4);
        System.out.println(s3);
        System.out.println(s4);

        s3.setAge(33);
        System.out.println(s3);
        System.out.println(s4);

        System.out.println("this part is bad thing about soft clone....No!!!!!");
        Skill skill = new Skill("拳打镇关西");
        NewStudents ns1 = new NewStudents("鲁智深", 30, skill);
        NewStudents ns2 = (NewStudents) ns1.clone();
        System.out.println(ns1 == ns2);
        System.out.println(ns1);
        System.out.println(ns2);

        ns1.setAge(33);
        skill.setSkill("倒把垂杨柳");
        //ns1.setSkill(new Skill("倒把垂杨柳"));
        System.out.println(ns1);
        System.out.println(ns2);



    }
}


class Students implements Cloneable {
    private String name;
    private Integer age;
    public Students(String name, Integer age) {
        super();
        this.name = name;
        this.age = age;
    }

    public Students() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Students{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class NewStudents implements Cloneable {
    private String name;
    private Integer age;
    private Skill skill;
    public NewStudents() {}
    public NewStudents(String name, Integer age, Skill skill) {
        this.name = name;
        this.age = age;
        this.skill = skill;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Skill getSkill() {
        return skill;
    }

    @Override
    public String toString() {
        return "NewStudents{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", skill=" + skill +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        //return super.clone();
        NewStudents students = (NewStudents) super.clone();
        Skill skill = (Skill) this.skill.clone();
        students.setSkill(skill);
        return students;
    }
}

class Skill implements Cloneable {
    private String skill;

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkill() {
        return skill;
    }
    public Skill() {}
    public Skill(String skill) {
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "skill='" + skill + '\'' +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}