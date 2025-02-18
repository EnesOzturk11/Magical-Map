// File: MyMinHeap.java

public class MyMinHeap<AnyType extends Comparable<? super AnyType>> {
    private static final int DEFAULT_CAPACITY = 10;
    public int currentSize;  // Number of elements in the heap
    public AnyType[] array;  // The heap array

    // Default constructor
    public MyMinHeap() {
        currentSize = 0;
        array = (AnyType[]) new Comparable[DEFAULT_CAPACITY];
    }

    // Constructor with capacity
    public MyMinHeap(int capacity) {
        currentSize = 0;
        array = (AnyType[]) new Comparable[capacity];
    }

    // Constructor that builds a heap from an existing array of items
    public MyMinHeap(AnyType[] items) {
        currentSize = items.length;
        array = (AnyType[]) new Comparable[items.length + 1];

        for (int i = 0; i < items.length; i++) {
            array[i + 1] = items[i];
        }

        buildHeap();
    }

    // Insert an element into the heap
    public void insert(AnyType x) {
        if (currentSize == array.length - 1) {
            enlargeArray(array.length * 2);
        }

        // Percolate up to maintain min-heap property
        int hole = ++currentSize;
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2) {
            array[hole] = array[hole / 2];
        }
        array[hole] = x;
    }

    // Find the minimum element
    public AnyType findMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return array[1];
    }

    // Delete the minimum element and return it
    public AnyType deleteMin() {
        if (isEmpty()) {
            return null;
        }

        AnyType minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);

        return minItem;
    }

    // Check if the heap is empty
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // Make the heap empty
    public void makeEmpty() {
        currentSize = 0;
    }

    // Percolate down method for maintaining min-heap property
    private void percolateDown(int hole) {
        int child;
        AnyType tmp = array[hole];

        while (hole * 2 <= currentSize) {
            child = hole * 2;
            if (child != currentSize && array[child + 1].compareTo(array[child]) < 0) {
                child++;
            }
            if (array[child].compareTo(tmp) < 0) {
                array[hole] = array[child];
            } else {
                break;
            }
            hole = child;
        }
        array[hole] = tmp;
    }

    // Build heap from an array of items
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    // Enlarge the array size
    private void enlargeArray(int newSize) {
        AnyType[] old = array;
        array = (AnyType[]) new Comparable[newSize];
        System.arraycopy(old, 0, array, 0, old.length);
    }

}
