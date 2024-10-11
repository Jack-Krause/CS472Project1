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
		int[][] test1 = {{8, 7, 6}, {5, 4, 3}, {2, 1, 0}};
		State s = new State(test1);

		String res = EightPuzzle.solve8Puzzle(s);
		System.out.println(res);

	}


}
