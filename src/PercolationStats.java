public class PercolationStats {
    private Percolation percolation;
    private int gridSize;
    private int numTests;

    private double sumOpenSitesFractions = 0;
    private double stdev;
    private double confidenceLo;
    private double confidenceHi;

    PercolationStats(int N, int T) {
        gridSize = N;
        numTests = T;

        for(int i=0;i<T;i++){
            runTest();
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

    private void runTest()
    {
        int open = 0;
        double openFraction;

        percolation = new Percolation(gridSize);
        while(!percolation.percolates()){
            int[] coords = pickBlockedSite();
            percolation.open(coords[0], coords[1]);
            open ++;
        }

        openFraction = open / (gridSize * gridSize);

        sumOpenSitesFractions += openFraction;
    }

    public double mean()
    {
        return sumOpenSitesFractions / numTests;
    }

    public double stddev()
    {

    }

    public double confidenceLo()
    {

    }

    public double confidenceHi()
    {

    }
}
