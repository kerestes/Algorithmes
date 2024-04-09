package fr.kerestes.sudoku;

import fr.kerestes.sudoku.graphAlgo.SudokuBoard;

public class app {
    public static void main(String[] args) {

        SudokuBoard board = new SudokuBoard("src/main/resources/sudoku_4X4_1.csv");

        System.out.println("----------------");
        System.out.println("New Sudoku - 4X4 - 1");
        System.out.println("----------------");

        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 4X4 - 1");
        System.out.println("----------------");

        Long start = System.currentTimeMillis();
        board.startAutomaticGame();
        Long end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        board = new SudokuBoard("src/main/resources/sudoku_4X4_2.csv");

        System.out.println("----------------");
        System.out.println("New Sudoku - 4X4 - 2");
        System.out.println("----------------");

        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 4X4 - 2");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        board = new SudokuBoard("src/main/resources/sudoku_9X9_easy_1.csv");

        System.out.println("----------------");
        System.out.println("New Sudoku - 9X9 - Easy");
        System.out.println("----------------");

        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 9X9 - Easy");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        System.out.println("----------------");
        System.out.println("New Sudoku - 9X9 - Medium");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_9X9_medium_1.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 9X9 - Medium");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        System.out.println("----------------");
        System.out.println("New Sudoku - 9X9 - Hard");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_9X9_hard_1.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 9X9 - Hard");
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
        System.out.println("New Sudoku - 16X16 - Easy 2");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_16X16_easy_2.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 16X16 - Easy 2");
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

        System.out.println("----------------");
        System.out.println("New Sudoku - 16X16 - 1");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_16X16_1.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 16X16 - 1");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));

        System.out.println("----------------");
        System.out.println("New Sudoku - 16X16 - Hard");
        System.out.println("----------------");

        board = new SudokuBoard("src/main/resources/sudoku_16X16_hard.csv");
        System.out.println(board.toString());

        System.out.println("----------------");
        System.out.println("Resolved Sudoku - 16X16 - Hard");
        System.out.println("----------------");

        start = System.currentTimeMillis();
        board.startAutomaticGame();
        end = System.currentTimeMillis();

        System.out.println("Process time: " + (end - start));
    }
}
