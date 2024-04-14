package fr.kerestes.sudoku;

import fr.kerestes.sudoku.graphAlgo.SudokuBoard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class app {
    public static void main(String[] args) throws IOException {

        SudokuBoard board;

        while(true){
            board = new SudokuBoard(fileName);
            System.out.println("----------------");
            System.out.println("New Sudoku - " + fileName);
            System.out.println("----------------");

            board.startAutomaticGame();

            System.out.println("----------------");
        }
        reader.close();
    }
}
