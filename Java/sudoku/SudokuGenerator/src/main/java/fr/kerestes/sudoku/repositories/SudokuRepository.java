package fr.kerestes.sudoku.repositories;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SudokuRepository {

    List<String> nodesCordinates = new ArrayList<>();
    String filePath;

    public SudokuRepository(String filePath){
        this.filePath=filePath;
    }

    public void writeFile(String board){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(board);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
