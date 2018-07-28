import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Smolin on 21.11.2016.
 */
public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Item value;
        Node next;
        Node prev;

        Node(Item value, Node prev, Node next) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node first = null;
    private Node last = null;
    private int size = 0;

    private class DequeueIterator implements Iterator<Item> {
        Node cur = first;

        @Override
        public boolean hasNext() {
            return !isEmpty() && cur != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                Item curVal = cur.value;
                if(hasNext()){
                    cur = cur.next;
                }
                return curVal;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of nodes on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if(!firstInit(item)){
            Node tmp = new Node(item, null, first);
            first.prev = tmp;
            first = tmp;
            size++;
        }
    }

    // add the item to the end
    public void addLast(Item item) {
        if(!firstInit(item)){
            Node tmp = new Node(item, last, null);
            last.next = tmp;
            last = tmp;
            size++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if(isEmpty()){
            throw new NoSuchElementException();
        } else {
            Item tmp = first.value;
            //if one element
            if(first.next == null){
                first = null;
                last = null;
            } else {
                first.next.prev = null;
                Node tmpNode = first.next;
                first.next = null;
                first = tmpNode;
            }

            size--;
            return tmp;
        }
    }

    // remove and return the item from the end
    public Item removeLast() {
        if(isEmpty()){
            throw new NoSuchElementException();
        } else {
            Item tmp = last.value;
            //if one element
            if(last.prev == null){
                first = null;
                last = null;
            } else {
                last.prev.next = null;
                Node tmpNode = last.prev;
                last.prev = null;
                last = tmpNode;
            }
            size--;
            return tmp;
        }
    }

    // return an iterator over nodes in order from front to end
    public Iterator<Item> iterator() {
        return new DequeueIterator();
    }

    // unit testing
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.addFirst(0);
        deque.addLast(7);
        deque.addLast(8);
        deque.addLast(9);
        deque.addFirst(-1);
        deque.addFirst(-2);
        deque.addLast(10);
        for (int i: deque) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < 3; i++) {
            deque.removeFirst();
        }
        for (int i = 0; i < 3; i++) {
            deque.removeLast();
        }
        for (int i: deque) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < 2; i++) {
            deque.removeFirst();
            deque.removeLast();
        }
        for (int i: deque) {
            System.out.print(i + " ");
        }
        System.out.println();
        deque.removeFirst();
        deque.removeLast();
        deque.removeLast();
    }

    private boolean firstInit(Item item) {
        if(item == null){
            throw new NullPointerException();
        }
        if(first == null && last == null){
            first = new Node(item, null, null);
            last = first;
            size++;
            return true;
        }
        return false;
    }
}
