import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Smolin on 22.11.2016.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private MyArrayList<Item> nodes;

    private class RandomizedQueueIterator implements Iterator<Item> {
        RandomizedQueue<Item> cloneQueue;

        RandomizedQueueIterator(){
            cloneQueue = new RandomizedQueue<Item>();
            for (int i = 0; i < size(); i++) {
                cloneQueue.nodes.add(nodes.get(i));
            }
        }

        @Override
        public boolean hasNext() {
            return !cloneQueue.isEmpty();
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                return cloneQueue.dequeue();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        nodes = new MyArrayList<Item>();
    }

    // is the queue empty?
    public boolean isEmpty() {
        return nodes.size() == 0;
    }

    // return the number of nodes on the queue
    public int size() {
        return nodes.size();
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        } else {
            nodes.add(item);
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            int ind = StdRandom.uniform(size());
            Item deletedNodeValue = nodes.get(ind);
            nodes.swapLastWithInd(ind);
            nodes.removeLast();
            return deletedNodeValue;
        }
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int ind = StdRandom.uniform(size());
        return nodes.get(ind);
    }

    // return an independent iterator over nodes in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> rQueue = new RandomizedQueue<>();
        for (int i = 0; i < 26; i++) {
            rQueue.enqueue(i);
        }

        for (int i : rQueue) {
            System.out.print(i + " ");
        }

        System.out.println();


        for (int i : rQueue) {
            System.out.print(i + " ");
        }

        System.out.println();

        for (int i = 0; i < 25; i++) {
            rQueue.dequeue();
        }

        for (int i : rQueue) {
            System.out.print(i + " ");
        }
        System.out.println();

    }

    private static class MyArrayList<T>  {
        private T[] ar;
        private static final int FIRST_ARRAY_SIZE = 4;
        private int size = 0;
        private int sizeReserved;


        MyArrayList() {
            ar = construct(FIRST_ARRAY_SIZE);
        }

        @SuppressWarnings("unchecked")
        //http://www.ibm.com/developerworks/java/library/j-jtp01255/index.html
        private T[] construct(int newSize) {
            sizeReserved = newSize;
            return (T[]) new Object[newSize];
        }

        void add(T el) {
            ar[size] = el;
            size++;
            if (size >= sizeReserved) {
                resize(sizeReserved * 2);
            }
        }

        void removeLast() {
            ar[size] = null;
            size--;
            if (size <= sizeReserved / 4) {
                resize(sizeReserved / 2);
            }
        }

        void swapLastWithInd(int i) {
            if(i < 0 || i >= size()) {
                throw new IndexOutOfBoundsException();
            }
            int lastInd = size() - 1;
            ar[i] = ar[lastInd];
        }

        boolean isEmpty() {
            return size == 0;
        }

        int size() {
            return size;
        }

        T get(int i) {
            if(i < 0 || i >= size()) {
                throw new IndexOutOfBoundsException();
            }
            return ar[i];
        }

        private void resize(int newSize) {
            T[] arr = construct(newSize);
            System.arraycopy(ar, 0, arr, 0, size());
            ar = arr;
        }

        public void set(int i, T item) {
            if(i < 0 || i >= size()) {
                throw new IndexOutOfBoundsException();
            }
            ar[i] = item;
        }
    }
}

