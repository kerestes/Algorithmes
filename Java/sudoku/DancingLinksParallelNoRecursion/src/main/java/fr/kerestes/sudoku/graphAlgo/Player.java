package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.exceptions.InvalidSudoku;
import fr.kerestes.sudoku.models.ColumnHead;
import fr.kerestes.sudoku.models.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Player {

    private int base;
    private int size;
    private ColumnHead columnHeadRootRight;
    private ColumnHead columnHeadRootLeft;
    private ExecutorService executor  = Executors.newFixedThreadPool(2);
    private Integer[][] board;
    private List<Node> resultRight;
    private List<Node> resultLeft;

    public Player(int base, ColumnHead columnHeadRootRight, ColumnHead columnHeadRootLeft, Integer[][] board){
        this.columnHeadRootRight = columnHeadRootRight;
        this.columnHeadRootLeft = columnHeadRootLeft;
        this.base=base;
        this.size = base*base;
        this.board = board;
        resultRight = new ArrayList<>(base*base);
        resultLeft = new ArrayList<>(base*base);
        this.columnHeadRootRight = firstInsertion(columnHeadRootRight,resultRight);
        this.columnHeadRootLeft = firstInsertion(columnHeadRootLeft,resultLeft);
    }

    public Integer[][] play() throws InvalidSudoku {
        List<Node> finalNodeList = new ArrayList<>();
        List<Future<Boolean>> futureList = new ArrayList<>();
        futureList.add(searchDown());
        futureList.add(searchUp());

        while(true){
            if(futureList.get(0).isDone()) {
                System.out.println("right result");
                futureList.get(1).cancel(true);
                finalNodeList = resultRight;
                break;
            }
            else if(futureList.get(1).isDone()){
                System.out.println("left result");
                futureList.get(1).cancel(true);
                finalNodeList = resultLeft;
                break;
            }
        }
        Integer[][] board = new Integer[size*size*size][size*size*4];
        for(int i=0; i<size*size; i++){
            int row;
            int column;
            int value;

            //If the node saved is a row
            if(finalNodeList.get(i).head.name<size*size){
                row =  (int) findLine(finalNodeList.get(i));
                column =  (int) findColumn(finalNodeList.get(i).rightNode);
                value = (int) findValue(finalNodeList.get(i).rightNode.rightNode);
            }
            //If the node saved is a column
            else if(finalNodeList.get(i).head.name<size*size*2){
                column =  (int) findColumn(finalNodeList.get(i));
                value = (int) findValue(finalNodeList.get(i).rightNode);
                row =  (int) findLine(finalNodeList.get(i).leftNode);
            }
            //If the node saved is a value
            else if(finalNodeList.get(i).head.name<size*size*3){
                value = (int) findValue(finalNodeList.get(i));
                row =  (int) findLine(finalNodeList.get(i).leftNode.leftNode);
                column =  (int) findColumn(finalNodeList.get(i).leftNode);
            }
            //If the node saved is a square
            else {
                row =  (int) findLine(finalNodeList.get(i).rightNode);
                column =  (int) findColumn(finalNodeList.get(i).leftNode.leftNode);
                value = (int) findValue(finalNodeList.get(i).leftNode);
            }
            board[row][column] = value;
        }
        return board;
    }

    private ColumnHead firstInsertion(ColumnHead columnHeadRoot, List<Node> result){
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
                                        columnHeadRoot = coverRight(insertColumnHead, columnHeadRoot);
                                        result.add(node);

                                        Node controlNode = node.rightNode;
                                        while(controlNode!=node){
                                            columnHeadRoot = coverRight(controlNode.head, columnHeadRoot);
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
        return columnHeadRoot;
    }

    private Future<Boolean> searchDown(){
        return executor.submit(() -> {
            while (columnHeadRootRight != columnHeadRootRight.rightNode){

                ColumnHead columnHead = findLowestSize(columnHeadRootRight);
                Node node = columnHead;

                if( columnHead == columnHead.downNode){
                    node = resultRight.get(resultRight.size() -1);
                    while(node.head.choice == node.head.size){
                        makeUncoverLeft(node);
                        node.head.choice = 0;
                        node = resultRight.get(resultRight.size() -1);
                    }

                    if(node.head.choice == Integer.MAX_VALUE)
                        return false;

                    makeUncoverLeft(node);
                }

                if(node != columnHead)
                    columnHead = node.head;

                columnHead.choice = columnHead.choice +1;
                node = node.downNode;
                columnHeadRootRight = makeCoverRight(node, columnHeadRootRight);

            }
            return true;
        });
    }

    private Future<Boolean> searchUp(){
        return executor.submit(() -> {
            while (columnHeadRootLeft != columnHeadRootLeft.rightNode){

                ColumnHead columnHead = findLowestSize(columnHeadRootLeft);
                Node node = columnHead;

                if( columnHead == columnHead.upNode){
                    node = resultLeft.get(resultLeft.size() -1);
                    while(node.head.choice == node.head.size){
                        makeUncoverRight(node);
                        node.head.choice = 0;
                        node = resultLeft.get(resultLeft.size() -1);
                    }

                    if(node.head.choice == Integer.MAX_VALUE)
                        return false;

                    makeUncoverRight(node);
                }

                if(node != columnHead)
                    columnHead = node.head;

                columnHead.choice = columnHead.choice +1;
                node = node.upNode;
                columnHeadRootLeft = makeCoverLeft(node, columnHeadRootLeft);

            }
            return true;
        });
    }

    private ColumnHead findLowestSize(ColumnHead columnHeadRoot){
        ColumnHead auxColumnHead = columnHeadRoot;
        ColumnHead choseOne = auxColumnHead;
        do{
            if(auxColumnHead.size < choseOne.size)
                choseOne = auxColumnHead;
            if(auxColumnHead.size == 0)
                break;
            auxColumnHead = (ColumnHead) auxColumnHead.rightNode;
        }while (auxColumnHead != columnHeadRoot);
        return choseOne;
    }

    private ColumnHead makeCoverRight(Node node, ColumnHead columnHeadRoot){
        columnHeadRoot = coverRight(node.head, columnHeadRoot);
        resultRight.add(node);
        Node controlNode = node.rightNode;
        while(node != controlNode){
            columnHeadRoot = coverRight(controlNode.head, columnHeadRoot);
            controlNode = controlNode.rightNode;
        }
        return columnHeadRoot;
    }

    private ColumnHead makeCoverLeft(Node node, ColumnHead columnHeadRoot){
        columnHeadRoot = coverLeft(node.head, columnHeadRoot);
        resultLeft.add(node);
        Node controlNode = node.leftNode;
        while(node != controlNode){
            columnHeadRoot = coverLeft(controlNode.head, columnHeadRoot);
            controlNode = controlNode.leftNode;
        }
        return columnHeadRoot;
    }

    private void makeUncoverLeft(Node node){
        resultRight.remove(node);
        Node controlNode = node.leftNode;
        while(controlNode != node){
            uncoverLeft(controlNode.head);
            controlNode = controlNode.leftNode;
        }
        uncoverLeft(node.head);
    }

    private void makeUncoverRight(Node node){
        resultLeft.remove(node);
        Node controlNode = node.rightNode;
        while(controlNode != node){
            uncoverRight(controlNode.head);
            controlNode = controlNode.rightNode;
        }
        uncoverRight(node.head);
    }

    private ColumnHead coverRight(ColumnHead columnHead, ColumnHead columnHeadRoot){
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
        return columnHeadRoot;
    }

    private ColumnHead coverLeft(ColumnHead columnHead, ColumnHead columnHeadRoot){
        if(columnHead == columnHeadRoot)
            columnHeadRoot = (ColumnHead) columnHeadRoot.leftNode;

        columnHead.leftNode.rightNode = columnHead.rightNode;
        columnHead.rightNode.leftNode = columnHead.leftNode;

        Node node = columnHead.upNode;
        while(columnHead != node){
            Node controlNode = node.leftNode;
            while(controlNode != node){
                controlNode.upNode.downNode = controlNode.downNode;
                controlNode.downNode.upNode = controlNode.upNode;
                controlNode.head.size = controlNode.head.size - 1;
                controlNode = controlNode.leftNode;
            }
            node = node.upNode;
        }
        return columnHeadRoot;
    }

    private void uncoverLeft(ColumnHead columnHead){
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

    private void uncoverRight(ColumnHead columnHead){
        Node node = columnHead.downNode;
        while(columnHead != node){
            Node controlNode = node.rightNode;
            while(controlNode != node){
                controlNode.head.size = controlNode.head.size + 1;
                controlNode.upNode.downNode = controlNode;
                controlNode.downNode.upNode = controlNode;
                controlNode = controlNode.rightNode;
            }
            node = node.downNode;
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
