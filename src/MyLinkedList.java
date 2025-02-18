// File: MyLinkedList.java
public class MyLinkedList<E> {
    private Node<E> head;
    private int size;

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
            this.next = null;
        }
    }

    public MyLinkedList() {
        head = null;
        size = 0;
    }

    public void add(E value) {
        if (!contains(value)) {
            Node<E> newNode = new Node<>(value);
            newNode.next = head;
            head = newNode;
            size++;
        }
    }

    public boolean contains(E value) {
        Node<E> current = head;
        while (current != null) {
            if (current.value.equals(value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean remove(E value) {
        Node<E> current = head;
        Node<E> prev = null;

        while (current != null) {
            if (current.value.equals(value)) {
                if (prev == null) {
                    head = current.next; // Remove head node
                } else {
                    prev.next = current.next; // Remove middle or last node
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public E[] toArray() {
        @SuppressWarnings("unchecked")
        E[] array = (E[]) new Object[size];
        Node<E> current = head;
        int index = 0;

        while (current != null) {
            array[index++] = current.value;
            current = current.next;
        }
        return array;
    }

    public int size() {
        return size;
    }

}
