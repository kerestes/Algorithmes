package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.models.ColumnHead;
import fr.kerestes.sudoku.models.Node;

public class SudokuGraph {
    private int base;
    private int size;
    private ColumnHead columnHeadRoot;

    public SudokuGraph(int base){
        this.base = base;
        this.size = base*base;
    }

    public ColumnHead createLinkedList(){
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

        return createColumnHeadLinkedList(baseNodeTable);
    }

    private ColumnHead createColumnHeadLinkedList(Node[][] baseNodeTable){
        for(int columnHeadCount=0; columnHeadCount<size*size*4; columnHeadCount++){
            ColumnHead newColumnHead = new ColumnHead(columnHeadCount);
            newColumnHead.size = size;

            //Create the Headers for each column
            if(columnHeadCount == 0){
                columnHeadRoot = newColumnHead;
                newColumnHead.leftNode = newColumnHead;
                newColumnHead.rightNode = newColumnHead;
                newColumnHead.upNode = newColumnHead;
                newColumnHead.downNode = newColumnHead;
            } else {
                newColumnHead.rightNode = columnHeadRoot;
                newColumnHead.leftNode = columnHeadRoot.leftNode;
                columnHeadRoot.leftNode.rightNode = newColumnHead;
                columnHeadRoot.leftNode = newColumnHead;
                newColumnHead.upNode = newColumnHead;
                newColumnHead.downNode = newColumnHead;
            }

            //Connect nodes to Headers if there is a value
            for(int row=0; row<size*size*size; row++){
                if(baseNodeTable[row][columnHeadCount] != null){
                    baseNodeTable[row][columnHeadCount].head = newColumnHead;
                    baseNodeTable[row][columnHeadCount].downNode = newColumnHead;
                    baseNodeTable[row][columnHeadCount].upNode = newColumnHead.upNode;
                    newColumnHead.upNode.downNode = baseNodeTable[row][columnHeadCount];
                    newColumnHead.upNode = baseNodeTable[row][columnHeadCount];
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
                        rootNode.rightNode = rootNode;
                        rootNode.leftNode = rootNode;
                    }else{
                        baseNodeTable[row][column].rightNode = rootNode;
                        baseNodeTable[row][column].leftNode = rootNode.leftNode;
                        rootNode.leftNode.rightNode = baseNodeTable[row][column];
                        rootNode.leftNode = baseNodeTable[row][column];
                    }
                }
            }
        }
        return columnHeadRoot;
    }
}
