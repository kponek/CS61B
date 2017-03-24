package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;

/**
 * Created by kevin on 3/17/2017.
 */
public class Solver {
    private int numMoves;
    private ArrayList<WorldState> solution;

    public Solver(WorldState initial) {
        //initialize everything
        MinPQ<SearchNode> pq = new MinPQ<>();
        numMoves = 0;
        SearchNode sn = new SearchNode(initial);
        WorldState check = initial;
        //pq.insert(sn);
        solution = new ArrayList<>();
        solution.add(sn.state);
        //A*
        while (!check.isGoal()) {
            for (WorldState w : check.neighbors()) {
                SearchNode neighbor = new SearchNode(w, numMoves + 1, sn);
                if (sn.prev != w && !solution.contains(w)) {
                    //!solution.contains(w)
                    pq.insert(neighbor);
                }
            }
            sn = pq.delMin();
            check = sn.state;
            solution.add(check);
            numMoves++;
        }
    }

    public int moves() {
        return numMoves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }
}
