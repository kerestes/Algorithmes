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

    public void writeFile(String filePath, String doc){
        try(FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(doc);
        } catch (IOException e) {
            System.out.println("Open file error");
        }
    }
}
