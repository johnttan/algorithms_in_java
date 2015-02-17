public class Board {
    private int[][] board;

    public Board(int[][] blocks)
    {
        board = blocks;
    }

    public int dimension()
    {

    }

    public int hamming()
    {
        int count = 0;
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board.length;j++){
                int realX = board[i][j] % board.length;
                int realY = board[i][j] / board.length;
                if(i != realX || j != realY){
                    count ++;
                }
            }
        }
        return count;
    }

    public int manhattan()
    {
        int count = 0;
        for(int i=0;i<board.length;i++) {
            for (int j = 0; j < board.length; j++) {
                int realX = board[i][j] % board.length;
                int realY = board[i][j] / board.length;
                count += Math.abs(realX - i) + Math.abs(realY - j);
            }
        }
        return count;
    }

    public boolean isGoal()
    {

    }

    public Board twin()
    {

    }

    public boolean equals(Object y)
    {

    }

    public String toString()
    {

    }

    public static void(String[] args)
    {

    }





}
