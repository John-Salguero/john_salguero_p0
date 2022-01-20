package com.johnsbank.java.utilities;

import java.util.Iterator;

// An ArrayList is a list that is simply 'backed' by an array
// That means that the data structures used to store the elements is still managed through an Array
// but new functionality is added to make it more dynamic
public class MyArrayList<T> implements Iterable<T> {

    private int count = 0;
    private int size = 10;
    private Object[] array = new Object[size];

    /**
     * I implemented this before I knew about the Java comparators in the given libraries, acts as a comparator
     * @param <T> - The element that will be compared in the operation
     */
    @FunctionalInterface
    public interface Compare<T> {
        boolean operation(T a, T b);
    }

    /**
     * Using the comparator I implemented, inserts an element in a sorted manner
     * @param data - The data being inserted
     * @param comparator - the comparator used to keep it sorted
     */
    public void insertOrdered(T data, MyArrayList.Compare<T> comparator) {

        for(int i = 0; i < count; ++i)
        {
            if(comparator.operation(data, (T)array[i])) {
                insert(i, data);
                return;
            }
        }
        add(data);
    }

    /**
     * Gets the element at an index
     * @param index - the index to get the object
     * @return - The object at the given index
     */
    public T get(int index) {
        return (T)array[index];
    }

    /**
     * Grows the internal array
     */
    private void grow(){
        Object[] array = new Object[size*2];

        System.arraycopy(this.array, 0, array, 0, size);

        size *= 2;
        this.array = array;
    }

    /**
     * Removes the element at the given index
     * @param index - The index to remove
     */
    public void remove(int index) {
        if(index >= count || index < 0)
            return;

        --count;
        System.arraycopy(array, index + 1, array, index, count - index);

    }

    /**
     * Adds the given element into the array
     * @param element - the value you want to add into the array
     * @return - the index that it was added at
     */
    public int add(T element) {
        if (count == size)
        {
            grow();
        }
        array[count++] = element;

        return count - 1;
    }

    /**
     * Returns the count of elements
     * @return - how many elements in the container
     */
    public int size() {
        return count;
    }

    /**
     * returns true if not holding any elements
     * @return - if the array is empty, returns true
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returnd the last element in the array
     * @return - the last element in the array
     */
    public T end(){
        if(count < 1)
            return null;
        return (T)array[count-1];
    }

    /**
     * replaces an element in the array
     * @param index - the index to replace the element with
     * @param data - the data replacing the element
     */
    public void replace(int index, T data){
        if(index >= count || index < 0)
            return;

        array[index] = data;
    }

    /**
     * returns the count of elements
     * @return - the count of elements
     */
    public int getCount() {
        return count;
    }

    /**
     * given an index and data, inserts that data at the index
     * @param index - the index to put the new element
     * @param element - the element to place in the index
     */
    public void insert(int index, T element){
        if(index < 0)
            index = 0;

        if(index >= count)
            add(element);
        else{
            if(count == size)
                grow();
            System.arraycopy(array, index, array, index + 1, count - index);
            array[index] = element;
            ++count;
        }

    }

    /**
     * returns the array to undergo low level operations
     * @return - The internal array of the container
     */
    public T[] getData(){
        return (T[]) array;
    }

    /**
     * returns all the elements inside a String
     * @return - the string containing all the elements
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("[");

        for(int i = 0; i < count; ++i)
        {
            out.append(array[i]);
            if(i < count -1) out.append(", ");
        }

        out.append("]");

        return out.toString();
    }

    /**
     * returns an iterator to be used for the container
     * @return The iterator used for the container
     */
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    /**
     * Iterator class used for MyArrayList
     */
    class MyIterator implements Iterator<T> {

        private int index = 0;

        public boolean hasNext() {
            return index < size();
        }

        public T next() {
            return get(index++);
        }

        public void remove() {
            MyArrayList.this.remove(--index);
        }
    }

}
