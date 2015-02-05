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
    private int sizeNum = 0;

    public Deque()
    {
    }
    public boolean isEmpty()
    {
        return sizeNum == 0;
    }
    public int size()
    {
        return sizeNum;
    }
    public void addFirst(Item item)
    {
        if(head == null){
            head = new Node();
            tail = head;
        }else{
            head.previous = new Node();
            head.previous.next = head;
            head = head.previous;
        }
        head.item = item;
        sizeNum ++;
    }
    public void addLast(Item item)
    {
        if(tail == null){
            tail = new Node();
            head = tail;
        }else{
            tail.next = new Node();
            tail.next.previous = tail;
            tail = tail.next;
        }
        tail.item = item;
        sizeNum ++;
    }
    public Item removeFirst() throws NoSuchElementException
    {
        if(!isEmpty()){
            Item result = head.item;
            if(head.next != null){
                head = head.next;
                head.previous = null;
            }else{
                head = null;
                tail = null;
            }
            sizeNum --;
            return result;
        }else{
            throw new NoSuchElementException();
        }
    }
    public Item removeLast()
    {
        if(!isEmpty()){
            Node result = tail;
            if(tail.previous != null){
                tail = tail.previous;
                tail.next = null;
            }else{
                head = null;
                tail = null;
            }
            sizeNum --;
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
            return current != null;
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
        for(int i=0;i<20;i++)
        {
            testD.addFirst("test" + i);
        }

        while(!testD.isEmpty())
        {
            System.out.println(testD.removeFirst());
        }

        testD = new Deque();
        for(int i=0;i<20;i++)
        {
            testD.addLast("test" + i);
        }

        while(!testD.isEmpty())
        {
            System.out.println(testD.removeLast());
        }
//
        testD = new Deque();
        for(int i=0;i<20;i++)
        {
            testD.addLast("test" + i);
//            System.out.println(testD.size());
        }
        for(Object x : testD)
        {
            System.out.println(x);
        }
        while(!testD.isEmpty())
        {
//            System.out.println(testD.size());
            System.out.println(testD.removeFirst());
//            testD.removeFirst();
        }

        testD = new Deque();

    }
}
