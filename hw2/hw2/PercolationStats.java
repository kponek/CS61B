package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private Percolation grid;
    private double[] fractions;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("ERROR: N or T less than 0");
        }
        fractions = new double[T];
        for (int i = 0; i < T; i++) {
            grid = new Percolation(N);
            while (!grid.percolates()) {
                grid.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            fractions[i] = (double) grid.numberOfOpenSites() / (N * N);
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (1.96 * stddev() / Math.sqrt(fractions.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / Math.sqrt(fractions.length));
    }
}                       
