package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;

public class Player {

    private int base;
    private Integer[][] board;

    public Player(int base, Integer[][] board){
        this.base=base;
        this.board=board;
    }

    public Integer[][] play() throws InvalidSudoku {
        if(backtracking())
            return board;
        else
            throw new InvalidSudoku("Invalid Game");
    }

    private boolean verifyLine(int row, int value){
        for(int column = 0; column < base * base; column++){
            if(board[row][column] == value)
                return false;
        }
        return true;
    }

    private boolean verifyColumn(int column, int value){
        for(int row = 0; row < base * base; row++){
            if(board[row][column] == value)
                return false;
        }
        return true;
    }

    private boolean verifySquare(int row, int column, int value){
        for(int l=0; l<base*base; l++){
            int zoneLine = l/base + (base * (row/base));
            int zoneColumn = l%base + ( base * (column/base));
            if(board[zoneLine][zoneColumn] == value)
                return false;
        }
        return true;
    }

    private boolean isAvailableNumber(int row, int column, int value){
        return verifyLine(row, value)
                && verifyColumn(column, value)
                && verifySquare(row, column, value);
    }

    private boolean backtracking(){
        for(int row=0; row<base*base; row++){
            for(int column=0; column<base*base; column++){
                if(board[row][column] == 0){
                    for(int l=1; l<=base*base; l++){
                        if(isAvailableNumber(row, column, l)){
                            board[row][column] = l;
                            if(backtracking())
                                return true;
                            board[row][column] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

}
