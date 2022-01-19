package com.johnsbank.java.utilities;
// In a LinkedList we use an Object, often referred to as a Node, as a way to do:
//   1. Provide data for a particular element in the Collection
//   2. Reference to the next Node in the list.
//
// Traversing a LinkedList can be quite slower than they are with ArrayLists
// However, the process of insertion and deletion can be significantly faster.

import java.util.Iterator;

public class MyLinkedList<T> implements Iterable<T> {

    public interface Compare<T> {
        boolean operation(T a, T b);
    }

    Node<T> head = null;
    Node<T> tail = null;
    int count = 0;

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

    public boolean isEmpty() {
        return head == null;
    }

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

    public Iterator<T> iterator() {
        return new MyIterator();
    }

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
