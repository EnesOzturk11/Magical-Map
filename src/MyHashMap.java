public class MyHashMap<K, V> {
    // Node class for representing each key-value pair
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Node<K, V>[] buckets;
    private int capacity;
    private int size;

    // Default capacity for the hashmap
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
    }

    // Hash function to get the index for a key
    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    // Method to add or update a key-value pair
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> current = buckets[index];

        // Check if the key already exists and update it
        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Add a new node at the beginning of the chain
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;

        // Resize if the load factor is exceeded
        if ((float) size / capacity > LOAD_FACTOR) {
            resize();
        }
    }

    // Method to get the value for a key
    public V get(K key) {
        int index = hash(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null; // Key not found
    }

    // Method to remove a key-value pair
    public V remove(K key) {
        int index = hash(key);
        Node<K, V> current = buckets[index];
        Node<K, V> prev = null;

        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                if (prev == null) {
                    buckets[index] = current.next; // Remove head node
                } else {
                    prev.next = current.next; // Remove middle or last node
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null; // Key not found
    }

    // Method to check if a key exists in the map
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Method to get the current size of the map
    public int size() {
        return size;
    }

    // Method to resize the buckets array when load factor is exceeded
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] oldBuckets = buckets;
        buckets = (Node<K, V>[]) new Node[newCapacity];
        capacity = newCapacity;
        size = 0;

        for (Node<K, V> head : oldBuckets) {
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }

}

