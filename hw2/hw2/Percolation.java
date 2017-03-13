package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.HashSet;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF connections;
    private int open;
    private int virtualTop;
    private int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("ERROR: n is less than or equal to 1");
        }
        grid = new boolean[n][n];
        connections = new WeightedQuickUnionUF(n * n + 2);
        int bottoms = n * (n - 1);
        virtualTop = n * n;
        virtualBottom = n * n + 1;
        //top connected to -1
       /* for (int i = 0; i < n; i++) {
            connections.union(virtualTop, i);
        }
        //bottom connected to -2
        for (int j = bottoms; j < (n * n); j++) {
            connections.union(virtualBottom, j);
        }*/
    }

    public Percolation(boolean[][] gr) {
        grid = gr;
    }

    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            open++;
            grid[row][col] = true;
            int num = gridNumber(row, col);
            if (row == 0) {
                connections.union(virtualTop, num);
            }
            if (row == grid.length - 1) {
                connections.union(virtualBottom, num);
            }
            if (num > grid.length && isOpen(num - grid.length)) {
                connections.union(num - grid.length, num);
            }
            if (num < grid.length * (grid.length - 1)
                    && isOpen(num + grid.length)) {
                connections.union(num + grid.length, num);
            }
            if (num % grid.length != 0 && isOpen(num - 1)) {
                connections.union(num - 1, num);
            }
            if (num % grid.length != grid.length - 1 && isOpen(num + 1)) {
                connections.union(num + 1, num);
            }
        }

    }

    public boolean isOpen(int row, int col) {
        return grid[row][col];
    }

    private boolean isOpen(int n) {
        return grid[numberToRow(n)][numberToCol(n)];
    }

    public boolean isFull(int row, int col) {
        int num = gridNumber(row, col);
        return (connections.connected(num, virtualTop) && isOpen(row, col));
    }

    private boolean isFull(int n) {
        return connections.connected(n, virtualTop);
    }

    public int numberOfOpenSites() {
        return open;
    }

    public boolean percolates() {
        return connections.connected(virtualTop, virtualBottom);
    }

    //return the grid number based on row and column integer values
    private int gridNumber(int r, int c) {
        return r * grid.length + c;
    }

    private int numberToCol(int n) {
        int c = 0;
        while (n > grid.length) {
            c++;
            n = n - grid.length;
        }
        return c;
    }

    private int numberToRow(int n) {
        return n % grid.length;
    }

    public static void main(String[] args) {
        boolean[][] grid = new boolean[4][4];
    }

}                       
