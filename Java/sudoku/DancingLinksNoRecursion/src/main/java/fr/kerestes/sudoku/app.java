package fr.kerestes.sudoku;

import fr.kerestes.sudoku.graphAlgo.SudokuBoard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class app {
    public static void main(String[] args) throws IOException {

        SudokuBoard board = new SudokuBoard();

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Sudoku_Files_Name.csv"));
        String fileName = "";
        while((fileName = reader.readLine()) != null){
            board = new SudokuBoard(fileName);
            System.out.println("----------------");
            System.out.println("New Sudoku - " + fileName);
            System.out.println("----------------");

            System.out.println(board.toString());

            System.out.println("----------------");
            System.out.println("Resolved Sudoku ");
            System.out.println("----------------");

            board.startAutomaticGame();
        }
    }
}
