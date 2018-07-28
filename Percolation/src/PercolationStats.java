import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Smolin on 18.11.2016.
 */
public class PercolationStats {
    private static final double CONFIDENCE_INTERVAL_KOEF_95 = 1.96;
    private int[] trialLimit;
    private int trials;
    private int size;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        trialLimit = new int[trials];
        size = n;
        for (int i = 0; i < trials; i++) {
            Percolation percolator = new Percolation(n);

            int x = 0;
            while (!percolator.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!percolator.isOpen(row, col)) {
                    x++;
                    percolator.open(row, col);
                }
            }
            trialLimit[i] = x;
        }
    }

    // sample standard deviation of percolation threshold
    public double mean() {
        double mean = 0;
        for (int i = 0; i < trials; i++) {
            mean += trialLimit[i];
        }
        mean /= size * size * trials;
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double m = mean();
        double std = 0;
        double sqrSize = size * size;
        for (int i = 0; i < trials; i++) {
            std += Math.pow(m - trialLimit[i] / sqrSize, 2);
        }
        std /= trials - 1;
        return Math.sqrt(std);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        // StdStats
        return mean() - CONFIDENCE_INTERVAL_KOEF_95 * stddev() / Math.sqrt((double) trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_INTERVAL_KOEF_95 * stddev() / Math.sqrt((double) trials);
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = 200;//StdIn.readInt();
        int trials = 100;//StdIn.readInt();
        PercolationStats perc = new PercolationStats(n, trials);
        StdOut.printf("mean                    = %f\n", perc.mean());
        StdOut.printf("stddev                  = %f\n", perc.stddev());
        StdOut.printf("95 confidence interval  = %f  %f\n", perc.confidenceLo(), perc.confidenceHi());
    }
}
