package edu.iastate.cs472.proj1;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static java.lang.System.in;

/**
 * @author
 */

public class EightPuzzle {
    /**
     * This static method solves an 8-puzzle with a given initial state using three heuristics. The
     * first two, allowing single moves only, compare the board configuration with the goal configuration
     * by the number of mismatched tiles, and by the Manhattan distance, respectively.  The third
     * heuristic, designed by yourself, allows double moves and must be also admissible.  The goal
     * configuration set for all puzzles is
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * @param s0
     * @return a string specified in the javadoc below
     */
    public static String solve8Puzzle(State s0) {
        // TODO

        // 1) If there exists no solution, return a message that starts with "No solution
        //    exists for the following initial state:" and follows with a blank line and
        //    then what would be the output from a call s0.toString(). See the end of
        //    Section 6 in the project description for an example.
        System.out.println("8 puzzle is solvable: " + s0.solvable());

        if (!s0.solvable()) {
            return "No solution exists for the following initial state:\n" + s0.toString();
        }

        // 2) Otherwise, solve the puzzle with the three heuristics.  The two solutions generated by
        //    the first two heuristics may be different but must have the same length for optimality.

        Heuristic h[] = {Heuristic.TileMismatch, Heuristic.ManhattanDist, Heuristic.DoubleMoveHeuristic};
        String[] moves = new String[3];

        for (int i = 0; i < 3; i++) {
            moves[i] = AStar(s0, h[i]);
        }

        // 3) Combine the three solution strings into one that would print out in the
        //    output format specified in Section 6 of the project description.

        return null;
    }


    /**
     * This method implements the A* algorithm to solve the 8-puzzle with an input initial state s0.
     * The algorithm implementation is described in Section 3 of the project description.
     * <p>
     * Precondition: the puzzle is solvable with the initial state s0.
     *
     * @param s0 initial state
     * @param h  heuristic
     * @return solution string
     */
    public static String AStar(State s0, Heuristic h) {

        // TODO

        // Initialize the two lists used by the algorithm.
        OrderedStateList OPEN = new OrderedStateList(h, true);
        OrderedStateList CLOSE = new OrderedStateList(h, false);


        // Implement the algorithm described in Section 3 to solve the puzzle.
        // Once a goal state s is reached, call solutionPath(s) and return the solution string.
        OPEN.addState(s0);
        int g_s = 0;
        int h_s = s0.cost();

        while (OPEN.size() > 0) {
            State s = OPEN.remove();
            CLOSE.addState(s);
            if (s.isGoalState()) {
                //TODO: add string
                return "SUCCESS";
            }

            Move[] moves = Move.values();
            Move[] usableMoves = {Move.UP, Move.DOWN, Move.LEFT, Move.RIGHT};

            if (h == Heuristic.DoubleMoveHeuristic) {
                usableMoves = moves;
            }

            for (Move m : moves) {
                System.out.println(m);

                try {
                    State t = s.successorState(m);

                    if (t != null && !t.equals(s.predecessor)) {
                        int f = t.cost();

                        State onC = CLOSE.findState(t);
                        State onO = OPEN.findState(t);

                        if (onC == null && onO == null) {
                            OPEN.addState(t);
                        } else if (onO != null) {
                            int oldF = onO.cost();

                            if (f < oldF) {
                                onO.predecessor = s;
                            }
                        } else {
                            int oldF = onC.cost();

                            if (f < oldF) {
                                OPEN.addState(onC);
                                onC.predecessor = s;
                            }
                        }
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("illegal move, continue");
                    continue;
                }


            }

        }


        return null;

    }


    /**
     * From a goal state, follow the predecessor link to trace all the way back to the initial state.
     * Meanwhile, generate a string to represent board configurations in the reverse order, with
     * the initial configuration appearing first. Between every two consecutive configurations
     * is the move that causes their transition. A blank line separates a move and a configuration.
     * In the string, the sequence is preceded by the total number of moves and a blank line.
     * <p>
     * See Section 6 in the projection description for an example.
     * <p>
     * Call the toString() method of the State class.
     *
     * @param goal
     * @return
     */
    private static String solutionPath(State goal) {
        // TODO

        return null;
    }


}
