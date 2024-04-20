package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;
import fr.kerestes.sudoku.repositories.SudokuRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SudokuBoard {

    private int base;
    private SudokuRepository sudokuRepository;
    private Integer[][] board;
    List<String> coordinates;
    public SudokuBoard(String filePath){
        sudokuRepository = new SudokuRepository(filePath);

        coordinates = sudokuRepository.readFile();

        this.base=Integer.parseInt(coordinates.get(0));
        board = new Integer[base*base][base*base];
    }

    public void mixedBoard(int times){
        int choice, first, second, aux, size = base*base;
        for(int i=0; i<times; i++){
            Random random = new Random();
            choice = random.nextInt(2);
            do{
                first = random.nextInt(size);
                second = random.nextInt(base) + (base * (first/base));
            }while(first == second);
            if(choice%2 == 0){
                for(int j=0; j<size; j++){
                    aux = board[j][first];
                    board[j][first] = board[j][second];
                    board[j][second] = aux;
                }
            } else{
                for(int j=0; j<size; j++){
                    aux = board[first][j];
                    board[first][j] = board[second][j];
                    board[second][j] = aux;
                }
            }
        }
    }

    public void generateGame(int parallelRemove, int clues, int mixTimes){
        for(int i = 1; i<coordinates.size(); i++){
            fillBoard(i);
            SudokuBoardCreator sudokuBoardCreator = new SudokuBoardCreator(base, board);
            mixedBoard(mixTimes);
            try{
                board = sudokuBoardCreator.create(parallelRemove, clues);
                String result = Arrays.stream(board).flatMap(Arrays::stream).map(String::valueOf).reduce("", (a,b) -> {
                   if(a.equals(""))
                       return b;
                   else
                       return a+","+b;
                });
                sudokuRepository.writeFile("src/main/resources/test", result);
                System.out.println(result);
            } catch (InvalidSudoku e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void fillBoard(int line){
        List<Integer> coordinateNumbers = Arrays.stream(coordinates.get(line).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        for(int i=0; i<coordinateNumbers.size(); i++){
            board[i/(base*base)][i%(base*base)] = coordinateNumbers.get(i);
        }
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
