package com.aaachuan.collection;

import java.util.Arrays;

/**
 * 重写ArrayList
 * @param <E>
 */
public class MyArrayList<E> {
    //对象数组
    private Object[] elementData;
    //实际元素个数
    private int size;
    //初始分配数组大小
    private final int DEFAULT_CAPACITY = 10;
    //默认空数组
    private final Object[] emptyArray = {};

    //初始化空数组
    public MyArrayList() {
        elementData = emptyArray;
    }

    //自定义分配大小
    public MyArrayList(int initsize) {
        if (initsize < 0) {
            throw new IllegalArgumentException("初始化大小不合法:" + initsize);
        } else if (initsize == 0) {
            elementData = emptyArray;
        } else {
            elementData = new Object[initsize];
        }
    }

    //添加
    public boolean add(E e) {
        //首次添加时初始化数组，大小分配为10
        if (elementData == emptyArray)
            elementData = new Object[DEFAULT_CAPACITY];
        if (size == elementData.length) {
            //扩容大约1.5倍
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            Object[] obj = new Object[newCapacity];
            //System.arraycopy(elementData, 0, obj, 0, elementData.length);
            //数组复制
            for (int i = 0; i < elementData.length; i++) {
                obj[i] = elementData[i];
            }
            elementData = obj;
        }
        elementData[size++] = e;
        return true;
    }

    //index索引位置插入，返回原先位置旧元素
    public E insert(int index, E e) {
        rangeCheck(index);
        E oldValue = (E) elementData[index];
        //2倍扩容
        if (size == elementData.length) {
            Object[] obj = new Object[elementData.length*2];
            for (int i = 0; i < elementData.length; i++) {
                obj[i] = elementData[i];
            }
            elementData = obj;
        }
        for (int i = size-1; i >= index ; i--)
            elementData[i+1] = elementData[i];
        elementData[index] = e;
        size++;
        return oldValue;
    }

    //删除index索引元素值
    public E remove(int index) {
        rangeCheck(index);
        E value = (E) elementData[index];
        for (int i = index+1; i < size; i++) {
            elementData[i-1] = elementData[i];
        }
        elementData[--size] = null;
        return value;
    }


    //修改index索引元素值
    public E set(int index, E newValue) {
        rangeCheck(index);
        E value = (E) elementData[index];
        elementData[index] = newValue;
        return value;
    }

    //获取index索引元素值
    public E get(int index) {
        rangeCheck(index);
        return (E) elementData[index];
    }

    //查找是否包含元素值，如包含返回index索引值，不包含返回-1
    public int contain(E e) {
        for (int i = 0; i < size; i++) {
            if (elementData[i].equals(e))
                return i;
        }
        return -1;
    }

    //清空元素值
    public void clear() {
        for (int i = 0; i < size; i++)
            elementData[i] = null;
        size = 0;
    }

    //数组元素实际大小
    public int getSize() {
        return size;
    }

    //判断是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    //边界检查
    private void rangeCheck(int index) {
        if (index < 0 || index > size)
            throw new ArrayIndexOutOfBoundsException("数组越界");
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i == size-1) {
                sb.append(elementData[i]).append("]");
            } else {
                sb.append(elementData[i]).append(",");
            }
        }
        return sb.toString();
    }
}
