import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node next;
        Node previous;
    }
    private Node head;
    private Node tail;
    private int size = 0;

    public Deque()
    {
        head = new Node();
        tail = head;
    }
    public boolean isEmpty()
    {
        return size == 0;
    }
    public int size()
    {
        return size;
    }
    public void addFirst(Item item)
    {
        head.item = item;
        head.previous = new Node();
        head.previous.next = head;
        head = head.previous;
        size ++;
    }
    public void addLast(Item item)
    {
        tail.item = item;
        tail.next = new Node();
        tail.next.previous = tail;
        tail = tail.next;
        size ++;
    }
    public Item removeFirst () throws NoSuchElementException
    {
        Node result;
        if(!isEmpty()){
            if(head.item == null){
                result = head.next;
            }else{
                result = head;
            }
            if(head.next.next != null){
                head.next.next.previous = head;
                head.next = head.next.next;
            }
            size --;
            return result.item;
        }else{
            throw new NoSuchElementException();
        }
    }
    public Item removeLast()
    {
        Node result;
        if(!isEmpty()){
            if(tail.item == null){
                result = tail.previous;
            }else{
                result = tail;
            }
            if(tail.previous.previous != null){
                tail.previous.previous.next = tail;
                tail.previous = tail.previous.previous;
            }
            size --;
            return result.item;
        }else{
            throw new NoSuchElementException();
        }
    }
    public Iterator<Item> iterator()
    {
        return new LinkedIterator();
    }
    private class LinkedIterator implements Iterator<Item>
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
        Deque testD = new Deque();
//        for(int i=0;i<20;i++)
//        {
//            testD.addFirst("test" + i);
//        }
//
//        while(!testD.isEmpty())
//        {
//            System.out.println(testD.removeFirst());
//        }
//
//        testD = new Deque();
//        for(int i=0;i<20;i++)
//        {
//            testD.addLast("test" + i);
//        }
//
//        while(!testD.isEmpty())
//        {
//            System.out.println(testD.removeLast());
//        }
//
        testD = new Deque();
        for(int i=0;i<20;i++)
        {
            testD.addLast("test" + i);
            System.out.println(testD.size());
        }
        for(Object x : testD)
        {
            System.out.println(x);
        }
        int counter = 0;
        while(!testD.isEmpty())
        {
            counter ++;
            System.out.println(testD.size());
            System.out.println(testD.removeFirst());
            System.out.println(counter);
//            testD.removeFirst();
        }
    }
}
