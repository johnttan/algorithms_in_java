import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queueArray;
    private int size;
    private int tail = 0;

    private void resize(boolean up)
    {
        Item[] tempArray = queueArray;
        int factor = up ? 2 : 1/2;
        queueArray = (Item[]) new Object[queueArray.length * factor];
        for(int i = 0;i<tail;i++){
            queueArray[i] = tempArray[i];
        }
    }

    public RandomizedQueue()
    {
        queueArray = (Item[]) new Object[1];
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
        if(item == null){
            throw new NullPointerException();
        }

        queueArray[tail] = item;
        tail ++;
        size ++;

        if(size == queueArray.length){
            resize(true);
        }

    }
    public Item dequeue() throws NoSuchElementException
    {
        if(isEmpty()){
            throw new NoSuchElementException();
        }

        int random = StdRandom.uniform(tail);

        Item temp = queueArray[tail-1];
        Item result = queueArray[random];
        queueArray[random] = temp;
        queueArray[tail-1] = null;

        tail --;
        size --;
        if(size <= size() / 4)
        {
            resize(false);
        }
        return result;
    }
    public Item sample() throws NoSuchElementException
    {
        if(isEmpty()){
            throw new NoSuchElementException();
        }

        int random = StdRandom.uniform(tail);

        Item result = queueArray[random];

        return result;
    }
    public Iterator<Item> iterator()
    {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item>
    {
        public boolean hasNext()
        {
            return !isEmpty();
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
            return sample();
        }
    }

    public static void main(String[] args)
    {
        RandomizedQueue testQueue = new RandomizedQueue();
        for(int i=0;i<10;i++){
            testQueue.enqueue(i);
        }

        while(!testQueue.isEmpty()){
            System.out.println(testQueue.dequeue());
        }
    }
}
