package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;
import fr.kerestes.sudoku.models.ColumnHead;
import fr.kerestes.sudoku.models.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Joueur {

    private int base;
    private int size;
    private Integer[][] board;
    private ColumnHead columnRoot = new ColumnHead("Root");
    private List<ColumnHead> result;

    public Joueur(int base, Integer[][] board){
        this.base=base;
        this.size = base*base;
        this.board=board;
        result = new ArrayList<>();

        columnRoot.setNext(columnRoot);
        columnRoot.setPrevious(columnRoot);

        createLinkedList();
        ColumnHead temp = columnRoot.getNext();
        firstInsertion();
    }

    public Integer[][] jouer() throws InvalidSudoku {
        findAnswer();
        System.out.println(result.size());
        if(result.size() == size*size){
            Integer[][] newBoard = new Integer[size][size];
            for(int i=0; i< result.size(); i++){
                ColumnHead columnHead = result.get(i);
                newBoard[(int)findLine(columnHead)][(int)findColumn(columnHead)] = (int)findValue(columnHead);
            }
            return newBoard;
        }else{
            throw new InvalidSudoku("There is no solution for this Sudoku");
        }
    }

    private void createLinkedList(){

        for(int line=0; line<size*size*size; line++){

            Node[] positionList = createPossibilitiesList(line);

            ColumnHead columnHead = new ColumnHead(size);
            columnHead.setMatrix(positionList);

            columnHead.setPrevious(columnRoot.getPrevious());
            columnHead.setNext(columnRoot);
            columnRoot.getPrevious().setNext(columnHead);
            columnRoot.setPrevious(columnHead);

        }

        linkedNodeWithColumnHead();
    }

    private Node[] createPossibilitiesList(int line){
        long square = 0;

        Node[] positionList = new Node[4];
        positionList[0] = new Node(line%(size*size));
        positionList[1] = new Node(line%size + (size * (line/(size*size))));
        if(line%size + line/(size*size) >= size)
            positionList[2] = new Node(line%(size*size) + line/(size*size) - size);
        else
            positionList[2] = new Node(line%(size*size) + line/(size*size));

        long tempLine = positionList[0].getValue()/size;
        long tempColumn = positionList[1].getValue()%size;
        long value = positionList[2].getValue()%size;
        square = (tempLine/base)*base + tempColumn/base;
        positionList[3] = new Node(value + (square * size));
        return positionList;
    }

    private void linkedNodeWithColumnHead(){
        ColumnHead columnHead = columnRoot.getNext();
        while (!isRootColumnHead(columnHead)){
            if(!isRootColumnHead(columnHead)){
                long line = columnHead.getMatrix()[0].getValue();
                long column = columnHead.getMatrix()[1].getValue();
                long value = columnHead.getMatrix()[2].getValue();
                long square = columnHead.getMatrix()[3].getValue();
                ColumnHead tempColumnHead = columnRoot.getNext();
                while(!isRootColumnHead(tempColumnHead)){
                    if(tempColumnHead != columnHead && !isRootColumnHead(tempColumnHead)){
                        if(tempColumnHead.getMatrix()[0].getValue() == line){
                            columnHead.getMatrix()[0].getConnectedList().add(tempColumnHead);
                        }
                        if(tempColumnHead.getMatrix()[1].getValue() == column){
                            columnHead.getMatrix()[1].getConnectedList().add(tempColumnHead);
                        }
                        if(tempColumnHead.getMatrix()[2].getValue() == value){
                            columnHead.getMatrix()[2].getConnectedList().add(tempColumnHead);
                        }
                        if(tempColumnHead.getMatrix()[3].getValue() == square){
                            columnHead.getMatrix()[3].getConnectedList().add(tempColumnHead);
                        }
                    }
                    tempColumnHead = tempColumnHead.getNext();
                }
            }
            columnHead = columnHead.getNext();
        }
    }
    private void firstInsertion(){
        for(int line=0; line<size; line++){
            for(int column=0; column<size; column++){
                if(board[line][column] != 0){
                    ColumnHead columnHead = columnRoot.getNext();
                    while (!isRootColumnHead(columnHead)){
                        if(findLine(columnHead) == line && findColumn(columnHead) == column && findValue(columnHead) == board[line][column]){
                            result.add(columnHead);
                            removeLinked(columnHead);
                            break;
                        }
                        columnHead = columnHead.getNext();
                    };
                }
            }
        }
    }

    private boolean findAnswer(){
        ColumnHead columnHead = columnRoot.getNext();
        int countNodes = Integer.MAX_VALUE;
        int countNodesCheck = 0;
        do{
            if(countNodes != countNodesCheck){
                countNodesCheck = countNodes;
                countNodes = 0;
                if(!isRootColumnHead(columnHead)){
                    for(int column=0; column<4; column++ ){
                        List<Long> valueList=new ArrayList<>();
                        while (!isRootColumnHead(columnHead)){
                            valueList.add(columnHead.getMatrix()[column].getValue());
                            columnHead = columnHead.getNext();
                            countNodes++;
                        }

                        columnHead = columnHead.getNext();
                        List<Long> removedList = new ArrayList<>();
                        for(int i=0; i<valueList.size(); i++){
                            for(int j=0; j<valueList.size(); j++){
                                if(i!=j && valueList.get(i) == valueList.get(j))
                                    removedList.add(valueList.get(i));
                            }
                        }
                        valueList.removeAll(removedList);

                        while (!isRootColumnHead(columnHead) && !valueList.isEmpty()){
                            for(int value=0; value < valueList.size(); value++){
                                if(valueList.get(value) == columnHead.getMatrix()[column].getValue()){
                                    result.add(columnHead);
                                    removeLinked(columnHead);
                                    break;
                                }
                            }
                            columnHead = columnHead.getNext();
                        }
                    }
                }
                columnHead = columnRoot.getNext();
            } else {
                long caseChoose = columnHead.getMatrix()[0].getValue();
                do{
                    if(caseChoose == columnHead.getMatrix()[0].getValue()){
                        result.add(columnHead);
                        removeLinked(columnHead);
                        if(findAnswer()){
                            return true;
                        }else{
                            result.remove(columnHead);
                            restoreLinked(columnHead);
                        }
                    }
                    columnHead = columnHead.getNext();
                } while (!isRootColumnHead(columnHead));
                return false;
            }
        }while (!ThereIsJustOneElementInTheLinkedList(columnHead));
        if(result.size() == size*size)
            return true;
        else
            return false;
    }

    private void removeLinked(ColumnHead columnHead){
        if(!ThereIsJustOneElementInTheLinkedList(columnHead) && !isRootColumnHead(columnHead)){
            columnHead.getNext().setPrevious(columnHead.getPrevious());
            columnHead.getPrevious().setNext(columnHead.getNext());
            removeReferenceToLink(columnHead);
            List<ColumnHead> temp = new ArrayList<>();
            for(int i=0; i<4; i++){
                for(int j=0; j<columnHead.getMatrix()[i].getConnectedList().size(); j++){
                    ColumnHead removeColumn = columnHead.getMatrix()[i].getConnectedList().get(j);
                    if(!temp.contains(removeColumn)){
                        removeColumn.getPrevious().setNext(removeColumn.getNext());
                        removeColumn.getNext().setPrevious(removeColumn.getPrevious());
                        removeReferenceToLink(removeColumn);
                        temp.add(removeColumn);
                    }
                }
            }
        }
    }

    private void removeReferenceToLink(ColumnHead removedColumnHead){
        ColumnHead tempColumnHead = columnRoot.getNext();
        while(!isRootColumnHead(tempColumnHead)){
            tempColumnHead.getMatrix()[0].getConnectedList().remove(removedColumnHead);
            tempColumnHead.getMatrix()[1].getConnectedList().remove(removedColumnHead);
            tempColumnHead.getMatrix()[2].getConnectedList().remove(removedColumnHead);
            tempColumnHead.getMatrix()[3].getConnectedList().remove(removedColumnHead);
            tempColumnHead = tempColumnHead.getNext();
        }
    }

    private void restoreLinked(ColumnHead columnHead){
        if(!isRootColumnHead(columnHead)){
            ColumnHead tempColumnHead;
            for(int i=0; i<4; i++){
                for(int j=0; j<columnHead.getMatrix()[i].getConnectedList().size(); j++){
                    ColumnHead restoreColumn = columnHead.getMatrix()[i].getConnectedList().get(j);
                    restoreColumn.getPrevious().setNext(restoreColumn);
                    restoreColumn.getNext().setPrevious(restoreColumn);
                    tempColumnHead = columnRoot.getNext();
                    restoreReferenceToLink(tempColumnHead, restoreColumn);
                }
            }
            columnHead.getNext().setPrevious(columnHead);
            columnHead.getPrevious().setNext(columnHead);
            tempColumnHead = columnRoot.getNext();
            restoreReferenceToLink(tempColumnHead, columnHead);
        }
    }

    private void restoreReferenceToLink(ColumnHead controlColumnHead, ColumnHead removedColumnHead){
        while(!isRootColumnHead(controlColumnHead)){
            controlColumnHead.getMatrix()[0].getConnectedList().add(removedColumnHead);
            controlColumnHead.getMatrix()[1].getConnectedList().add(removedColumnHead);
            controlColumnHead.getMatrix()[2].getConnectedList().add(removedColumnHead);
            controlColumnHead.getMatrix()[3].getConnectedList().add(removedColumnHead);
            controlColumnHead = controlColumnHead.getNext();
        }
    }

    private long findLine(ColumnHead columnHead){
            return columnHead.getMatrix()[0].getValue()/size;
    }

    private long findColumn(ColumnHead columnHead){
        return columnHead.getMatrix()[1].getValue()%size;
    }

    private long findValue(ColumnHead columnHead){
        return columnHead.getMatrix()[2].getValue()%size+1;
    }

    private boolean ThereIsJustOneElementInTheLinkedList(ColumnHead columnHead){
        return columnHead.getPrevious() == columnHead && columnHead.getNext() == columnHead;
    }

    private boolean isRootColumnHead(ColumnHead columnHead){
        return columnHead.getName().isPresent();
    }

}
