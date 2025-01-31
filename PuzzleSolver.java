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

	public static void main(String[] args) {
		int[][] test1 = {{8, 7, 6}, {5, 4, 3}, {2, 1, 0}};
		State s = new State();

		try {
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
