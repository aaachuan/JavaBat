package com.aaachuan.collection;

import java.util.Arrays;

public class MyArrayList<E> {
    private Object[] elementData;
    private int size;
    private final int DEFAULT_CAPACITY = 10;
    private final Object[] emptyArray = {};

    public MyArrayList() {
        elementData = emptyArray;
    }

    public MyArrayList(int initsize) {
        if (initsize < 0) {
            throw new IllegalArgumentException("初始化大小不合法:" + initsize);
        } else if (initsize == 0) {
            elementData = emptyArray;
        } else {
            elementData = new Object[initsize];
        }
    }

    public boolean add(E e) {
        if (elementData == emptyArray)
            elementData = new Object[DEFAULT_CAPACITY];
        if (size == elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            Object[] obj = new Object[newCapacity];
            //System.arraycopy(elementData, 0, obj, 0, elementData.length);
            for (int i = 0; i < elementData.length; i++) {
                obj[i] = elementData[i];
            }
            elementData = obj;
        }
        elementData[size++] = e;
        return true;
    }

    public E remove(int index) {
        rangeCheck(index);
        E value = (E) elementData[index];
        for (int i = index+1; i < size; i++) {
            elementData[i-1] = elementData[i];
        }
        elementData[--size] = null;
        return value;
    }


    public E set(int index, E newValue) {
        rangeCheck(index);
        E value = (E) elementData[index];
        elementData[index] = newValue;
        return value;
    }

    public E get(int index) {
        rangeCheck(index);
        return (E) elementData[index];
    }

    public void clear() {
        for (int i = 0; i < size; i++)
            elementData[i] = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

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
