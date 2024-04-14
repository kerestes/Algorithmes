package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;
import fr.kerestes.sudoku.models.ColumnHead;
import fr.kerestes.sudoku.models.Node;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int base;
    private int size;
    private ColumnHead columnHeadRoot;
    private Integer[][] board;
    private List<Node> result;

    public Player(int base, ColumnHead columnHead, Integer[][] board){
        this.columnHeadRoot = columnHead;
        this.base=base;
        this.size = base*base;
        this.board = board;
        result = new ArrayList<>(base*base);
        firstInsertion();
    }

    public Integer[][] play() throws InvalidSudoku {
        if(findAnswer()){
            Integer[][] board = new Integer[size*size*size][size*size*4];
            for(int i=0; i<size*size; i++){
                int row;
                int column;
                int value;

                //If the node saved is a row
                if(result.get(i).head.name < size*size){
                    row =  (int) findLine(result.get(i));
                    column =  (int) findColumn(result.get(i).rightNode);
                    value = (int) findValue(result.get(i).rightNode.rightNode);
                }
                //If the node saved is a column
                else if(result.get(i).head.name < size*size*2){
                    column =  (int) findColumn(result.get(i));
                    value = (int) findValue(result.get(i).rightNode);
                    row =  (int) findLine(result.get(i).leftNode);
                }
                //If the node saved is a value
                else if(result.get(i).head.name < size*size*3){
                    value = (int) findValue(result.get(i));
                    row =  (int) findLine(result.get(i).leftNode.leftNode);
                    column =  (int) findColumn(result.get(i).leftNode);
                }
                //If the node saved is a square
                else {
                    row =  (int) findLine(result.get(i).rightNode);
                    column =  (int) findColumn(result.get(i).leftNode.leftNode);
                    value = (int) findValue(result.get(i).leftNode);
                }
                board[row][column] = value;
            }
            return board;
        } else {
            throw new InvalidSudoku("There is no answer for this Sudoku");
        }
    }

    private void firstInsertion(){
        for(int row=0; row<size; row++){
            for(int column=0; column<size; column++){
                if(board[row][column] != 0){
                    int verify = result.size()+1;
                    ColumnHead columnHead = columnHeadRoot;
                    while (verify!= result.size()){
                        if( findLine(columnHead.downNode) == row){
                            Node node = columnHead.downNode;
                            if(findColumn(node.rightNode) == column){
                                do{
                                    if(findValue(node.rightNode.rightNode) == board[row][column]){
                                        ColumnHead insertColumnHead = node.head;
                                        insertColumnHead.choice = Integer.MAX_VALUE;
                                        cover(insertColumnHead);
                                        result.add(node);

                                        Node controlNode = node.rightNode;
                                        while(controlNode!=node){
                                            cover(controlNode.head);
                                            controlNode = controlNode.rightNode;
                                        }
                                        break;
                                    }
                                    node = node.downNode;
                                }while(node != columnHead);
                            }

                        }
                        columnHead = (ColumnHead) columnHead.rightNode;
                    }
                }
            }
        }
    }

    private boolean findAnswer(){
        while (columnHeadRoot != columnHeadRoot.rightNode){
            
            ColumnHead columnHead = findLowestSize();
            Node node = columnHead;

            if( columnHead == columnHead.downNode){
                node = result.get(result.size() -1);
                while(node.head.choice == node.head.size){
                    makeUncover(node);
                    node.head.choice = 0;
                    node = result.get(result.size() -1);
                }
                
                if(node.head.choice == Integer.MAX_VALUE)
                    return false;

                makeUncover(node);
            }

            if(node != columnHead)
                columnHead = node.head;

            columnHead.choice = columnHead.choice +1;
            node = node.downNode;
            makeCover(node);

        }
        return true;
    }

    private ColumnHead findLowestSize(){
        ColumnHead auxColumnHead = columnHeadRoot;
        ColumnHead choseOne = auxColumnHead;
        do{
            if(auxColumnHead.size < choseOne.size)
                choseOne = auxColumnHead;
            if(auxColumnHead.size == 1)
                break;
            auxColumnHead = (ColumnHead) auxColumnHead.rightNode;
        }while (auxColumnHead != columnHeadRoot);
        return choseOne;
    }

    private void makeCover(Node node){
        cover(node.head);
        result.add(node);
        Node controlNode = node.rightNode;
        while(node != controlNode){
            cover(controlNode.head);
            controlNode = controlNode.rightNode;
        }
    }

    private void makeUncover(Node node){
        result.remove(node);
        Node controlNode = node.leftNode;
        while(controlNode != node){
            uncover(controlNode.head);
            controlNode = controlNode.leftNode;
        }
        uncover(node.head);
    }

    private void cover(ColumnHead columnHead){
        if(columnHead == columnHeadRoot)
            columnHeadRoot = (ColumnHead) columnHeadRoot.rightNode;

        columnHead.leftNode.rightNode = columnHead.rightNode;
        columnHead.rightNode.leftNode = columnHead.leftNode;

        Node node = columnHead.downNode;
        while(columnHead != node){
            Node controlNode = node.rightNode;
            while(controlNode != node){
                controlNode.upNode.downNode = controlNode.downNode;
                controlNode.downNode.upNode = controlNode.upNode;
                controlNode.head.size = controlNode.head.size - 1;
                controlNode = controlNode.rightNode;
            }
            node = node.downNode;
        }
    }

    private void uncover(ColumnHead columnHead){
        Node node = columnHead.upNode;
        while(columnHead != node){
            Node controlNode = node.leftNode;
            while(controlNode != node){
                controlNode.head.size = controlNode.head.size + 1;
                controlNode.upNode.downNode = controlNode;
                controlNode.downNode.upNode = controlNode;
                controlNode = controlNode.leftNode;
            }
            node = node.upNode;
        }
        columnHead.leftNode.rightNode = columnHead;
        columnHead.rightNode.leftNode = columnHead;
    }

    private long findLine(Node node){
        return node.head.name/size;
    }

    private long findColumn(Node node){
        return node.head.name%size;
    }

    private long findValue(Node node){
        return node.head.name%size+1;
    }

}
