import java.util.ArrayDeque;

public class Board {
    private int[][] board;
    private int N;
    private int hammingDistance;
    private int manhattanDistance;
    private int[][]twinGrid;
    private ArrayDeque<Board>neighborsQueue;
    

    public Board(int[][] blocks) throws NullPointerException
    {
        if(blocks == null){
            throw new NullPointerException();
        }
        board = blocks;
        N = board.length;

        hammingDistance = 0;
        manhattanDistance = 0;

        int[] swap1 = null;
        int[] swap2 = null;
        twinGrid = new int[N][N];

//      Iterate through and precompute swaps, hamming distance, and manhattan distance.
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                twinGrid[i][j] = board[i][j];
                int realX = board[i][j] % N;
                int realY = board[i][j] / N;
                if(i != realX || j != realY){
                    hammingDistance ++;
                }
                manhattanDistance += Math.abs(realX - i) + Math.abs(realY - j);

                if(swap1 == null && board[i][j] != 0){
                    swap1 = new int[2];
                    swap1[0] = i;
                    swap1[1] = j;
                }else if(swap2 == null && board[i][j] != 0){
                    swap2 = new int[2];
                    swap2[0] = i;
                    swap2[1] = j;
                }
            }
        }
//      Precompute swaps for twin.
        twinGrid[swap1[0]][swap1[1]] = board[swap2[0]][swap2[1]];
        twinGrid[swap2[0]][swap2[1]] = board[swap1[0]][swap1[1]];


    }

    public int dimension()
    {
        return N;
    }

    public int hamming()
    {
        return hammingDistance;
    }

    public int manhattan()
    {
        return manhattanDistance;
    }

    public boolean isGoal()
    {
        return hamming() == 0;
    }

    public Board twin()
    {
        return new Board(twinGrid);
    }

    public boolean equals(Object y)
    {
        if(y == this){
            return true;
        }
        if(y == null){
            return false;
        }
        if(y.getClass() != this.getClass()){
            return false;
        }
        Board that = (Board) y;
        return that.toString().equals(toString());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public Iterable<Board> neighbors()
    {

    }


    public static void main(String[] args)
    {
        int[][] testGrid = new int[3][3];
        for(int i=0;i<testGrid.length;i++){
            for(int j=0;j<testGrid.length;j++){
                testGrid[i][j] = 3 * 3 - (i * testGrid.length + j) - 1;
//                System.out.print(testGrid[i][j]);
            }
//            System.out.println("");
        }
        Board testBoard = new Board(testGrid);

        System.out.println(testBoard.manhattan());
        System.out.println(testBoard.hamming());
        System.out.println(testBoard.toString());
        System.out.println(testBoard.twin().toString());


    }
}
