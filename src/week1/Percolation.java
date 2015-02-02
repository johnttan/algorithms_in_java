//TODO:
//Fix backwash and N==1 N=2 corner cases.
public class Percolation {
    private boolean[][] grid;
    private int bound;
    private int numNodes;
    private WeightedQuickUnionUF unionFind;
    public Percolation(int N) throws IllegalArgumentException
    {
        if(N <= 0){
            throw new IllegalArgumentException();
        }
        bound = N;
        numNodes = N * N + 2;
        grid = new boolean[N][N];
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                grid[i][j] = false;
            }
        }

        unionFind = new WeightedQuickUnionUF((N * N) + 2);
        //            Initialize virtual top and bottom unions
        for(int i=1;i<=N;i++){
            unionFind.union(0, i);
            unionFind.union(numNodes - 1, numNodes - 1 - i);
        }
    }

    private void unionCoord(int x, int y, int x2, int y2) {
        if (x > 0 && y > 0 && x <= bound && y <= bound && x2 > 0 && y2 > 0 && x2 <= bound && y2 <= bound) {
            if (grid[x2 - 1][y2 - 1]) {
                unionFind.union(bound * (x - 1) + y, bound * (x2 - 1) + y2);
            }
        }
    }

    public boolean percolates()
    {
        return unionFind.connected(0, numNodes - 1);
    }

    public void open(int i, int j) throws IndexOutOfBoundsException
    {
        if(i > bound || i <= 0 || j > bound || j <= 0){
            throw new IndexOutOfBoundsException();
        }
        grid[i-1][j-1] = true;
        unionCoord(i, j, i + 1, j);
        unionCoord(i, j, i - 1, j);
        unionCoord(i, j, i, j + 1);
        unionCoord(i, j, i, j - 1);
    }

    public boolean isOpen(int i, int j) throws IndexOutOfBoundsException
    {
        if(i > bound || i <= 0 || j > bound || j <= 0){
            throw new IndexOutOfBoundsException();
        }
        return grid[i-1][j-1];
    }

    public boolean isFull(int i, int j) throws IndexOutOfBoundsException
    {
        if(i > bound || i <= 0 || j > bound || j <= 0){
            throw new IndexOutOfBoundsException();
        }
        return unionFind.connected(0, bound * (i - 1) + j) && isOpen(i, j);
    }

    public static void main(String args[])
    {
        Percolation testPerc = new Percolation(20);
        testPerc.open(20, 1);
        System.out.println(testPerc.isFull(12, 10));
        testPerc.percolates();
    }
}