package edu.iastate.cs472.proj1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  
 * @author Jack Krause
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

		} catch(FileNotFoundException e) {
			System.out.println("could not read file");
			e.printStackTrace();
		}

	}
    
    
    /**
     * Generate the successor state resulting from a given move.  Throw an exception if the move 
     * cannot be executed.  Besides setting the array board[][] properly, you also need to do the 
     * following:
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
		int emptyRow = -1;
		int emptyColumn = -1;

		//TODO
		// find location of the empty square
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (this.board[r][c] == 0) {
					emptyRow = r;
					emptyColumn = c;
				}
			}
		}

		if (emptyRow == -1 || emptyColumn == -1) throw new IllegalArgumentException("error: no empty space found");
		if (! isMoveValid(m, emptyRow, emptyColumn)) throw new IllegalArgumentException("invalid move");

		State s = new State(this.board);
		s.move = m;
		s.predecessor = this;
		s.next = null;
		s.previous = null;
		s.numMoves = this.numMoves + 1;

		System.out.println("before move " + m);
		s.toString();

		s.performMove(m, emptyRow, emptyColumn);

		System.out.println("board after move " + m);
		s.toString();

    	return s;
    }

	public int[] performMove(Move m, int r, int c) throws IllegalArgumentException {
		int[] emptyCell = {-1, -1};
		if (this.board[r][c] != 0)  {
			throw new IllegalArgumentException("move performed at non-empty cell");
		}

		switch(m) {
			case UP:
				emptyCell[0] = r+1;
				emptyCell[1] = c;
				break;
			case DOWN:
				emptyCell[0] = r-1;
				emptyCell[1] = c;
				break;
			case LEFT:
				emptyCell[0] = r;
				emptyCell[1] = c+1;
				break;
			case RIGHT:
				emptyCell[0] = r;
				emptyCell[1] = c-1;
				break;
			case DBL_UP:
				emptyCell[0] = r+1;
				emptyCell[1] = c;
				this.board[r][c] = this.board[r+1][c];
				this.board[r+1][c] = 0;
				return performMove(Move.UP, emptyCell[0], emptyCell[1]);
			case DBL_DOWN:
				emptyCell[0] = r-1;
				emptyCell[1] = c;
				this.board[r][c] = this.board[r-1][c];
				this.board[r-1][c] = 0;
				return performMove(Move.DOWN, emptyCell[0], emptyCell[1]);
			case DBL_LEFT:
				emptyCell[0] = r;
				emptyCell[1] = c+1;
				this.board[r][c] = this.board[r][c+1];
				this.board[r][c+1] = 0;
				return performMove(Move.LEFT, emptyCell[0], emptyCell[1]);
			case DBL_RIGHT:
				emptyCell[0] = r;
				emptyCell[1] = c-1;
				this.board[r][c] = this.board[r][c-1];
				this.board[r][c-1] = 0;
				return performMove(Move.RIGHT, emptyCell[0], emptyCell[1]);
			default:
				return emptyCell;

		}

		this.board[r][c] = this.board[ emptyCell[0] ][ emptyCell[1] ];
		this.board[ emptyCell[0] ][ emptyCell[1] ] = 0;

		return emptyCell;
	}



	/**
	 * @throws IllegalArgumentException if LEFT when the empty square is in the right column, or
	 *                                  if RIGHT when the empty square is in the left column, or
	 *                                  if UP when the empty square is in the bottom row, or
	 *                                  if DOWN when the empty square is in the top row, or
	 *                                  if DBL_LEFT when the empty square is not in the left column, or
	 *                                  if DBL_RIGHT when the empty square is not in the right column, or
	 *                                  if DBL_UP when the empty square is not in the top row, or
	 *                                  if DBL_DOWN when the empty square is not in the bottom row.
	 */
	public boolean isMoveValid(Move m, int r, int c) {
        return (m != Move.LEFT || c != 2)
                &&
                (m != Move.RIGHT || c != 0)
                &&
                (m != Move.UP || r != 2)
                &&
                (m != Move.DOWN || r != 0)
                &&
                (m != Move.DBL_LEFT || c == 0)
                &&
                (m != Move.DBL_RIGHT || c == 2)
                &&
                (m != Move.DBL_UP || r == 0)
                &&
                (m != Move.DBL_DOWN || r == 2);
    }
    
        
    /**
     * Determines if the board configuration in this state can be rearranged into the goal configuration. 
     * According to the PowerPoint notes that introduce the 8-puzzle, we check if this state has an odd number 
     * of inversions.
	 * solvable iff initial state has an odd number of inversions
     */
    public boolean solvable()
    {
		System.out.println("inversions " + this.inversions());
		return (this.inversions() % 2 == 1);
    }

	/**
	 * returns the number of inversions in the current board
	 * inversion if a larger number is above or to the right of a smaller number
	 * note: the puzzle is solvable iff there are an odd number of inversions
	 * @return the count of inversions in the board
	 */
    public int inversions() {
		int inversionCount = 0;

		// get board in a 1-d array and just move
		int[] flattenedBoard = new int[9];

		int idx = 0;
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				int cell = this.board[r][c];

				flattenedBoard[idx] = cell;
				idx++;
			}
		}

		for (int i = 0; i < 9; i++) {
			int cell = flattenedBoard[i];

			for (int j = i + 1; j < 9; j++) {
				int jCell = flattenedBoard[j];

				if (jCell != 0 && cell > jCell) inversionCount++;
			}
		}

		return inversionCount;
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
    	return this.computeNumMismatchedTiles() == 0;
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
		State s = new State(this.board);
		s.numMoves = this.numMoves;
		return s;
    }
  

    /**
     * Compare this state with the argument state.  Two states are equal if their arrays board[][] 
     * have the same content.
     */
    @Override 
    public boolean equals(Object o)
    {
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
     * If heu == TileMismatch, add up numMoves and the return values from computeNumMismatchedTiles().
     * If heu == MahattanDist, add up numMoves and the return values of computeMahattanDistance(). 
     * If heu == DoubleMoveHeuristic, add up numMoves and the return value of computeNumSingleDoubleMoves(). 
     * 
     * @return estimated number of moves from the initial state to the goal state via this state.
     * @throws IllegalArgumentException if heuristic is none of TileMismatch, MahattanDist, DoubleMoveHeuristic. 
     */
    public int cost() throws IllegalArgumentException
    {
		if (heu == Heuristic.TileMismatch) return this.numMoves + computeNumMismatchedTiles();
		if (heu == Heuristic.ManhattanDist) return this.numMoves + computeManhattanDistance();
		if (heu == Heuristic.DoubleMoveHeuristic) return this.numMoves + this.computeNumSingleDoubleMoves();
    	throw new IllegalArgumentException("hue is not properly set");
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
		if (this.numMismatchedTiles > 0) return this.numMismatchedTiles;

		this.numMismatchedTiles = 0;

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (this.board[r][c] != goalState[r][c]) {
					this.numMismatchedTiles ++;
				}
			}
		}

		return this.numMismatchedTiles;
	}

	
	/**
	 * Return the value of the private variable ManhattanDistance if it is non-negative, and compute its value 
	 * otherwise.
	 * 
	 * @return the Manhattan distance between this state and the goal state. 
	 */
	private int computeManhattanDistance() {
		// TODO
		if (this.ManhattanDistance > 0) return this.ManhattanDistance;
		
		int dist = 0;

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				int i = this.board[r][c];

				int x1 = 0;
				int x2 = 0;

				switch(i) {
					case 0:
						x1 = 1;
						x2 = 1;
						break;
					case 1:
						x1 = 0;
						x2 = 0;
						break;
					case 2:
						x1 = 0;
						x2 = 1;
						break;
					case 3:
						x1 = 0;
						x2 = 2;
						break;
					case 4:
						x1 = 1;
						x2 = 2;
						break;
					case 5:
						x1 = 2;
						x2 = 2;
						break;
					case 6:
						x1 = 2;
						x2 = 1;
						break;
					case 7:
						x1 = 2;
						x2 = 0;
						break;
					case 8:
						x1 = 1;
						x2 = 0;
						break;
				}

				dist += (Math.abs(x1 - r) + Math.abs(x2 - c));
			}
		}

		return dist;
	}
	
	
	/**
	 * Return the value of the private variable numSingleDoubleMoves if it is non-negative, and compute its value 
	 * otherwise. 
	 * 
	 * @return the value of the private variable numSingleDoubleMoves that bounds from below the number of moves, 
	 *         single or double, which will take this state to the goal state.
	 */
	private int computeNumSingleDoubleMoves() {
		//TODO
		if (this.numSingleDoubleMoves < 0) {
			this.numSingleDoubleMoves = 0;
			int h = this.computeManhattanDistance();

			// idea: use manhattan distance and account for distances that can be covered in two moves
			for (int r = 0; r < 3; r++) {
				for (int c = 0; c < 3; c++) {
					int cell = this.board[r][c];

					if (cell == goalState[r][c]) {
						continue;
					}

					if (r == 2 && (this.board[r-1][c] != goalState[r-1][c])) {
						if (this.isMoveValid(Move.DBL_UP, r, c));
					}

					if (r < 2 && (this.board[r+1][c] != goalState[r+1][c])) {
						if (this.isMoveValid(Move.DBL_DOWN, r, c)) h--;
					}

					if (c < 2 && (this.board[r][c+1] != goalState[r][c+1])) {
						if (this.isMoveValid(Move.DBL_LEFT, r, c)) h--;
					}

				}
			}

		}

		return 0;
	}


}
