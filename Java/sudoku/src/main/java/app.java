
public class app {
    public static void main(String[] args) {

        Integer base = 3;

        SudokuBoard board = new SudokuBoard(base);

        board.printSudoku();

        System.out.println("\nResolved Sudoku\n");

        board.findSolution();

        board.printSudoku();

    }
}
