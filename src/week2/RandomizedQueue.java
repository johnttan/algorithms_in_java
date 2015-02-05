import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queueArray;
    private int sizeNum;
    private int tail = 0;

    private void resize(boolean up)
    {
        Item[] tempArray = queueArray;
        int factor = up ? 2 : 1/2;
        if(factor < 1){
            factor = 1;
        }
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
        return size() == 0;
    }
    public int size()
    {
        return sizeNum;
    }
    public void enqueue(Item item) throws NullPointerException
    {
        if(item == null){
            throw new NullPointerException();
        }

        queueArray[tail] = item;
        tail ++;
        sizeNum ++;

        if(size() == queueArray.length){
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
        sizeNum --;
        if(size() <= size() / 4)
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
        private RandomizedQueue tempQueue;
        public QueueIterator() {
            tempQueue = new RandomizedQueue();
            for(Item x : queueArray){
                tempQueue.enqueue(x);
            }
        }
        public boolean hasNext()
        {
            return !(tempQueue.isEmpty());
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
            Object result = tempQueue.dequeue();
            return (Item) result;
        }
    }

    public static void main(String[] args)
    {
        RandomizedQueue testQueue = new RandomizedQueue();
//        for(int i=0;i<100;i++){
//            testQueue.enqueue(i);
//        }
//
//        for(int i=0;i<50;i++){
//            testQueue.dequeue();
//        }
//        for(int i=0;i<1000;i++){
//            testQueue.enqueue(i);
//        }
//        for(int i=0;i<1000;i++){
//            testQueue.dequeue();
//        }
//        while(!testQueue.isEmpty()){
//            System.out.println(testQueue.dequeue());
//        }
//
//        testQueue = new RandomizedQueue();
//        for(int i=0;i<10;i++){
//            testQueue.enqueue(i);
//        }
//        for(Object x : testQueue){
//            System.out.println(x);
//        }
//
//        testQueue = new RandomizedQueue();
//        for(int i=0;i<100;i++){
//            testQueue.enqueue(i);
//        }
//        for(int i=0;i<=testQueue.size();i++){
//            System.out.println(testQueue.sample());
//        }

        testQueue = new RandomizedQueue();
        testQueue.enqueue(1);
        testQueue.dequeue();
        testQueue.enqueue(1);
        testQueue.dequeue();
    }
}
