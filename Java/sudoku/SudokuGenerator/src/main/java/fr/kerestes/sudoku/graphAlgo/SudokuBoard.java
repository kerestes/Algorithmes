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
    List<Integer> coordinates;
    public SudokuBoard(){

    }
    public SudokuBoard(String filePath){
        sudokuRepository = new SudokuRepository(filePath);

        coordinates = generateCardinates();

        this.base=2;
        board = new Integer[base*base][base*base];
    }

    public void fillBoard(int line){
        List<Integer> coordinateNumbers = Arrays.stream(coordinates.get(line).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        for(int i=0; i<coordinateNumbers.size(); i++){
            board[i/(base*base)][i%(base*base)] = coordinateNumbers.get(i);
        }
    }

    public void startAutomaticGame(){
        for(int i = 1; i<coordinates.size(); i++){
            fillBoard(i);
            SudokuGraph graph = new SudokuGraph(base, board);
            Player player = new Player(base, graph.getColumnHeadRoot(), board);
            try{
                board = player.play();
                String result = coordinates.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","));
                sudokuRepository.writeFile(result);
                System.out.println("There is more then one possibility");
            } catch (InvalidSudoku e){
                System.out.println("There is just one possibility");
            }
        }
    }

   private List<> generateCardinates(){

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
