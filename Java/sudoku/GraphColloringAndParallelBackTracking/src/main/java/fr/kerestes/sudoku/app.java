package fr.kerestes.sudoku;

import fr.kerestes.sudoku.graphAlgo.SudokuBoard;

public class app {
    public static void main(String[] args) {

        SudokuBoard board = new SudokuBoard("src/main/resources/sudoku_9X9_easy_1.csv");

        System.out.println("----------------");
        System.out.println("New Sudoku - Easy");
        System.out.println("----------------");

        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - Easy");
        System.out.println("----------------");

        Long start = System.currentTimeMillis();
        board.startAutomaticGame();
        Long end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        System.out.println("----------------");
        System.out.println("New Sudoku - Medium");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_9X9_medium_1.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - Medium");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        System.out.println("----------------");
        System.out.println("New Sudoku - Hard");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_9X9_hard_1.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - Hard");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        System.out.println("----------------");
        System.out.println("New Sudoku - 16X16 - Easy");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_16X16_easy.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 16X16 - Easy");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        System.out.println("----------------");
        System.out.println("New Sudoku - 16X16 - Medium");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_16X16_medium.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 16X16 - Medium");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        System.out.println("----------------");
        System.out.println("New Sudoku - 16X16 - Medium 2");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_16X16_medium_2.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 16X16 - Medium 2");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));
    }
}
