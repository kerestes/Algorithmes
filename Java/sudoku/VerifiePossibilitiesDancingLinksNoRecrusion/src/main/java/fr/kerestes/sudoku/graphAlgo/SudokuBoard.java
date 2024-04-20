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

    private void fillBoard(int line){
        List<Integer> coordinateNumbers = Arrays.stream(coordinates.get(line).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        for(int i=0; i<coordinateNumbers.size(); i++){
            board[i/(base*base)][i%(base*base)] = coordinateNumbers.get(i);
        }
    }
    public void startAutomaticGame(){
        int i;
        long max = Integer.MIN_VALUE, min = Integer.MAX_VALUE, start, end, intialTime, finalTime, avg = 0, time;
        intialTime = System.nanoTime();
        for(i = 1; i<coordinates.size(); i++){
            fillBoard(i);
            SudokuGraph graph = new SudokuGraph(base, board);
            Player player = new Player(base, graph.getColumnHeadRoot(), board);
            try{
                start = System.nanoTime();
                player.play();
                end = System.nanoTime();
                time = (end - start);
                if(min > time)
                    min = time;
                if(max < time)
                    max = time;
                avg +=time;
                System.out.println("Process time: " + time);
            } catch (InvalidSudoku e){
                System.out.println(e.getMessage() + "\n");
                System.exit(100);
            }
            System.out.println("----------------------------------------------------------" + "\n");
        }
        finalTime = System.nanoTime();
        System.out.println("Lowest time: " + min);
        System.out.println("Highest time: " + max);
        System.out.println("Average: " + (avg/(i-1)));
        System.out.println("Total process time: " + (finalTime - intialTime));
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
