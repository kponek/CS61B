package hw3.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    int[][] game;
    private final int BLANK = 0;

    public Board(int[][] tiles) {
        game = tiles;
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i >= game.length || j < 0 || j >= game.length) {
            throw new IndexOutOfBoundsException();
        }
        return game[i][j];
    }

    public int size() {
        return game.length;
    }

    /* Taken from Josh Hug's solution */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[i].length; j++) {
                if (game[i][j] != (1 + j + i * game.length)) {
                    count++;
                }
            }
        }
        return count;
    }

    public int manhattan() {
        int count = 0;
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[i].length; j++) {
                int goal = (1 + j + i * game.length);
                if (game[i][j] != goal && game[i][j] != 0) {
                    int curr = game[i][j];
                    curr--;
                    int row = curr / game.length;
                    int col = curr % game.length;
                    count += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        return count;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean isGoal() {
        return this.equals(isGoal());
    }

    public boolean equals(Object y) {
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[i].length; j++) {
                if (this.game[i][j] != ((Board) y).game[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
