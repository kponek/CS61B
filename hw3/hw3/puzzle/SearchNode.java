package hw3.puzzle;

/**
 * Created by kevin on 3/23/2017.
 */
public class SearchNode implements Comparable{
    WorldState state;
    int movesFromInitial;
    SearchNode prev;

    public SearchNode(WorldState w, int moves, SearchNode pr) {
        state = w;
        movesFromInitial = moves;
        prev = pr;
    }
    public SearchNode(WorldState w, int moves) {
        this(w,moves,null);
    }
    public SearchNode(WorldState w) {
        this(w,0,null);
    }
    public int compareTo(Object x) {
        if ((this.movesFromInitial + this.state.estimatedDistanceToGoal())
                < (((SearchNode)x).movesFromInitial + ((SearchNode) x).state.estimatedDistanceToGoal())) {
            return -1;
        } else if ((this.movesFromInitial + this.state.estimatedDistanceToGoal())
                > (((SearchNode)x).movesFromInitial + ((SearchNode) x).state.estimatedDistanceToGoal())) {
            return 1;
        } else {
            return 0;
        }
    }
}
