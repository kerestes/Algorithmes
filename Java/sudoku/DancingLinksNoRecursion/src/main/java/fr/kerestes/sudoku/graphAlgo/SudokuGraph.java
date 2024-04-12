package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.models.ColumnHead;
import fr.kerestes.sudoku.models.Node;

public class SudokuGraph {
    private int base;
    private int size;
    private Integer[][] board;
    private ColumnHead columnHeadRoot;

    public SudokuGraph(int base, Integer[][] board){
        this.base = base;
        this.board = board;
        this.size = base*base;
        createLinkedList();
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

            baseNodeTable[line][row] = new Node();
            baseNodeTable[line][column + size*size] = new Node();
            baseNodeTable[line][value + size*size*2] = new Node();
            baseNodeTable[line][square + size*size*3] = new Node();
        }

        createColumnHeadLinkedList(baseNodeTable);
    }

    private void createColumnHeadLinkedList(Node[][] baseNodeTable){
        for(int columnHeadCount=0; columnHeadCount<size*size*4; columnHeadCount++){
            ColumnHead newColumnHead = new ColumnHead(columnHeadCount);
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

    public ColumnHead getColumnHeadRoot() {
        return columnHeadRoot;
    }
}
