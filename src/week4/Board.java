public class Board {
    private int[][] board;
    private int N;
    public Board(int[][] blocks) throws NullPointerException
    {
        if(blocks == null){
            throw new NullPointerException();
        }
        board = blocks;
        N = board.length;
    }

    public int dimension()
    {
        return N;
    }

    public int hamming()
    {
        int count = 0;
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                int realX = board[i][j] % N;
                int realY = board[i][j] / N;
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
        for(int i=0;i<N;i++) {
            for (int j = 0; j < N; j++) {
                int realX = board[i][j] % N;
                int realY = board[i][j] / N;
                count += Math.abs(realX - i) + Math.abs(realY - j);
            }
        }
        return count;
    }

    public boolean isGoal()
    {
        return hamming() == 0;
    }

    public Board twin()
    {

    }

    public boolean equals(Object y)
    {
        if(y == this){
            return true;
        }
        if(y == null){
            return false;
        }
        if(x.getClass() != this.getClass()){
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

    public static void main(String[] args)
    {

    }
}
