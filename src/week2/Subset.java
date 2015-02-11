
public class Subset {
    public static void main(String[] args)
    {
        RandomizedQueue testQueue = new RandomizedQueue();
        while(!StdIn.isEmpty()){
            testQueue.enqueue(StdIn.readString());
        }
        for(int i=0;i<Integer.parseInt(args[0]);i++){
            System.out.println(testQueue.dequeue());
        }
    }
}
