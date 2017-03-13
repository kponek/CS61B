package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.HashSet;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF connections;
    private WeightedQuickUnionUF full;
    private int open;
    private int virtualTop;
    private int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("ERROR: n is less than or equal to 1");
        }
        grid = new boolean[n][n];
        connections = new WeightedQuickUnionUF(n * n + 2);
        full = new WeightedQuickUnionUF(n * n + 1);
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
                full.union(virtualTop, num);
                connections.union(virtualTop, num);
            }
            //don't union full here because this is the backwash fix
            if (row == grid.length - 1) {
                connections.union(virtualBottom, num);
            }
            if (row > 0 && isOpen(num - grid.length)) {
                full.union(num - grid.length, num);
                connections.union(num - grid.length, num);
            }
            if (row < (grid.length - 1)
                    && isOpen(num + grid.length)) {
                full.union(num + grid.length, num);
                connections.union(num + grid.length, num);
            }
            if (col != 0 && isOpen(num - 1)) {
                full.union(num - 1, num);
                connections.union(num - 1, num);
            }
            if ((col != grid.length - 1) && isOpen(num + 1)) {
                full.union(num + 1, num);
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
        return (full.connected(num, virtualTop) && isOpen(row, col));
    }

    private boolean isFull(int n) {
        return full.connected(n, virtualTop) && isOpen(numberToRow(n), numberToCol(n));
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
        return n % grid.length;
    }

    private int numberToRow(int n) {
        int c = 0;
        while (n >= grid.length) {
            c++;
            n = n - grid.length;
        }
        return c;
    }

    public static void main(String[] args) {
        Percolation grid = new Percolation(3);
        grid.open(0, 0);
        grid.open(1, 1);

    }

}                       
