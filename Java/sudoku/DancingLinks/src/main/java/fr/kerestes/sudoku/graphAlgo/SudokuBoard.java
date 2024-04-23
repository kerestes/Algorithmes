package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;
import fr.kerestes.sudoku.repositories.SudokuRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuBoard {

    private int base;
    private SudokuRepository sudokuRepository;
    private Integer[][] board;
    List<String> coordinates;
    public SudokuBoard(){

    }
    public SudokuBoard(String filePath){
        sudokuRepository = new SudokuRepository(filePath);

        coordinates = sudokuRepository.readFile();

        this.base=Integer.parseInt(coordinates.get(0));
        board = new Integer[base*base][base*base];
    }

    public void fillBoard(int line){
        List<Integer> coordinateNumbers = Arrays.stream(coordinates.get(line).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        for(int i=0; i<coordinateNumbers.size(); i++){
            board[i/(base*base)][i%(base*base)] = coordinateNumbers.get(i);
        }
    }

    public void startAutomaticGame(){
        int i;
        long max = Integer.MIN_VALUE, min = Integer.MAX_VALUE, start, end, initialTime, finalTime, avg = 0, time;
        initialTime = System.nanoTime();
        for(i = 1; i<coordinates.size(); i++){
            fillBoard(i);
            SudokuCreateGraph graph = new SudokuCreateGraph(base, board);
            SudokuDancingLinks sudokuDancingLinks = new SudokuDancingLinks(base, graph.getColumnHeadRoot(), board);
            try{
                start = System.nanoTime();
                board = sudokuDancingLinks.play();
                end = System.nanoTime();
                if(verifyResult()) {
                    time = (end - start);
                    if(min > time)
                        min = time;
                    if(max < time)
                        max = time;
                    avg +=time;
                    //System.out.println(this);
                    System.out.println("Process time: " + time + " at line " + i);
                }else
                    System.out.println("Invalid Game");
                System.out.println("----------------------------------------------------------" + "\n");
            } catch (InvalidSudoku e){
                System.out.println(e.getMessage());
            }
        }
        finalTime = System.nanoTime();
        System.out.println("Lowest time: " + min);
        System.out.println("Highest time: " + max);
        System.out.println("Average: " + (avg/(i-1)));
        System.out.println("Total process time: " + (finalTime - initialTime));

    }

    private boolean verifyResult(){
        for(int i=0; i<base*base; i++){
            for(int j=0; j<base*base; j++){
                for(int k=0; k<3; k++){
                    for(int l=0; l<base*base; l++){
                        switch (k){
                            case 0:
                                if(l!=i)
                                    if(board[l][j] == board[i][j])
                                        return false;
                                break;
                            case 1:
                                if(l!=j)
                                    if(board[i][l] == board[i][j])
                                        return false;
                                break;
                            case 2:
                                int zoneLine = l/base + (base * (i/base));
                                int zoneColumn = l%base + ( base * (j/base));
                                if(i != zoneLine && j != zoneColumn)
                                    if(board[zoneLine][zoneColumn] == board[i][j])
                                        return false;
                                break;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString(){
        String line = "";
        for (int i=0; i<base*base; i++){
            line += "| ";
            for(int j=0; j<base*base; j++){
                if(board[i][j] == 0){
                    line += "   | ";
                } else{
                    line += board[i][j] < 10 ? "0" + board[i][j] : board[i][j];
                    line += " | ";
                }
            }
            line += "\n";
        }
        return line;
    }
}
