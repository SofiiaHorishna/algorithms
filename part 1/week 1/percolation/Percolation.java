/* *****************************************************************************
 *  Name: Sofiia Horishna
 *  Date: 12.12.2019
 *  Description: Algorithms Part 1 - Week 1: Percolation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final int gridSize;
    private final int virtualTopSite;
    private final int virtualBottomSite;
    private final WeightedQuickUnionUF uf;

    private boolean[] grid;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        argumentSanityCheck(n);

        this.n = n;
        this.gridSize = n * n;
        this.virtualTopSite = gridSize;
        this.virtualBottomSite = gridSize + 1;
        this.numberOfOpenSites = 0;
        this.grid = new boolean[gridSize];
        this.uf = new WeightedQuickUnionUF(gridSize + 2);

        for (int i = 0; i < gridSize; i++) {
            grid[i] = false;
        }

        // connect first row to virtual top site
        for (int i = 0; i < n; i++) {
            uf.union(virtualTopSite, i);
        }

        // connect last row to virtual bottom site
        for (int i = gridSize - 1; i >= gridSize - n; i--) {
            uf.union(virtualBottomSite, i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        argumentSanityCheck(row, col);

        if (isOpen(row, col)) {
            return;
        }

        int siteIndex = getSiteIndex(row, col);
        grid[siteIndex] = true;
        numberOfOpenSites++;

        connectToAdjacentOpenSites(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        argumentSanityCheck(row, col);

        int siteIndex = getSiteIndex(row, col);
        return grid[siteIndex];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int siteIndex = getSiteIndex(row, col);
        return isOpen(row, col) && uf.connected(siteIndex, virtualTopSite);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(virtualTopSite, virtualBottomSite);
    }

    private void connectToAdjacentOpenSites(int row, int col) {
        int siteIndex = getSiteIndex(row, col);

        int topRow = row - 1;
        int topSite = getSiteIndex(topRow, col);

        int bottomRow = row + 1;
        int bottomSite = getSiteIndex(bottomRow, col);

        int leftCol = col + 1;
        int leftSite = getSiteIndex(row, leftCol);

        int rightCol = col - 1;
        int rightSite = getSiteIndex(row, rightCol);

        if (rowColInRange(topRow, col) && isOpen(topRow, col)) {
            uf.union(siteIndex, topSite);
        }

        if (rowColInRange(bottomRow, col) && isOpen(bottomRow, col)) {
            uf.union(siteIndex, bottomSite);
        }

        if (rowColInRange(leftCol, row) && isOpen(row, leftCol)) {
            uf.union(siteIndex, leftSite);
        }

        if (rowColInRange(rightCol, row) && isOpen(row, rightCol)) {
            uf.union(siteIndex, rightSite);
        }
    }

    private void argumentSanityCheck(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private void argumentSanityCheck(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
    }

    private boolean rowColInRange(int row, int col) {
        return row > 0 && col > 0 && row <= n && col <= n;
    }

    private int getSiteIndex(int row, int col) {
        return n * (row - 1) + (col - 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);

        percolation.connectToAdjacentOpenSites(3, 5);

        for (int i = 0; i <= 26; i++) {
            int site = percolation.uf.find(i);
            StdOut.println(site);
        }
    }
}
