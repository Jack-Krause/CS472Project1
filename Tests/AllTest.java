package edu.iastate.cs472.proj1.Tests;

import edu.iastate.cs472.proj1.Move;
import edu.iastate.cs472.proj1.State;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AllTest {

    @Test
    void test1() {
        int[][] s = {{2, 0, 3}, {1, 8, 4}, {7, 6, 5}};
        State testState = new State(s);

        assertTrue(testState.solvable());
        System.out.println(testState.solvable());

        testState.successorState(Move.RIGHT);
        testState.successorState(Move.UP);
        testState.successorState(Move.LEFT);

        assertTrue(testState.isGoalState());
        System.out.println("GOAL " + testState.isGoalState());
    }

}
