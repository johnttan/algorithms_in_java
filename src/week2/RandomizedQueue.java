import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queueArray;
    private int size;


    public RandomizedQueue()
    {
        queueList = (Item[]) new Object[1];
    }
    public boolean isEmpty()
    {
        return size == 0;
    }
    public int size()
    {
        return size;
    }
    public void enqueue(Item item) throws NullPointerException
    {

    }
    public Item dequeue() throws NoSuchElementException
    {

    }
    public Item sample() throws NoSuchElementException
    {

    }
    public Iterator<Item> iterator()
    {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item>
    {
        private Node current = head;
        public boolean hasNext()
        {
            return current.item != null;
        }
        public void remove() throws UnsupportedOperationException
        {
            throw new UnsupportedOperationException();
        }
        public Item next() throws NoSuchElementException
        {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args)
    {

    }
}
