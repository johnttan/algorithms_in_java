public class PercolationStats {
    private Percolation percolation;
    private int gridSize;
    private int numTests;

    private double[] testResults;

    private double stdev;
    private double confidenceLo;
    private double confidenceHi;

    PercolationStats(int N, int T) throws IllegalArgumentException {
        if(N <= 0 || T <= 0){
            throw new IllegalArgumentException();
        }
        gridSize = N;
        numTests = T;
        testResults = new double[T];
        for (int i = 0; i < T; i++) {
            runTest(i);
        }

    }

    private int[] pickBlockedSite()
    {
        int x;
        int y;
        do{
            x = StdRandom.uniform(gridSize) + 1;
            y = StdRandom.uniform(gridSize) + 1;
        }while(percolation.isOpen(x, y));

        return new int[] {x, y};
    }

    private void runTest(int testNum)
    {
        double open = 0;
        double openFraction;

        percolation = new Percolation(gridSize);
        while(!percolation.percolates()){
            int[] coords = pickBlockedSite();
            percolation.open(coords[0], coords[1]);
            open ++;
        }

        openFraction = open / (gridSize * gridSize);
        testResults[testNum] = openFraction;
    }

    public double mean()
    {
        return StdStats.mean(testResults);
    }

    public double stddev()
    {
        return StdStats.stddev(testResults);
    }

    public double confidenceLo()
    {
        return mean() - ((1.96 * stddev()) / Math.sqrt(numTests));
    }

    public double confidenceHi()
    {
        return mean() + ((1.96 * stddev()) / Math.sqrt(numTests));

    }

    public static void main(String[] args){
        PercolationStats percStatsTest = new PercolationStats(200, 100);
        System.out.println(percStatsTest.mean());
        System.out.println(percStatsTest.stddev());
        System.out.println(percStatsTest.confidenceLo());
        System.out.println(percStatsTest.confidenceHi());
    }
}
