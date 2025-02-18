// File: MyHashSet.java
import java.util.LinkedList;

public class MyHashSet<E> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private MyLinkedList<E>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = (MyLinkedList<E>[]) new MyLinkedList[INITIAL_CAPACITY];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new MyLinkedList<>();
        }
        size = 0;
    }

    private int hash(E value) {
        return (value == null) ? 0 : Math.abs(value.hashCode() % buckets.length);
    }

    public boolean add(E value) {
        int index = hash(value);
        MyLinkedList<E> bucket = buckets[index];

        if (!bucket.contains(value)) {
            bucket.add(value);
            size++;
            if (size > LOAD_FACTOR * buckets.length) {
                resize();
            }
            return true;
        }
        return false;
    }

    public boolean contains(E value) {
        int index = hash(value);
        MyLinkedList<E> bucket = buckets[index];
        return bucket.contains(value);
    }

    public boolean remove(E value) {
        int index = hash(value);
        MyLinkedList<E> bucket = buckets[index];

        if (bucket.remove(value)) {
            size--;
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        MyLinkedList<E>[] oldBuckets = buckets;
        buckets = (MyLinkedList<E>[]) new MyLinkedList[oldBuckets.length * 2];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new MyLinkedList<>();
        }

        size = 0;
        for (MyLinkedList<E> bucket : oldBuckets) {
            if (bucket != null) {
                for (E value : bucket.toArray()) {
                    add(value);
                }
            }
        }

    }

}

