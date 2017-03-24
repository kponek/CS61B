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
        MinPQ pq = new MinPQ();
        numMoves = 0;
        SearchNode sn = new SearchNode(initial);
        //pq.insert(sn);
        solution = new ArrayList<>();
        solution.add(sn.state);

        //A*
        while (!sn.state.isGoal()) {
            for (WorldState w : sn.state.neighbors()) {
                if (sn.prev != w) {
                    //!solution.contains(w)
                    SearchNode neighbor = new SearchNode(w, numMoves + 1, sn);
                    pq.insert(neighbor);
                }
            }
            sn = (SearchNode) pq.delMin();
            solution.add(sn.state);
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
