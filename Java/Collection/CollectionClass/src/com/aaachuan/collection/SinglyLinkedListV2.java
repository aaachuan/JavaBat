package com.aaachuan.collection;



public class SinglyLinkedListV2<E> {
    //链表长度
    private int size;
    //头节点
    private Node first;

    /**
     * 节点定义(内部类)——单链表包含节点值与下一节点地址
     * @param <E>
     */
    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    /**
     * 指定索引返回对应元素
     * @param index
     * @return
     */
    public E get(int index) {
        indexCheck(index);
        return node(index).value;
    }

    /**
     * 指定索引节点修改元素
     * @param index
     * @param value
     * @return
     */
    public E set(int index, E value) {
        indexCheck(index);
        Node<E> oldNode =  node(index);
        E oldValue = oldNode.value;
        oldNode.value = value;
        return oldValue;
    }

    /**
     * 这里仍参考LinkedList的clear()方法，O(n)复杂度去置null;
     * 而非采用直接头节点置null的方法（有种解释是可达性算法，剩余node到达不了头节点GC ROOTS，就会被GC回收——在此情况并非准确）
     * 全置null的原因是中间的node在不同的代码区域仍存在对node的引用，例如List使用过Iterator的情况，迭代到中间断了的话，栈内
     * 有引用指向Iterator对象，而Iterator对象又有引用指向node导致整条链路得不到释放，GC就不能回收。
     * （年轻代的对象被老年代的对象引用，young gc不会回收）
     */
    public void clear() {
        // Clearing all of the links between nodes is "unnecessary", but:
        // - helps a generational GC if the discarded nodes inhabit
        //   more than one generation
        // - is sure to free memory even if there is a reachable Iterator
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.value = null;
            x.next = null;
            x = next;
        }
        first = null;
        size = 0;
    }

    /**
     * 通过元素定位索引
     * @param value
     * @return
     */
    public int indexOf(E value) {
        int index = 0;
        //需要区分判别是否为null，equals时value.equals(x.value)前后位置不可互换，因value已不可能为null，而x.value为null的话调用会报空指针异常
        if (value == null) {
            for (Node<E> x = first; x != null;  x = x.next) {
                if (x.value == null)
                    return index;
            }
            index++;
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (value.equals(x.value))
                    return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * 判断链表是否包含该元素
     * @param value
     * @return
     */
    public boolean contains(E value) {
        return indexOf(value) != -1;
    }

    /**
     * 指定索引位置插入元素，这里index插入位置比元素多一个位置，所以index可以取size长度
     * @param index
     * @param value
     */
    public void add(int index, E value) {
        //此时允许index值为size
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("索引值越界: index:" + index + ",size:" + size);
        if (index == 0) {
            //first = new Node<>(value, first);
           Node<E> newNode = new Node<>(value, first);
           first = newNode;
        } else {
            Node<E> pre = node(index - 1);
            Node<E> next = pre.next;
            //下面两步可合并成
            //pre.next = new Node<>(value, next);
            Node<E> newNode = new Node<>(value, next);
            pre.next = newNode;
        }
        size++;
    }

    /**
     * 默认即最后一个位置插入
     * @param value
     */
    public void add(E value) {
        add(size, value);
    }

    /**
     * 通过索引移除元素
     * @param index
     * @return
     */
    public E remove(int index) {
        indexCheck(index);
        Node<E> oldNode = first;
        if (index == 0) {
            first = first.next;
        } else {
            Node<E> pre = node(index - 1);
            oldNode = pre.next;
            pre.next = oldNode.next;
        }
        return oldNode.value;
    }

    /**
     * 索引越界检查
     * index应满足(index >= 0 && index < size)
     * @param index
     */
    private void indexCheck(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("索引值越界: index:" + index + ",size:" + size);
    }

    /**
     * 通过索引定位元素(其他方法使用场景多而抽取)
     * @param index
     * @return
     */
    private Node<E> node(int index) {
        Node<E> x = first;
        for (int i = 0; i < index; i++) {
            x = x.next;
        }
        return x;
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        StringBuilder sb = new StringBuilder().append("[");
        for (Node<E> x = first; x != null; x = x.next) {
            sb.append(x.value);
            if (x.next == null)
                return sb.append("]").toString();
            sb.append(",");
        }
        return sb.toString();
    }
}
