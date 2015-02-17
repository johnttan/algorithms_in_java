import java.util.ArrayDeque;
import java.util.ArrayList;

public class Board {
    private int[][] board;
    private int N;
    private int hammingDistance;
    private int manhattanDistance;
    private int[][]twinGrid;
    private ArrayDeque<Board>neighborsQueue = null;
    private ArrayList<int[][]>neighborsList;

    private int[][] copyBoard(int[][] blocks){
        int[][] result = new int[blocks.length][blocks.length];
        for(int i=0;i<blocks.length;i++){
            for(int j=0;j<blocks.length;j++){
                result[i][j] = blocks[i][j];
            }
        }
        return result;
    }

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
        neighborsList = new ArrayList<int[][]>();

//      Iterate through and precompute swaps, neighbors, hamming distance, and manhattan distance.
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                twinGrid[i][j] = board[i][j];
//                System.out.println(String.format("At (%d, %d) %d)", i, j, board[i][j]));
                if(board[i][j] != 0){
                    int realY = (board[i][j]-1) % N;
                    int realX = (board[i][j]-1) / N;
//                    System.out.print(String.format("%d (%d %d)->(%d %d) ", board[i][j], i, j, realX, realY));
                    if(i != realX || j != realY){
                        hammingDistance ++;
                    }
                    manhattanDistance += Math.abs(realX - i) + Math.abs(realY - j);

                    if(swap1 == null){
                        swap1 = new int[2];
                        swap1[0] = i;
                        swap1[1] = j;
                    }else if(swap2 == null){
                        swap2 = new int[2];
                        swap2[0] = i;
                        swap2[1] = j;
                    }
                }else{
//                    System.out.println(String.format("\nCOMPUTING NEIGHBORS: %d, %d, %d", i, j, board[i][j]));
                    if(i + 1 < N){
                        int[][] neighbor1 = copyBoard(board);
                        neighbor1[i][j] = board[i+1][j];
                        neighbor1[i+1][j] = board[i][j];
                        neighborsList.add(neighbor1);
                    }
                    if(j + 1 < N){
                        int[][] neighbor2 = copyBoard(board);
                        neighbor2[i][j] = board[i][j+1];
                        neighbor2[i][j+1] = board[i][j];
                        neighborsList.add(neighbor2);
                    }
                    if(i - 1 >= 0){
                        int[][] neighbor3 = copyBoard(board);
                        neighbor3[i][j] = board[i-1][j];
                        neighbor3[i-1][j] = board[i][j];
                        neighborsList.add(neighbor3);
                    }
                    if(j - 1 >= 0){
                        int[][] neighbor4 = copyBoard(board);
                        neighbor4[i][j] = board[i][j-1];
                        neighbor4[i][j-1] = board[i][j];
                        neighborsList.add(neighbor4);
                    }
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
        if(neighborsQueue != null){
            return neighborsQueue;
        }else{
            neighborsQueue = new ArrayDeque<Board>();
            for(int[][] grid : neighborsList){
                neighborsQueue.add(new Board(grid));
            }
            return neighborsQueue;
        }

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
        System.out.println("\nMANHATTAN");
        System.out.println(testBoard.manhattan());
        assert testBoard.manhattan() == 20 : "manhattan should be 20";
        assert testBoard.hamming() == 7: "hamming should be 7";
        System.out.println("HAMMING");
        System.out.println(testBoard.hamming());
        System.out.println(testBoard.toString());
//        System.out.println(testBoard.twin().toString());

        for(Board neighbor : testBoard.neighbors()){
            System.out.println(neighbor.toString());
        }

        testGrid = new int[3][3];
        for(int i=0;i<testGrid.length;i++){
            for(int j=0;j<testGrid.length;j++){
                testGrid[i][j] = (i * testGrid.length + j) + 1;
                if(i == testGrid.length-1 && j == testGrid.length-1){
                    testGrid[i][j] = 0;
                }
                System.out.print(testGrid[i][j]);
            }
            System.out.println("");
        }
        testBoard = new Board(testGrid);
        StdOut.println(testBoard.isGoal());
    }
}
