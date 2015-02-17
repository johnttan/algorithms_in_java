import java.util.ArrayDeque;

public class Solver {
    private MinPQ<SearchNode> queueOne;
    private MinPQ<SearchNode> queueTwo;

    private SearchNode searchOne;
    private SearchNode searchTwo;

    private int searchOneCount;
    private int searchTwoCount;

    private ArrayDeque<Board> solutionQueue;

    private class SearchNode implements Comparable<SearchNode> {
        private Board grid;
        private int priority;
        private SearchNode previous;

        SearchNode(Board grid, int numMoves)
        {
            board = grid;
            priority = grid.hamming() + numMoves;
            previous = null;
        }

        public void assignPrevious(SearchNode prev)
        {
            previous = prev;
        }

        public SearchNode getPrevious()
        {
            return previous;
        }

        public int getPriority()
        {
            return priority;
        }

        public Board getBoard()
        {
            return grid;
        }

        public int compareTo(SearchNode otherNode)
        {
            if (this.getPriority() == otherNode.getPriority()){
                return 0;
            }else if(this.getPriority() > otherNode.getPriority()){
                return 1;
            }else{
                return -1;
            }
        }
    }

    public Solver(Board initial)
    {
        queueOne = new MinPQ<SearchNode>();
        queueTwo = new MinPQ<SearchNode>();

        searchOne = new SearchNode(initial, 0);
        searchTwo = new SearchNode(initial.twin(), 0);
    }

    public boolean isSolvable()
    {
        return searchOne.getBoard().hamming() == 0;
    }

    public int moves()
    {
        if(!isSolvable()){
            return -1;
        }else{
            return searchOneCount;
        }
    }

    public Iterable<Board> solution()
    {
        if(!isSolvable()){
            return null;
        }else{
            solutionQueue = new ArrayDeque<Board>();
            SearchNode current = searchOne;

            while(current != null){
                solutionQueue.add(current.getBoard());
                current = current.getPrevious();
            }
            return solutionQueue;
        }


    }

    public static void main(String[] args)
    {

    }

}
