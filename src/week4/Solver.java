public class Solver {
    private MinPQ<SearchNode> queue;

    private SearchNode searchOne;
    private SearchNode searchTwo;

    private class SearchNode implements Comparable<SearchNode> {
        private Board grid;
        private int priority;
        private SearchNode previous;

        SearchNode(Board grid, int numMoves)
        {
            board = grid;
            priority = grid.hamming() + numMoves;
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

    }

    public boolean isSolvable()
    {

    }

    public int moves()
    {

    }

    public Iterable<Board> solution()
    {

    }

    public static void main(String[] args)
    {

    }

}
