package com.johnsbank.java.utilities;

import java.util.Iterator;

// An ArrayList is a list that is simply 'backed' by an array
// That means that the data structures used to store the elements is still managed through an Array
// but new functionality is added to make it more dynamic
public class MyArrayList<T> implements Iterable<T> {

    private int count = 0;
    private int size = 10;
    private Object[] array = new Object[size];

    public interface Compare<T> {
        boolean operation(T a, T b);
    }

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

    public T get(int index) {
        return (T)array[index];
    }

    private void grow(){
        Object[] array = new Object[size*2];

        System.arraycopy(this.array, 0, array, 0, size);

        size *= 2;
        this.array = array;
    }

    public void remove(int index) {
        if(index >= count || index < 0)
            return;

        --count;
        System.arraycopy(array, index + 1, array, index, count - index);

    }

    /**
     * Adds the given String into the array
     * @param str - the value you want to add into the array
     * @return - the index that it was added at
     */
    public int add(T str) {
        if (count == size)
        {
            grow();
        }
        array[count++] = str;

        return count - 1;
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public T end(){
        if(count < 1)
            return null;
        return (T)array[count-1];
    }

    public void replace(int index, T data){
        if(index >= count || index < 0)
            return;

        array[index] = data;
    }

    public int getCount() {
        return count;
    }

    public void insert(int index, T str){
        if(index < 0)
            index = 0;

        if(index >= count)
            add(str);
        else{
            if(count == size)
                grow();
            System.arraycopy(array, index, array, index + 1, count - index);
            array[index] = str;
            ++count;
        }

    }

    public T[] getData(){
        return (T[]) array;
    }


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

    public Iterator<T> iterator() {
        return new MyIterator();
    }

    class MyIterator implements Iterator<T> {

        private int index = 0;

        public boolean hasNext() {
            return index < size();
        }

        public T next() {
            return get(index++);
        }

        public void remove() {
            MyArrayList.this.remove(index);
        }
    }

}
