package fr.kerestes.sudoku;

import fr.kerestes.sudoku.graphAlgo.SudokuBoard;

public class app {
    public static void main(String[] args) {

        SudokuBoard board = new SudokuBoard("src/main/resources/sudoku_9X9_easy_1.csv");

        System.out.println("----------------");
        System.out.println("New Sudoku - Easy");
        System.out.println("----------------");

        board.printSudoku();

        System.out.println("----------------");
        System.out.println("Resolved Sudoku");
        System.out.println("----------------");

        board.startResolution();

        System.out.println("----------------");
        System.out.println("New Sudoku - Medium");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_9X9_medium_1.csv");
        board.printSudoku();

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - Hard");
        System.out.println("----------------");

        board.startResolution();

        System.out.println("----------------");
        System.out.println("New Sudoku");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_9X9_hard_1.csv");
        board.printSudoku();

        System.out.println("----------------");
        System.out.println("Resolved Sudoku");
        System.out.println("----------------");

        board.startResolution();
    }
}
