package edu.iastate.cs472.proj1;

import java.io.FileNotFoundException;
import java.io.File;
import java.sql.SQLOutput;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 *  
 * @author Jack Krause
 *
 */

public class PuzzleSolver 
{

	/**
	 * NOTE TO USER: please change the string where noted to try different input file.
	 * Also, feel free to insert states manually
	 */
	public static void main(String[] args) {
		// User can also manually create states, such as the following:
		//int[][] test1 = {{8, 7, 6}, {5, 4, 3}, {2, 1, 0}};
		State s = new State();

		try {
			// change this string (inputFileName) as needed
			s = new State("8puzzle.txt");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			exit(-1);
		}

		String res = EightPuzzle.solve8Puzzle(s);
		System.out.println(res);


		int[][] noSolArr = {{4, 1, 2}, {5, 3, 0}, {8, 6, 7}};
		State sNoSol = new State(noSolArr);

		String failedRes = EightPuzzle.solve8Puzzle(sNoSol);
		System.out.println(failedRes);
	}

}
