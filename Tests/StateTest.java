package edu.iastate.cs472.proj1.Tests;

import edu.iastate.cs472.proj1.State;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {
    private static final int[][] goal = {{1, 2, 3}, {8, 0, 4}, {7, 6, 5}};
    private State goalState;

    @BeforeEach
    void setup() {
        goalState = new State(goal);
    }

    @Test
    @Disabled
    void successorState() {
    }

    @Test
    @Disabled
    void isMoveValid() {
    }

    @Test
    void solvableGoal() {
        assertTrue(goalState.solvable());
    }

    @Test
    @Disabled
    void solvable() {

    }

    @Test
    void inversions() {
        assertEquals(7, goalState.inversions());

    }

    @Test
    void inversionFull() {
        int[][] test1 = {{8, 7, 6}, {5, 4, 3}, {2, 1, 0}};
        int[][] test2 = {{1, 8, 2}, {0, 4, 3}, {7, 6, 5}};
        int[][] test3 = {{8, 1, 2}, {0, 4, 3}, {7, 6, 5}};
        int[][] test4 = {{1, 2, 3}, {4, 0, 6}, {8, 7, 5}};

        State s1 = new State(test1);
        State s2 = new State(test2);
        State s3 = new State(test3);
        State s4 = new State(test4);

        assertEquals(28, s1.inversions());
        assertEquals(10, s2.inversions());
        assertEquals(11, s3.inversions());
        assertEquals(4, s4.inversions());
    }

    @Test
    void solvableFull() {
        int[][] test1 = {{8, 7, 6}, {5, 4, 3}, {2, 1, 0}};
        int[][] test2 = {{1, 8, 2}, {0, 4, 3}, {7, 6, 5}};
        int[][] test3 = {{8, 1, 2}, {0, 4, 3}, {7, 6, 5}};
        int[][] test4 = {{1, 2, 3}, {4, 0, 6}, {8, 7, 5}};

        State s1 = new State(test1);
        State s2 = new State(test2);
        State s3 = new State(test3);
        State s4 = new State(test4);

        assertFalse(s1.solvable());
        assertFalse(s2.solvable());
        assertTrue(s3.solvable());
        assertFalse(s4.solvable());
    }

    @Test
    void isGoalState() {
        assertTrue(goalState.isGoalState());
    }


    @Test
    @Disabled
    void cost() {
    }

    @Test
    @Disabled
    void compareTo() {
    }
}