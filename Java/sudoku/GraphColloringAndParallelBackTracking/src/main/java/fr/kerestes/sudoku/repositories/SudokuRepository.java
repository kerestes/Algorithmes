package fr.kerestes.sudoku.repositories;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SudokuRepository {

    List<String> nodesCordinates = new ArrayList<>();
    String filePath;

    public SudokuRepository(String filePath){
        this.filePath=filePath;
    }

    public List<String> readFile(){
        String line = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));

            while((line = reader.readLine()) != null){
                nodesCordinates.add(line);
            }
            reader.close();
            return nodesCordinates;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
