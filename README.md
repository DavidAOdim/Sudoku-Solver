# Sudoku-Solver
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class SudokuSolver {

    private static final int GRID_SIZE = 9;

    public static void main(String[] args) {
        int[][] board = {
                {7, 0, 2, 0, 5, 0, 6, 0, 0},
                {0, 0, 0, 0, 3, 0, 0, 0, 0},
                {1, 0, 0, 0, 9, 5, 0, 0, 0},
                {8, 0, 0, 0, 0, 0, 9, 0, 0},
                {0, 4, 3, 0, 0, 7, 5, 0, 0},
                {0, 9, 0, 0, 0, 0, 0, 0, 8},
                {0, 0, 9, 7, 0, 0, 0, 0, 5},
                {0, 0, 0, 2, 0, 0, 0, 0, 0},
                {0, 0, 7, 0, 4, 0, 2, 0, 3}
        };

        if (solveBoard(board) && isBoardFullyFilled(board)) {
            System.out.println("Solved successfully!");
        } else {
            System.out.println("Unsolvable board or incomplete solution!");
        }

        printBoard(board);

    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            // Add a horizontal grid line after every 3 rows, except the first row
            if (row % 3 == 0 && row != 0) {
                System.out.println("------+-------+------");
            }

            for (int column = 0; column < GRID_SIZE; column++) {
                // Add a vertical grid line after every 3 columns, except the first column
                if (column % 3 == 0 && column != 0) {
                    System.out.print("| ");
                }

                // Print a dot for empty cells (0), otherwise print the number
                System.out.print(board[row][column] == 0 ? ". " : board[row][column] + " ");
            }
            System.out.println();
        }
    }

    //Checks if a number exists in a specific row by iterating through each cell in that row.
    private static boolean isNumberInRow(int[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInColumn(int[][] board, int number, int column) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][column] == number) {
                return true;
            }
        }
        return false;
    }

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

    private static boolean isValidPlacement(int[][] board, int number, int row, int column) {
        return !isNumberInRow(board, number, row) &&
                !isNumberInColumn(board, number, column) &&
                !isNumberInBox(board, number, row, column);
    }

    private static boolean solveBoard(int[][] board) {
        Deque<int[]> currentSpotDeque = new LinkedList<>();
        Stack<int[]> backtrackStack = new Stack<>();

        // Fill deque with empty spots
        findEmptySpots(board, currentSpotDeque);

        // Track the number of backtrack steps to prevent infinite loops
        int maxSteps = 10000;
        int steps = 0;

        while (!currentSpotDeque.isEmpty()) {
            steps++;
            if (steps > maxSteps) {
                System.out.println("Exceeded max steps, terminating.");
                return false;
            }

            int[] currentSpot = currentSpotDeque.pollFirst(); // Get next empty spot
            int row = currentSpot[0];
            int col = currentSpot[1];

            boolean numberPlaced = false;
            for (int number = 1; number <= GRID_SIZE; number++) {
                if (isValidPlacement(board, number, row, col)) {
                    board[row][col] = number;
                    backtrackStack.push(new int[]{row, col, number});
                    System.out.println("Placed " + number + " at [" + row + "," + col + "]");
                    numberPlaced = true;
                    break;
                }
            }

            if (!numberPlaced) {
                if (backtrackStack.isEmpty()) {
                    System.out.println("Backtracking stack empty. Board unsolvable.");
                    return false;  // No numbers fit, no way to proceed
                }

                System.out.println("Backtracking...");
                // Backtrack
                int[] lastStep = backtrackStack.pop();
                board[lastStep[0]][lastStep[1]] = 0;
                currentSpotDeque.addFirst(new int[]{lastStep[0], lastStep[1]});
            }
        }

        return true;
    }

    private static boolean isBoardFullyFilled(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    return false; // Found an empty cell
                }
            }
        }
        return true; // No empty cells, board is fully filled
    }

    private static void findEmptySpots(int[][] board, Deque<int[]> deque) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    deque.addLast(new int[]{row, col});
                }
            }
        }
    }
}
