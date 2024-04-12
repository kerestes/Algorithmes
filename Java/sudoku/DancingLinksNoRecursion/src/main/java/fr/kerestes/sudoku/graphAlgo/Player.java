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
        result = new ArrayList<>();
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
                if(result.get(i).getHead().getName()<size*size){
                    row =  (int) findLine(result.get(i));
                    column =  (int) findColumn(result.get(i).getRightNode());
                    value = (int) findValue(result.get(i).getRightNode().getRightNode());
                }
                //If the node saved is a column
                else if(result.get(i).getHead().getName()<size*size*2){
                    column =  (int) findColumn(result.get(i));
                    value = (int) findValue(result.get(i).getRightNode());
                    row =  (int) findLine(result.get(i).getLeftNode());
                }
                //If the node saved is a value
                else if(result.get(i).getHead().getName()<size*size*3){
                    value = (int) findValue(result.get(i));
                    row =  (int) findLine(result.get(i).getLeftNode().getLeftNode());
                    column =  (int) findColumn(result.get(i).getLeftNode());
                }
                //If the node saved is a square
                else {
                    row =  (int) findLine(result.get(i).getRightNode());
                    column =  (int) findColumn(result.get(i).getLeftNode().getLeftNode());
                    value = (int) findValue(result.get(i).getLeftNode());
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
                        if( findLine(columnHead.getDownNode()) == row){
                            Node node = columnHead.getDownNode();
                            if(findColumn(node.getRightNode()) == column){
                                do{
                                    if(findValue(node.getRightNode().getRightNode()) == board[row][column]){
                                        ColumnHead insertColumnHead = node.getHead();
                                        insertColumnHead.setChoice(Integer.MAX_VALUE);
                                        cover(insertColumnHead);
                                        result.add(node);

                                        Node controlNode = node.getRightNode();
                                        while(controlNode!=node){
                                            cover(controlNode.getHead());
                                            controlNode = controlNode.getRightNode();
                                        }
                                        break;
                                    }
                                    node = node.getDownNode();
                                }while(node != columnHead);
                            }

                        }
                        columnHead = (ColumnHead) columnHead.getRightNode();
                    }
                }
            }
        }
    }

    private boolean findAnswer(){
        while (columnHeadRoot != columnHeadRoot.getRightNode() || result.size() != size*size){
            
            ColumnHead columnHead = findLowestSize();
            Node node = columnHead;

            if((columnHead == columnHead.getRightNode() && result.size() != size*size) || columnHead == columnHead.getDownNode()){
                node = result.get(result.size() -1);
                while(node.getHead().getChoice() == node.getHead().getSize()){
                    makeUncover(node);
                    node.getHead().setChoice(0);
                    node = result.get(result.size() -1);
                }
                
                if(node.getHead().getChoice() == Integer.MAX_VALUE)
                    return false;

                makeUncover(node);
            }

            if(node != columnHead)
                columnHead = node.getHead();

            columnHead.setChoice(columnHead.getChoice() +1);
            node = columnHead;
            for(int i=0; i<columnHead.getChoice(); i++){
                node = node.getDownNode();
            }
            makeCover(node);

        }
        return true;
    }

    private ColumnHead findLowestSize(){
        ColumnHead auxColumnHead = columnHeadRoot;
        ColumnHead choseOne = auxColumnHead;
        do{
            if(auxColumnHead.getSize() < choseOne.getSize())
                choseOne = auxColumnHead;
            if(auxColumnHead.getSize() == 1)
                break;
            auxColumnHead = (ColumnHead) auxColumnHead.getRightNode();
        }while (auxColumnHead != columnHeadRoot);
        return choseOne;
    }

    private void makeCover(Node node){
        cover(node.getHead());
        result.add(node);
        Node controlNode = node.getRightNode();
        while(node != controlNode){
            cover(controlNode.getHead());
            controlNode = controlNode.getRightNode();
        }
    }

    private void makeUncover(Node node){
        result.remove(node);
        Node controlNode = node.getLeftNode();
        while(controlNode != node){
            uncover(controlNode.getHead());
            controlNode = controlNode.getLeftNode();
        }
        uncover(node.getHead());
    }

    private void cover(ColumnHead columnHead){
        if(columnHead == columnHeadRoot)
            columnHeadRoot = (ColumnHead) columnHeadRoot.getRightNode();

        columnHead.getLeftNode().setRightNode(columnHead.getRightNode());
        columnHead.getRightNode().setLeftNode(columnHead.getLeftNode());

        Node node = columnHead.getDownNode();
        while(columnHead != node){
            Node controlNode = node.getRightNode();
            while(controlNode != node){
                controlNode.getUpNode().setDownNode(controlNode.getDownNode());
                controlNode.getDownNode().setUpNode(controlNode.getUpNode());
                controlNode.getHead().setSize(controlNode.getHead().getSize() - 1);
                controlNode = controlNode.getRightNode();
            }
            node = node.getDownNode();
        }
    }

    private void uncover(ColumnHead columnHead){
        Node node = columnHead.getUpNode();
        while(columnHead != node){
            Node controlNode = node.getLeftNode();
            while(controlNode != node){
                controlNode.getHead().setSize(controlNode.getHead().getSize() + 1);
                controlNode.getUpNode().setDownNode(controlNode);
                controlNode.getDownNode().setUpNode(controlNode);
                controlNode = controlNode.getLeftNode();
            }
            node = node.getUpNode();
        }
        columnHead.getLeftNode().setRightNode(columnHead);
        columnHead.getRightNode().setLeftNode(columnHead);
    }

    private long findLine(Node node){
        return node.getHead().getName()/size;
    }

    private long findColumn(Node node){
        return node.getHead().getName()%size;
    }

    private long findValue(Node node){
        return node.getHead().getName()%size+1;
    }

}
