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
        private Board board;
        private int priority;
        private SearchNode previous;
        private int count;

        SearchNode(Board grid, int numMoves)
        {
            board = grid;
            priority = grid.hamming() + numMoves;
            previous = null;
            count = numMoves;
        }

        public void assignPrevious(SearchNode prev)
        {
            previous = prev;
        }

        public int getCount()
        {
            return count;
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
            return board;
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

        queueOne.insert(searchOne);
        queueTwo.insert(searchTwo);

        while(!searchOne.getBoard().isGoal() && !searchTwo.getBoard().isGoal()){
            if(!queueOne.isEmpty()){
                searchOneCount ++;
                searchOne = queueOne.delMin();
                for(Board board : searchOne.getBoard().neighbors()) {
                    if(!(searchOne.getPrevious() != null && board.equals(searchOne.getPrevious().getBoard()))) {
                        SearchNode currentOne = new SearchNode(board, searchOneCount);
                        currentOne.assignPrevious(searchOne);
                        queueOne.insert(currentOne);
                    }
                }
            }

            if(!queueTwo.isEmpty()){
                searchTwoCount ++;
                searchTwo = queueTwo.delMin();
                for(Board board : searchTwo.getBoard().neighbors()){
                    if(!(searchTwo.getPrevious() != null && board.equals(searchTwo.getPrevious().getBoard()))){
                        SearchNode currentTwo = new SearchNode(board, searchTwoCount);
                        currentTwo.assignPrevious(searchTwo);
                        queueOne.insert(currentTwo);
                    }
                }
            }
        }
    }

    public boolean isSolvable()
    {
        return searchOne.getBoard().isGoal();
    }

    public int moves()
    {
        if(!isSolvable()){
            return -1;
        }else{
            return searchOne.getCount();
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

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
