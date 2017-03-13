package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.*;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF connections;
    private int open;

    public Percolation(int n) {
        grid = new boolean[n][n];
        connections = new WeightedQuickUnionUF(gridNumber(n, n) + 2);
        int tops = 0;
        int bottoms = n * (n - 1);
        //top connected to -1
        for (int i = 0; i < n; i++) {
            connections.union(-1, i);
        }
        //bottom connected to -2
        for (int j = bottoms; j < (n * n); j++) {
            connections.union(-2, j);
        }
    }

    public Percolation(boolean[][] gr) {
        grid = gr;
    }

    public void open(int row, int col) {
        if (grid[row][col] == false) {
            open++;
            grid[row][col] = true;
            int num = gridNumber(row, col);
            HashSet<Integer> neighbors = new HashSet<>();
            if (num > grid.length) {
                neighbors.add(num - grid.length);
            }
            if (num < grid.length * grid.length) {
                neighbors.add(num + grid.length);
            }
            if (num % grid.length != 0) {
                neighbors.add(num - 1);
            }
            if (num % grid.length != 4) {
                neighbors.add(num + 1);
            }
            for (int i : neighbors) {
                if (isOpen(i)) {
                    connections.union(i, num);
                }
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
        return (connections.connected(num, -1));
    }

    private boolean isFull(int n) {
        return connections.connected(n, -1);
    }

    public int numberOfOpenSites() {
        return open;
    }

    public boolean percolates() {
        return connections.connected(-1, -2);
    }

    //return the grid number based on row and column integer values
    private int gridNumber(int r, int c) {
        return c * grid.length + r % grid.length;
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
