package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;
import fr.kerestes.sudoku.models.ColumnHead;
import fr.kerestes.sudoku.models.Node;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int base;
    private int size;
    private Integer[][] board;
    private ColumnHead columnHeadRoot;
    List<Node> result;

    public Player(int base, Integer[][] board){
        this.base=base;
        this.size = base*base;
        this.board=board;
        result = new ArrayList<>();
        createLinkedList();
        firstInsertion();
    }

    public Integer[][] play() throws InvalidSudoku {
        if(findAnswer()){
            for(int i=0; i<size*size; i++){
                int row;
                int column;
                int value;

                //If the node saved is a row
                if(result.get(i).getValue()<size*size){
                    row =  (int) findLine(result.get(i));
                    column =  (int) findColumn(result.get(i).getRightNode());
                    value = (int) findValue(result.get(i).getRightNode().getRightNode());
                }
                //If the node saved is a column
                else if(result.get(i).getValue()<size*size*2){
                    column =  (int) findColumn(result.get(i));
                    value = (int) findValue(result.get(i).getRightNode());
                    row =  (int) findLine(result.get(i).getLeftNode());
                }
                //If the node saved is a value
                else if(result.get(i).getValue()<size*size*3){
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


    private void createLinkedList(){
        Node[][] baseNodeTable = new Node[size*size*size][size*size*4];

        for(int line=0; line<size*size*size; line++){
            //To understand these calculations, look at the README table
            int row= line%(size*size);
            int column= line%size + (size * (line/(size*size)));

            int value;
            if(line%size + line/(size*size) >= size)
                value = line%(size*size) + line/(size*size) - size;
            else
                value = line%(size*size) + line/(size*size);

            int tempSquare = ((row/size)/base)*base + (column%size)/base;
            int square = value%size + (tempSquare * size);

            baseNodeTable[line][row] = new Node((long) row);
            baseNodeTable[line][column + size*size] = new Node((long) column + size*size);
            baseNodeTable[line][value + size*size*2] = new Node((long) value + size*size*2);
            baseNodeTable[line][square + size*size*3] = new Node((long) square + size*size*3);
        }

        createColumnHeadLinkedList(baseNodeTable);
    }

    private void createColumnHeadLinkedList(Node[][] baseNodeTable){
        for(int columnHeadCount=0; columnHeadCount<size*size*4; columnHeadCount++){
            ColumnHead newColumnHead = new ColumnHead();
            newColumnHead.setSize(size);

            //Create the Headers for each column
            if(columnHeadCount == 0){
                columnHeadRoot = newColumnHead;
                newColumnHead.setLeftNode(newColumnHead);
                newColumnHead.setRightNode(newColumnHead);
                newColumnHead.setUpNode(newColumnHead);
                newColumnHead.setDownNode(newColumnHead);
            } else {
                newColumnHead.setRightNode(columnHeadRoot);
                newColumnHead.setLeftNode(columnHeadRoot.getLeftNode());
                columnHeadRoot.getLeftNode().setRightNode(newColumnHead);
                columnHeadRoot.setLeftNode(newColumnHead);
                newColumnHead.setUpNode(newColumnHead);
                newColumnHead.setDownNode(newColumnHead);
            }

            //Connect nodes to Headers if there is a value
            for(int row=0; row<size*size*size; row++){
                if(baseNodeTable[row][columnHeadCount] != null){
                    baseNodeTable[row][columnHeadCount].setHead(newColumnHead);
                    baseNodeTable[row][columnHeadCount].setDownNode(newColumnHead);
                    baseNodeTable[row][columnHeadCount].setUpNode(newColumnHead.getUpNode());
                    newColumnHead.getUpNode().setDownNode(baseNodeTable[row][columnHeadCount]);
                    newColumnHead.setUpNode(baseNodeTable[row][columnHeadCount]);
                }
            }
        }

        //Connect nodes sideways
        for(int row=0; row<size*size*size; row++){
            Node rootNode = null;
            for(int column=0; column<size*size*4; column++){
                if(baseNodeTable[row][column] != null){
                    if(rootNode == null){
                        rootNode = baseNodeTable[row][column];
                        rootNode.setRightNode(rootNode);
                        rootNode.setLeftNode(rootNode);
                    }else{
                        baseNodeTable[row][column].setRightNode(rootNode);
                        baseNodeTable[row][column].setLeftNode(rootNode.getLeftNode());
                        rootNode.getLeftNode().setRightNode(baseNodeTable[row][column]);
                        rootNode.setLeftNode(baseNodeTable[row][column]);
                    }
                }
            }
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
                                        result.add(node);
                                        cover(node);
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
        int verifyLinearCall = result.size();
        while (columnHeadRoot != columnHeadRoot.getRightNode()){
            ColumnHead columnHead = findLowerSize();

            //This line avoids the recursive processing of columns that only have one option and therefore must be chosen.
            if(columnHead.getSize() == 1 && columnHead != columnHead.getDownNode()){
                result.add(columnHead.getDownNode());
                cover(columnHead.getDownNode());
            } else{
                if(columnHeadRoot != columnHeadRoot.getRightNode()){
                    Node node = columnHead.getDownNode();
                    while(node != columnHead){
                        result.add(node);
                        cover(node);
                        if(findAnswer()){
                            return true;
                        }
                        result.remove(node);
                        uncover(node);
                        node = node.getDownNode();
                    }
                }
                //however, if the linear processing of columns with an option occurs
                // within a recursive call, it must be undone if the return is false
                for(int i= result.size(); i> verifyLinearCall ; i--){
                    Node tempNode = result.get(i-1);
                    result.remove(tempNode);
                    uncover(tempNode);
                }
                return false;
            }
        }
        if(result.size() == size*size){
            return true;
        } else{
            return false;
        }
    }

    private ColumnHead findLowerSize(){
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

    private void cover(Node node){
        ColumnHead rootColumn = node.getHead();
        ColumnHead verifyRootColumn = rootColumn;
        do{
            coverHead(verifyRootColumn);
            Node iterateNode = verifyRootColumn.getDownNode();
            while (iterateNode != verifyRootColumn){
                if(iterateNode != node)
                    coverNode(iterateNode);
                iterateNode = iterateNode.getDownNode();
            }
            node = node.getRightNode();
            verifyRootColumn = node.getHead();
        }while(rootColumn != verifyRootColumn);
    }

    private void coverHead(ColumnHead columnHead){
        if(columnHead == columnHeadRoot)
            columnHeadRoot = (ColumnHead) columnHead.getRightNode();
        columnHead.getRightNode().setLeftNode(columnHead.getLeftNode());
        columnHead.getLeftNode().setRightNode(columnHead.getRightNode());
    }

    private void coverNode(Node node){
        Node verifyNode = node.getRightNode();
        while (verifyNode != node){
            verifyNode.getUpNode().setDownNode(verifyNode.getDownNode());
            verifyNode.getDownNode().setUpNode(verifyNode.getUpNode());
            verifyNode.getHead().setSize(verifyNode.getHead().getSize() -1 );
            verifyNode = verifyNode.getRightNode();
        }
    }

    private void uncover(Node node){
        ColumnHead rootColumn = node.getHead();
        ColumnHead verifyRootColumn = rootColumn;
        do{
            Node iterateNode = verifyRootColumn.getUpNode();
            while (iterateNode != verifyRootColumn){
                if(iterateNode != node)
                    uncoverNode(iterateNode);
                iterateNode = iterateNode.getUpNode();
            }
            uncoverHead(verifyRootColumn);
            node = node.getLeftNode();
            verifyRootColumn = node.getHead();
        }while(rootColumn != verifyRootColumn);
    }

    private void uncoverHead(ColumnHead columnHead){
        if(columnHead == columnHeadRoot)
            columnHeadRoot = (ColumnHead) columnHead.getLeftNode();
        columnHead.getRightNode().setLeftNode(columnHead);
        columnHead.getLeftNode().setRightNode(columnHead);
    }

    private void uncoverNode(Node node){
        Node verifyNode = node.getLeftNode();
        while (verifyNode != node){
            verifyNode.getUpNode().setDownNode(verifyNode);
            verifyNode.getDownNode().setUpNode(verifyNode);
            verifyNode.getHead().setSize(verifyNode.getHead().getSize() +1 );
            verifyNode = verifyNode.getLeftNode();
        }
    }
    private long findLine(Node node){
        return node.getValue()/size;
    }

    private long findColumn(Node node){
        return node.getValue()%size;
    }

    private long findValue(Node node){
        return node.getValue()%size+1;
    }

}
