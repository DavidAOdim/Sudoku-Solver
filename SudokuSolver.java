import java.util.*;

public class SudokuSolver {

    private static final int GRID_SIZE = 9;  // Standard Sudoku board size
    private static final Random RANDOM = new Random();  // Random generator for board creation

    public static void main(String[] args) {
        int[][] board = generateRandomBoard(30); // Generate a board with 30 pre-filled numbers

        System.out.println("Randomly Generated Sudoku Board:");
        printBoard(board);

        if (solveBoard(board) && isBoardFullyFilled(board)) {
            System.out.println("\nSolved Sudoku Board:");
        } else {
            System.out.println("\nUnsolvable board or incomplete solution!");
        }
        printBoard(board);
    
    }

    /**
     * Generates a random Sudoku board with a specified number of pre-filled cells.
     * Ensures numbers are placed correctly according to Sudoku rules.
     */
    private static int[][] generateRandomBoard(int filledCells) {
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        int count = 0;

        while (count < filledCells) {
            int row = RANDOM.nextInt(GRID_SIZE);
            int col = RANDOM.nextInt(GRID_SIZE);
            int num = RANDOM.nextInt(9) + 1; // Generate a number between 1 and 9

            // Place the number only if the cell is empty and placement is valid
            if (board[row][col] == 0 && isValidPlacement(board, num, row, col)) {
                board[row][col] = num;
                count++;
            }
        }
        return board;
    }

    /**
     * Prints the Sudoku board with proper formatting.
     * Empty cells (0) are represented as dots for better readability.
     */
    private static void printBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("------+-------+------"); // Print horizontal separator
            }
            for (int column = 0; column < GRID_SIZE; column++) {
                if (column % 3 == 0 && column != 0) {
                    System.out.print("| "); // Print vertical separator
                }
                System.out.print(board[row][column] == 0 ? ". " : board[row][column] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Checks if a given number exists in the specified row.
     */
    private static boolean isNumberInRow(int[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given number exists in the specified column.
     */
    private static boolean isNumberInColumn(int[][] board, int number, int column) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][column] == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given number exists in the 3x3 subgrid (box).
     */
    private static boolean isNumberInBox(int[][] board, int number, int row, int column) {
        int localBoxRow = row - row % 3;
        int localBoxColumn = column - column % 3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxColumn; j < localBoxColumn + 3; j++) {
                if (board[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Validates whether a number can be placed at a given position.
     */
    private static boolean isValidPlacement(int[][] board, int number, int row, int column) {
        return !isNumberInRow(board, number, row) &&
               !isNumberInColumn(board, number, column) &&
               !isNumberInBox(board, number, row, column);
    }

    /**
     * Solves the Sudoku board using a backtracking approach.
     */
    private static boolean solveBoard(int[][] board) {
        Deque<int[]> currentSpotDeque = new LinkedList<>();
        Stack<int[]> backtrackStack = new Stack<>();
        findEmptySpots(board, currentSpotDeque); // Find all empty spots

        int maxSteps = 10000, steps = 0; // Prevent infinite loops

        while (!currentSpotDeque.isEmpty()) {
            steps++;
            if (steps > maxSteps) return false; // Safety check for infinite loops

            int[] currentSpot = currentSpotDeque.pollFirst();
            int row = currentSpot[0], col = currentSpot[1];

            boolean numberPlaced = false;
            for (int number = 1; number <= GRID_SIZE; number++) {
                if (isValidPlacement(board, number, row, col)) {
                    board[row][col] = number;
                    backtrackStack.push(new int[]{row, col, number});
                    numberPlaced = true;
                    break;
                }
            }

            // If no valid number found, backtrack
            if (!numberPlaced) {
                if (backtrackStack.isEmpty()) return false;
                int[] lastStep = backtrackStack.pop();
                board[lastStep[0]][lastStep[1]] = 0;
                currentSpotDeque.addFirst(new int[]{lastStep[0], lastStep[1]});
            }
        }
        return true;
    }

    /**
     * Checks if the Sudoku board is fully filled.
     */
    private static boolean isBoardFullyFilled(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) return false; // Found an empty cell
            }
        }
        return true; // Board is completely filled
    }

    /**
     * Identifies all empty spots on the board and stores them in a deque.
     */
    private static void findEmptySpots(int[][] board, Deque<int[]> deque) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) deque.addLast(new int[]{row, col});
            }
        }
    }
}
