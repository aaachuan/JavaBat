# 并发容器

## 写时复制List和Set

### CopyOnWriteArrayList
CopyOnWriteArrayList实现了List接口，特点：
- 线程安全
- 迭代器不支持修改操作，但也不会抛出ConcurrentModificationException
- 以原子方式支持一些复合操作

synchronized迭代时，同步容器不能解决ConcurrentModificationException，要避免需要在遍历的时候给整个容器对象加锁，但是CopyOnWriteArrayList没有这个问题。

```

    /**
     * Appends the element, if not present.
     *
     * @param e element to be added to this list, if absent
     * @return {@code true} if the element was added
     */
    public boolean addIfAbsent(E e)
    
    
    /**
     * Appends all of the elements in the specified collection that
     * are not already contained in this list, to the end of
     * this list, in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param c collection containing elements to be added to this list
     * @return the number of elements added
     * @throws NullPointerException if the specified collection is null
     * @see #addIfAbsent(Object)
     */
    public int addAllAbsent(Collection<? extends E> c)
```
写时复制：每次修改操作新建一个数组，复制原数组内容到新数组，修改新数组后再以原子方式设置数组内部引用。
```

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            E oldValue = get(elements, index);

            if (oldValue != element) {
                int len = elements.length;
                Object[] newElements = Arrays.copyOf(elements, len);
                newElements[index] = element;
                setArray(newElements);
            } else {
                // Not quite a no-op; ensures volatile write semantics
                setArray(elements);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }
```
```
public class CopyOnWriteArrayList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    private static final long serialVersionUID = 8673264195747942595L;

    /** The lock protecting all mutators */
    final transient ReentrantLock lock = new ReentrantLock();

    /** The array, accessed only via getArray/setArray. */
    private transient volatile Object[] array;

    /**
     * Gets the array.  Non-private so as to also be accessible
     * from CopyOnWriteArraySet class.
     */
    final Object[] getArray() {
        return array;
    }

    /**
     * Sets the array.
     */
    final void setArray(Object[] a) {
        array = a;
    }

    /**
     * Creates an empty list.
     */
    public CopyOnWriteArrayList() {
        setArray(new Object[0]);
    }
}
```
CopyOnWriteArrayList适合于读多写少的场景。

写时复制用于操作系统内部的进程管理和内存管理中。

### CopyOnWriteArraySet
CopyOnWriteArraySet是基于CopyOnWriteArrayList实现的。

### ConcurrentHashMap
并发版本的HashMap,特点：
- 并发安全
- 直接支持一些原子复合操作
- 支持高并发，读操作完全并行，写操作支持一定程度的并行
- 与同步容器Collections.synchronizedMap相比，迭代不用加锁，不会抛出ConcurrentModificationException
- 弱一致性

