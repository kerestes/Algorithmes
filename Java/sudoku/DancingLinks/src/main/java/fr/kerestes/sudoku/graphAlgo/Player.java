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
        if (columnHeadRoot != columnHeadRoot.getRightNode()){ // O(1)
            ColumnHead columnHead = findLowestSize(); // O(1) - O(n)
            cover(columnHead); // O(base² -1)  - O(base⁴ -2*base² +1)
            Node node = columnHead.getDownNode(); // O(1)
            while(node != columnHead){ //0(1) - O(base²)
                result.add(node); // O(1)
                Node controlNode = node.getRightNode(); // O(1)
                while(node != controlNode){ // O(base² -1)
                    cover(controlNode.getHead()); // O(base² -1)  - O(base⁴ -2*base² +1)
                    controlNode = controlNode.getRightNode(); // O(1)
                }
                if(findAnswer()){ // T(n-4)
                    return true;
                }
                result.remove(node); // O(1)
                controlNode = node.getLeftNode(); // O(1)
                while(controlNode != node){ // O(base² -1)
                    uncover(controlNode.getHead()); // O(base² -1)  - O(base⁴ -2*base² +1)
                    controlNode = controlNode.getLeftNode(); // O(1)
                }
                node = node.getDownNode(); // O(1)
            }
            uncover(columnHead); // O(base⁴)
            return false;
        }
        return true; // O(1)
    }

    private ColumnHead findLowestSize(){
        ColumnHead auxColumnHead = columnHeadRoot; //O(1)
        ColumnHead choseOne = auxColumnHead; //O(1)
        do{
            if(auxColumnHead.getSize() < choseOne.getSize()) //O(1)
                choseOne = auxColumnHead; //O(1)
            if(auxColumnHead.getSize() == 1) //O(1)
                break; //O(1)
            auxColumnHead = (ColumnHead) auxColumnHead.getRightNode(); //O(1)
        }while (auxColumnHead != columnHeadRoot); // O(1) - O(n)
        return choseOne; //O(1)
    }

    private void cover(ColumnHead columnHead){ // O(base² -1)  - O(base⁴ -2*base² +1)
        if(columnHead == columnHeadRoot) //O(1)
            columnHeadRoot = (ColumnHead) columnHeadRoot.getRightNode(); //O(1)

        columnHead.getLeftNode().setRightNode(columnHead.getRightNode()); //O(1)
        columnHead.getRightNode().setLeftNode(columnHead.getLeftNode()); //O(1)

        Node node = columnHead.getDownNode(); //O(1)
        while(columnHead != node){ //O(1) - O(base² -1)
            Node controlNode = node.getRightNode(); //O(1)
            while(controlNode != node){ //O(base² -1)
                controlNode.getUpNode().setDownNode(controlNode.getDownNode()); //O(1)
                controlNode.getDownNode().setUpNode(controlNode.getUpNode()); //O(1)
                controlNode.getHead().setSize(controlNode.getHead().getSize() - 1); //O(1)
                controlNode = controlNode.getRightNode();
            }
            node = node.getDownNode(); //O(1)
        }
    }

    private void uncover(ColumnHead columnHead){ // O(base² -1)  - O(base⁴ -2*base² +1)
        Node node = columnHead.getUpNode(); // O(1)
        while(columnHead != node){ // O(1) - O(base² -1)
            Node controlNode = node.getLeftNode(); // O(1)
            while(controlNode != node){ // O(base² -1)
                controlNode.getHead().setSize(controlNode.getHead().getSize() + 1); // O(1)
                controlNode.getUpNode().setDownNode(controlNode); // O(1)
                controlNode.getDownNode().setUpNode(controlNode); // O(1)
                controlNode = controlNode.getLeftNode(); // O(1)
            }
            node = node.getUpNode(); // O(1)
        }
        columnHead.getLeftNode().setRightNode(columnHead); // O(1)
        columnHead.getRightNode().setLeftNode(columnHead); // O(1)
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
