package fr.kerestes.sudoku.exceptions;

public class InvalidSudoku extends Exception{
    public InvalidSudoku(String errorMessage){
        super(errorMessage);
    }
}
