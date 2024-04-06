package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;
import fr.kerestes.sudoku.repositories.SudokuRepository;

import java.util.List;

public class SudokuBoard {

    private int base;
    private SudokuRepository sudokuRepository;
    private Integer[][] board;

    public SudokuBoard(String filePath){
        sudokuRepository = new SudokuRepository(filePath);

        List<String> coordinate = sudokuRepository.readFile();

        this.base=Integer.parseInt(coordinate.get(0));
        board = new Integer[base*base][base*base];
        fillBoard(coordinate);


    }

    public void fillBoard(List<String> nodesCoordinate){
        for(int i=1; i<nodesCoordinate.size(); i++){
            String [] coord = nodesCoordinate.get(i).split(",");
            board[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = Integer.parseInt(coord[2]);
        }
        for(int line=0; line<base*base; line++){
            for(int column=0; column<base*base; column++){
                if(board[line][column] == null)
                    board[line][column] = 0;
            }
        }
    }

    public void startAutomaticGame(){
        Joueur joueur = new Joueur(base, board);
        try{
            board = joueur.jouer();
            if(verifyResult())
                System.out.println(this);
            else
                System.out.println("Invalid Game");
        } catch (InvalidSudoku e){
            System.out.println(e.getMessage());
        }
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
