package com.johnsbank.java.utilities;
// In a LinkedList we use an Object, often referred to as a Node, as a way to do:
//   1. Provide data for a particular element in the Collection
//   2. Reference to the next Node in the list.
//
// Traversing a LinkedList can be quite slower than they are with ArrayLists
// However, the process of insertion and deletion can be significantly faster.

import java.util.Iterator;

/**
 * A custom generic linked list used as a replacement for the default one in Container
 * @param <T> The type the linked list holds
 */
public class MyLinkedList<T> implements Iterable<T> {

    /**
     * I implemented this before I knew about the Comparator interface
     * @param <T> - The Type the container has to compare
     */
    @FunctionalInterface
    public interface Compare<T> {
        boolean operation(T a, T b);
    }

    Node<T> head = null;
    Node<T> tail = null; // not implemented
    int count = 0;

    /**
     * Adds an element to the list
     * @param data - The ned element to add
     * @return returns the amount of elements in the list
     */
    public int add(T data){
        Node<T> newNode = new Node<T>(data);

        if(head == null) {
            tail = newNode;
            head = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        return ++count;
    }

    /**
     * Using my comparator interface, inserts an element in a sorted fashion
     * @param data - the new element to insert
     * @param comparator - the comparator object used to keep the elements sorted
     */
    public void insertOrdered(T data, Compare<T> comparator) {
        Node<T> it = head;
        Node<T> newNode = new Node<T>(data);

        // we are inserting the first element into the list
        if(it == null)
        {
            head = newNode;
            return;
        }

        if(comparator.operation(newNode.data, head.data)){
            head = newNode;
            newNode.next = it;
            return;
        }

        Node<T> prevIt = it;
        it = it.next;
        while(it != null && !comparator.operation(newNode.data, it.data)){
            prevIt = it;
            it = it.next;
        }

        prevIt.next = newNode;
        newNode.next = it;

    }

    /**
     * Given an index inserts an element at the given index
     * @param index - The index to insert the new element at
     * @param data - the new element being inserted
     * @return - true if the element successfully was added
     */
    public boolean insertAt(int index, T data){
        if(index > count || index < 0)
            return false;

        Node<T> newNode = new Node<T>(data);
        Node<T> it = head;
        Node<T> prevIt = it;

        if(index == 0){
            head = newNode;
            newNode.next = it;
        }

        int at = 0;
        while(at < index) {
            prevIt = it;
            it = it.next;
            ++at;
        }

        prevIt.next = newNode;
        newNode.next = it;
        ++count;
        return true;
    }

    /**
     * Given an index get an element
     * @param index - the index used to specify the element
     * @return - the element that was successfully inserted, null otherwise
     */
    public T getAt(int index){

        if(index < 0 || index >= count)
            return  null;

        Node<T> it = head;
        for(int i = 0; i < index; ++i)
        {
            if(it == tail)
                return null;
            it = it.next;
        }

        return it.data;
    }

    /**
     * true if the list is empty
     * @return - True if there are no elements
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns all the elements in the list as a String
     * @return the elements in string form
     */
    @Override
    public String toString(){
        StringBuilder out = new StringBuilder("(");

        for(Node<T> it = head; it != null; it = it.next){
            out.append(it.data);
            if(it != tail)
                out.append(", ");
        }

        out.append(")");
        return out.toString();
    }

    /**
     * returns Custom iterator class for use in the MyLinkedList class
     * @return an iterator for the MyLinkedList class
     */
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    /**
     * Custom iterator class for use in the MyLinkedList class
     */
    class MyIterator implements Iterator<T> {

        private Node<T> nextNode;
        private Node<T> curNode;
        private Node<T> prevNode;


        MyIterator(){
            nextNode = MyLinkedList.this.head;
        }

        public boolean hasNext() {
            return nextNode != null;
        }

        public T next() {
            T retVal = nextNode.data;
            prevNode = curNode;
            curNode = nextNode;
            nextNode = nextNode.next;
            return retVal;
        }

        public void remove() {
            if(nextNode == MyLinkedList.this.head) {
                throw new IllegalStateException();
            }
            else if(curNode == MyLinkedList.this.head){
                MyLinkedList.this.head = nextNode;
            } else if(curNode != null)
                prevNode.next = curNode.next;

            curNode = nextNode;
            if(nextNode!= null)
                nextNode = nextNode.next;
        }
    }
}
