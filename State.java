package edu.iastate.cs472.proj1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  
 * @author
 *
 */


/**
 * This class represents a board configuration in the 8-puzzle.  Only the initial configuration is 
 * generated by a constructor, while intermediate configurations will be generated via calling
 * the method successorState().  State objects will form two circular doubly-linked lists OPEN and 
 * CLOSED, which will be used by the A* algorithm to search for a path from a given initial board
 * configuration to the final board configuration below: 
 * 
 *  1 2 3 
 *  8   4
 *  7 6 5
 *
 * The final configuration (i.e., the goal state) above is not explicitly represented as an object 
 * of the State class. 
 */
public class State implements Cloneable, Comparable<State>
{
	public int[][] board; 		// configuration of tiles 
	private static final int[][] goalState = {{1, 2, 3}, {8, 0, 4}, {7, 6, 5}};

	public State previous;    	// previous node on the OPEN/CLOSED list
	public State next; 			// next node on the OPEN/CLOSED list
	public State predecessor; 	// predecessor node on the path from the initial state 
	
	public Move move;           // the move that generated this state from its predecessor
	public int numMoves; 	    // number of moves from the initial state to this state

	public static Heuristic heu; // heuristic used. shared by all the states. 
	
	private int numMismatchedTiles = -1;    // number of mismatched tiles between this state 
	                                        // and the goal state; negative if not computed yet.
	private int ManhattanDistance = -1;     // Manhattan distance between this state and the 
	                                        // goal state; negative if not computed yet. 
	private int numSingleDoubleMoves = -1;  // number of single and double moves with each double 
										    // move counted as one; negative if not computed yet. 

	
	/**
	 * Constructor (for the initial state).  
	 * 
	 * It takes a 2-dimensional array representing an initial board configuration. The empty 
	 * square is represented by the number 0.  
	 * 
	 *     a) Initialize all three links previous, next, and predecessor to null.  
	 *     b) Set move to null and numMoves to zero.
	 * 
	 * @param board
	 * @throws IllegalArgumentException		if board is not a 3X3 array or its nine entries are 
	 * 										not respectively the digits 0, 1, ..., 8. 
	 */
    public State(int[][] board) throws IllegalArgumentException 
    {
    	// TODO
		if (board.length != 3 || board[0].length != 3) {
			throw new IllegalArgumentException("incorrect size");
		}

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (board[r][c] < 0) {
					throw new IllegalArgumentException("incorrect grid value");
				}
			}
		}

		this.board = board;
        this.previous = null;
		this.next = null;
		this.predecessor = null;
		this.move = null;
		this.numMoves = 0;
		System.out.println("state created successfully");
	}
    
    
    /**
     * Constructor (for the initial state) 
     * 
     * It takes a state from an input file that has three rows, each containing three digits 
     * separated by exactly one blank.  Every row starts with a digit. The nine digits are 
     * from 0 to 8 with no duplicates.  
     * 
     * Do the same initializations as for the first constructor. 
     * 
     * @param inputFileName
     * @throws FileNotFoundException
     * @throws IllegalArgumentException  if the file content does not meet the above requirements. 
     */
    public State (String inputFileName) throws FileNotFoundException, IllegalArgumentException
    {
    	
    	// TODO
		int[][] initialBoard = new int[3][3];

		try {
			File f = new File(inputFileName);
			Scanner scnr = new Scanner(f);
			int row = 0;

			while (scnr.hasNextLine()) {
				String l = scnr.nextLine();
				String[] rowArr = l.split(" ");

				for (int i = 0; i < rowArr.length; i++) {
					int cell = Integer.parseInt(rowArr[i]);

					if (cell < 0 || cell > 8) {
						throw new IllegalArgumentException("illegal inputs in the cell");
					}

					initialBoard[row][i] = Integer.parseInt(rowArr[i]);
				}
				++row;
			}
			scnr.close();

			// Set attributes of state
			this.board = initialBoard;
			this.previous = null;
			this.next = null;
			this.predecessor = null;
			this.move = null;
			this.numMoves = 0;
			System.out.println("state created successfully");

		} catch(FileNotFoundException e) {
			System.out.println("could not read file");
			e.printStackTrace();
		}

	}
    
    
    /**
     * Generate the successor state resulting from a given move.  Throw an exception if the move 
     * cannot be executed.  Besides setting the array board[][] properly, you also need to do the 
     * following:
     * 
     *     a) set the predecessor of the successor state to this state;
     *     b) set the private instance variable move of the successor state to the parameter m; 
     *     c) Set the links next and previous to null;  
     *     d) Set the variable numMoves for the successor state to this.numMoves + 1. 
     * 
     * @param m  one of the moves LEFT, RIGHT, UP, DOWN, DBL_LEFT, DBL_RIGHT, DBL_UP, and DBL_DOWN
     * @return null  			if the successor state is this.predecessor
     *         successor state  otherwise 
     * @throws IllegalArgumentException if LEFT when the empty square is in the right column, or  
     *                                  if RIGHT when the empty square is in the left column, or
     *                                  if UP when the empty square is in the bottom row, or 
     *                                  if DOWN when the empty square is in the top row, or
     *                                  if DBL_LEFT when the empty square is not in the left column, or 
     *                                  if DBL_RIGHT when the empty square is not in the right column, or 
     *                                  if DBL_UP when the empty square is not in the top row, or 
     *                                  if DBL_DOWN when the empty square is not in the bottom row. 
     */                                  
    public State successorState(Move m) throws IllegalArgumentException 
    {
    	// TODO 
    	return null; 
    }
    
        
    /**
     * Determines if the board configuration in this state can be rearranged into the goal configuration. 
     * According to the PowerPoint notes that introduce the 8-puzzle, we check if this state has an odd number 
     * of inversions. 
     */
    /**
     * 
     * @return true if the puzzle starting in this state can be rearranged into the goal state.
     */
    public boolean solvable()
    {
    	// TODO 
    	return false; 
    }
    
    
    /**
     * Check if this state is the goal state, namely, if the array board[][] stores the following contents: 
     * 
     * 		1 2 3 
     * 		8 0 4 
     * 		7 6 5 
     * 
     * @return true if the current state is the goal state, false otherwise
     */
    public boolean isGoalState()
    {
    	// TODO
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (this.board[r][c] != goalState[r][c]) {
					return false;
				}
			}
		}
    	return true;
    }
    
    
    /**
     * Write the board configuration according to the following format:
     * 
     *     a) Output row by row in three lines with no indentations.  
     *     b) Two adjacent tiles in each row have exactly one blank in between. 
     *     c) The empty square is represented by a blank.  
     *     
     * For example, 
     * 
     * 2   3
     * 1 8 4
     * 7 6 5  
     * 
     */
    @Override 
    public String toString()
    {
		for (int r = 0; r < 3; r++) {
			StringBuilder strB = new StringBuilder();

			for (int c = 0; c < 3; c++) {
				int cell = this.board[r][c];
				strB.append(" ");

				if (cell == 0) {
					strB.append(" ");
				}
				else {
					strB.append(cell);
				}

				strB.append(" ");
			}

			System.out.println(strB.toString());
		}
    	return null; 
    }
    
    
    /**
     * Create a clone of this State object by copying over the board[][]. Set the links previous,
     * next, and predecessor to null.
     * The method is called by SuccessorState(); 
     */
    @Override
    public Object clone()
    {
		// call constructor - copies the board and sets everything else to defaults
    	return new State(this.board);
    }
  

    /**
     * Compare this state with the argument state.  Two states are equal if their arrays board[][] 
     * have the same content.
     */
    @Override 
    public boolean equals(Object o)
    {
    	// TODO
		o = (State)o;
		for (int c = 0; c < 3; c++) {
			for (int r = 0; r < 3; r++) {
				if (this.board[r][c] != ((State) o).board[r][c]) {
					return false;
				}
			}
		}
    	return true;
    }
        
    
    /**
     * Evaluate the cost of this state as the sum of the number of moves from the initial state and 
     * the estimated number of moves to the goal state using the heuristic stored in the instance 
     * variable heu. 
     * 
     * If heu == TileMismatch, add up numMoves and the return values from computeNumMismatchedTiles().
     * If heu == MahattanDist, add up numMoves and the return values of computeMahattanDistance(). 
     * If heu == DoubleMoveHeuristic, add up numMoves and the return value of computeNumSingleDoubleMoves(). 
     * 
     * @return estimated number of moves from the initial state to the goal state via this state.
     * @throws IllegalArgumentException if heuristic is none of TileMismatch, MahattanDist, DoubleMoveHeuristic. 
     */
    public int cost() throws IllegalArgumentException
    {
    	// TODO 
    	return 0; 
    }

    
    /**
     * Compare two states by the cost. Let c1 and c2 be the costs of this state and the argument state s.
     * 
     * @return -1 if c1 < c2 
     *          0 if c1 = c2 
     *          1 if c1 > c2 
     *          
     * Call the method cost(). This comparison will be used in maintaining the OPEN list by the A* algorithm.
     */
    @Override
    public int compareTo(State s)
    {
    	// TODO 
    	return 0; 
    }
    

    /**
     * Return the value of the private variable numMismatchedTiles if it is non-negative, and compute its 
     * value otherwise. 
     * 
     * @return the number of mismatched tiles between this state and the goal state. 
     */
	private int computeNumMismatchedTiles()
	{
		// TODO 
		return 0; 
	}

	
	/**
	 * Return the value of the private variable ManhattanDistance if it is non-negative, and compute its value 
	 * otherwise.
	 * 
	 * @return the Manhattan distance between this state and the goal state. 
	 */
	private int computeManhattanDistance()
	{
		// TODO 
		return 0; 
	}
	
	
	/**
	 * Return the value of the private variable numSingleDoubleMoves if it is non-negative, and compute its value 
	 * otherwise. 
	 * 
	 * @return the value of the private variable numSingleDoubleMoves that bounds from below the number of moves, 
	 *         single or double, which will take this state to the goal state.
	 */
	private int computeNumSingleDoubleMoves()
	{
		// TODO 
		return 0; 
	}
}
