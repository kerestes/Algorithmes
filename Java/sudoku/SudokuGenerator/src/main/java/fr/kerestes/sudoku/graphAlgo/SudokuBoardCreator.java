package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;
import fr.kerestes.sudoku.models.ColumnHead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SudokuBoardCreator {

    private int base, size;
    private List<Integer> possibleList, removedValue;
    private Integer[][] board;
    private Random random;

    public SudokuBoardCreator(int base, Integer[][] board){
        this.base=base;
        size = base*base;
        this.board=board;

        random = new Random();

        removedValue = new ArrayList<>();
        possibleList = new ArrayList<>(size*size);
        createPossibleList();
    }

    private void createPossibleList(){
        int k=0;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(board[i][j] != 0)
                    possibleList.add(k);
                k++;
            }
        }
    }

    public Integer[][] create(int parallelRemove, int clues) throws InvalidSudoku {
        SudokuVerify sudokuVerify = new SudokuVerify();
        SudokuGraph sudokuGraph = new SudokuGraph(base);

        removeParallelNumbers(parallelRemove, sudokuVerify, sudokuGraph);
        if(!removeRecursivelyNumbers(parallelRemove, clues, sudokuVerify, sudokuGraph))
            throw new InvalidSudoku("There is no solution for this initial board size");

        return board;
    }

    private void removeParallelNumbers(int parallelRemove, SudokuVerify sudokuVerify, SudokuGraph sudokuGraph) {
        int choice, place, listSize, reversedPlace;
        while(parallelRemove *2 > removedValue.size()){
            listSize = possibleList.size();
            do{
                choice = random.nextInt(listSize);
            } while(size%2 == 1 && choice == listSize/2);
            place = possibleList.get(choice);
            reversedPlace = possibleList.get(possibleList.size() - 1 - choice);
            removeParallelClues(place, reversedPlace);
            sudokuVerify.insertTable(sudokuGraph.createLinkedList(), board, base);
            if(sudokuVerify.search()){
                possibleList.remove(choice);
                if(choice >= (listSize)/2)
                    possibleList.remove(possibleList.size() - choice);
                else
                    possibleList.remove(possibleList.size() - 1 - choice);
                continue;
            }
            restoreParallelClues(place, reversedPlace);
        }
    }

    private boolean removeRecursivelyNumbers(int parallelRemove, int clues, SudokuVerify sudokuVerify, SudokuGraph sudokuGraph) {
        int place;
        List<Integer> indexChoice = new ArrayList<>();
        for(int i=0; i<possibleList.size(); i++){
            indexChoice.add(i);
        }
        while(!indexChoice.isEmpty()){
            place = indexChoice.remove(random.nextInt(indexChoice.size()));
            removeOneClue(possibleList.get(place));
            sudokuVerify.insertTable(sudokuGraph.createLinkedList(), board, base);
            if(sudokuVerify.search()){
                int removed = possibleList.remove(place);
                if(removeRecursivelyNumbers(parallelRemove, clues, sudokuVerify, sudokuGraph))
                    return true;
                possibleList.add(place, removed);
            }
            replaceOneClue(possibleList.get(place));
            if(clues > size*size - removedValue.size()){
                System.out.println("Initial Board Size: " + (size*size - removedValue.size()));
                return true;
            }
        }
        return false;
    }

    private void removeParallelClues(int place, int reversedPlace){
        int row = place/size;
        int column = place%size;
        int reversedRow = reversedPlace/size;
        int reversedColumn = reversedPlace%size;
        removedValue.add(board[row][column]);
        board[row][column] = 0;
        removedValue.add(board[reversedRow][reversedColumn]);
        board[reversedRow][reversedColumn] = 0;
    }

    private void restoreParallelClues(int place, int reversedPlace){
        int row = place/size;
        int column = place%size;
        int reversedRow = reversedPlace/size;
        int reversedColumn = reversedPlace%size;
        int reversedValue = removedValue.remove(removedValue.size() -1);
        int value = removedValue.remove(removedValue.size() -1);
        board[row][column] = value;
        board[reversedRow][reversedColumn] = reversedValue;
    }

    private void removeOneClue(int place){
        int row = place/size;
        int column = place%size;
        removedValue.add(board[row][column]);
        board[row][column] = 0;
    }

    private void replaceOneClue(int place){
        int row = place/size;
        int column = place%size;
        board[row][column] = removedValue.remove(removedValue.size() -1);
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
