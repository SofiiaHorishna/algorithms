/* *****************************************************************************
 *  Name: Sofiia Horishna
 *  Date: 12.12.2019
 *  Description: Algorithms Part 1 - Week 1: Percolation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final int n;
    private final int trials;
    private final double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        argumentSanityCheck(n, trials);

        this.n = n;
        this.trials = trials;
        this.thresholds = new double[this.trials];

        for (int i = 0; i < this.trials; i++) {
            thresholds[i] = performExperiment();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    private double performExperiment() {
        Percolation percolation = new Percolation(n);

        while (!percolation.percolates()) {
            int i = StdRandom.uniform(1, n + 1);
            int j = StdRandom.uniform(1, n + 1);

            percolation.open(i, j);
        }

        return percolation.numberOfOpenSites() / Math.pow(n, 2);
    }

    private static void argumentSanityCheck(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        StdOut.printf("mean = %f\n", percolationStats.mean());
        StdOut.printf("stddev = %f\n", percolationStats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]",
                      percolationStats.confidenceLo(),
                      percolationStats.confidenceHi());
    }

}
