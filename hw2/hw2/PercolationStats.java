package hw2;                       

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    int[] C;

    /** perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T) {
        C = new int[T];
        for (int i = 0; i < T; i++) {
            int count = 0;
            Percolation p = new Percolation(N);
            while (!p.percolates()) {
                int rdmRow = StdRandom.uniform(N);
                int rdmCol = StdRandom.uniform(N);
                try {
                    p.open(rdmRow, rdmCol);
                    count++;
                } catch (Exception e) {
                    continue;
                }

            }
            C[i] = count;
        }
    }

    /** sample mean of percolation threshold */
    public double mean() {
        return StdStats.mean(C);

    }

    /** sample standard deviation of percolation threshold */
    public double stddev() {
        return StdStats.stddev(C);

    }

    /** low endpoint of 95% confidence interval */
    public double confidenceLow() {
        return mean() - 1.96 * Math.sqrt(stddev() / C.length);
    }

    /** high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        return mean() + 1.96 * Math.sqrt(stddev() / C.length);
    }

}                       
