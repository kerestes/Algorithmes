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
                if(result.get(i).head.name<size*size){
                    row =  (int) findLine(result.get(i));
                    column =  (int) findColumn(result.get(i).rightNode);
                    value = (int) findValue(result.get(i).rightNode.rightNode);
                }
                //If the node saved is a column
                else if(result.get(i).head.name<size*size*2){
                    column =  (int) findColumn(result.get(i));
                    value = (int) findValue(result.get(i).rightNode);
                    row =  (int) findLine(result.get(i).leftNode);
                }
                //If the node saved is a value
                else if(result.get(i).head.name<size*size*3){
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
        if (columnHeadRoot != columnHeadRoot.rightNode){ // O(1)
            ColumnHead columnHead = findLowestSize(); // O(1) - O(n)
            cover(columnHead); // O(base² -1)  - O(base⁴ -2*base² +1)
            Node node = columnHead.downNode; // O(1)
            while(node != columnHead){ //0(1) - O(base²)
                result.add(node); // O(1)
                Node controlNode = node.rightNode; // O(1)
                while(node != controlNode){ // O(base² -1)
                    cover(controlNode.head); // O(base² -1)  - O(base⁴ -2*base² +1)
                    controlNode = controlNode.rightNode; // O(1)
                }
                if(findAnswer()){ // T(n-4)
                    return true;
                }
                result.remove(node); // O(1)
                controlNode = node.leftNode; // O(1)
                while(controlNode != node){ // O(base² -1)
                    uncover(controlNode.head); // O(base² -1)  - O(base⁴ -2*base² +1)
                    controlNode = controlNode.leftNode; // O(1)
                }
                node = node.downNode; // O(1)
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
            if(auxColumnHead.size < choseOne.size) //O(1)
                choseOne = auxColumnHead; //O(1)
            if(auxColumnHead.size == 1) //O(1)
                break; //O(1)
            auxColumnHead = (ColumnHead) auxColumnHead.rightNode; //O(1)
        }while (auxColumnHead != columnHeadRoot); // O(1) - O(n)
        return choseOne; //O(1)
    }

    private void cover(ColumnHead columnHead){ // O(base² -1)  - O(base⁴ -2*base² +1)
        if(columnHead == columnHeadRoot) //O(1)
            columnHeadRoot = (ColumnHead) columnHeadRoot.rightNode; //O(1)

        columnHead.leftNode.rightNode = columnHead.rightNode; //O(1)
        columnHead.rightNode.leftNode = columnHead.leftNode; //O(1)

        Node node = columnHead.downNode; //O(1)
        while(columnHead != node){ //O(1) - O(base² -1)
            Node controlNode = node.rightNode; //O(1)
            while(controlNode != node){ //O(base² -1)
                controlNode.upNode.downNode = controlNode.downNode; //O(1)
                controlNode.downNode.upNode = controlNode.upNode; //O(1)
                controlNode.head.size = controlNode.head.size - 1; //O(1)
                controlNode = controlNode.rightNode;
            }
            node = node.downNode; //O(1)
        }
    }

    private void uncover(ColumnHead columnHead){ // O(base² -1)  - O(base⁴ -2*base² +1)
        Node node = columnHead.upNode; // O(1)
        while(columnHead != node){ // O(1) - O(base² -1)
            Node controlNode = node.leftNode; // O(1)
            while(controlNode != node){ // O(base² -1)
                controlNode.head.size = controlNode.head.size + 1; // O(1)
                controlNode.upNode.downNode = controlNode; // O(1)
                controlNode.downNode.upNode = controlNode; // O(1)
                controlNode = controlNode.leftNode; // O(1)
            }
            node = node.upNode; // O(1)
        }
        columnHead.leftNode.rightNode = columnHead; // O(1)
        columnHead.rightNode.leftNode = columnHead; // O(1)
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
