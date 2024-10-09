package edu.iastate.cs472.proj1;

import java.io.FileNotFoundException;
import java.io.File;
import java.sql.SQLOutput;
import java.util.Scanner;

/**
 *  
 * @author
 *
 */

public class PuzzleSolver 
{

	public static void main(String[] args) {
		int[][] s = {{2, 0, 3}, {1, 8, 4}, {7, 6, 5}};
		State testState = new State(s);

		testState.successorState(Move.RIGHT);
		testState.successorState(Move.UP);
		testState.successorState(Move.LEFT);

	}


}
