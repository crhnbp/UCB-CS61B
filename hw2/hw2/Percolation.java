package hw2;                       

import java.util.ArrayList;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int dimension;
    private boolean[][] Uni;
    private WeightedQuickUnionUF UFUni;
    private int numOpenSites;
    private int vTopRow;
    private int vBtmRow;

    /** create N-by-N grid, with all sites initially blocked */
    public Percolation(int N) {
        dimension = N;
        Uni = new boolean[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Uni[r][c] = false;
            }
        }
        UFUni = new WeightedQuickUnionUF(N * N + 2);
        numOpenSites = 0;
        vTopRow = N * N ;
        vBtmRow = N * N + 1;
    }

    /** convert 2D array into 1D array */
    private int rcTo1D(int row, int col) {
        return row * dimension + col;
    }

    /** return a list which includes whether there is any adjacent site opened */
    private ArrayList<Integer> isAdjOpened(int row, int col) {
        ArrayList<Integer> result = new ArrayList<>();
        if (col != 0 && isOpen(row, col - 1)) {
            result.add(rcTo1D(row, col - 1));
        }
        if (row != 0 && isOpen(row - 1, col)) {
            result.add(rcTo1D(row - 1, col));
        }
        if (col != (dimension - 1) && isOpen(row, col + 1)) {
            result.add(rcTo1D(row, col + 1));

        }
        if (row != (dimension - 1) && isOpen(row + 1, col)) {
            result.add(rcTo1D(row + 1, col));
        }
        return result;
    }

    /** open the site (row, col) if it is not open already */
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            throw new IllegalArgumentException("Site already opened");
        } else {
            Uni[row][col] = true;
            numOpenSites += 1;
            int UFSite = rcTo1D(row, col);
            if (row == 0) {
                UFUni.union(UFSite, vTopRow);
            }
            ArrayList<Integer> AdjOpened = isAdjOpened(row, col);
            for (int i = 0; i < AdjOpened.size(); i++) {
                UFUni.union(UFSite, AdjOpened.get(i));
            }
            if (row == dimension - 1) {
                if (UFUni.connected(UFSite, vTopRow)) {
                    UFUni.union(UFSite, vBtmRow);
                }
            }
        }
    }

    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        return Uni[row][col];
    }

    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        if (row == 0) {
            return isOpen(row, col);
        }
        return UFUni.connected(vTopRow, rcTo1D(row, col));
        /*
        for (int i = 0; i < dimension; i++) {
            int n = rcTo1D(row, col);
            if (i != n && UFUni.connected(i, n)) {
                return true;
            }
        }
        return false;*/
    }

    /** number of open sites */
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    /** does the system percolate? */
    public boolean percolates() {
        /*
        for (int i = 0; i < dimension; i++) {
            for (int j = dimension * dimension - 1; j >= dimension * (dimension - 1); j--) {
                if (UFUni.connected(i, j)) {
                    return true;
                }
            }
        }
        return false;*/
        return UFUni.connected(vTopRow, vBtmRow);
    }

    /* unit testing (not required) */
    public static void main(String[] args) {
        PercolationStats PS = new PercolationStats(20, 30);
        System.out.println(PS.mean());
    }
}
