package fr.kerestes.sudoku.models;

import java.util.ArrayList;
import java.util.List;

public class Node {
    long value;
    List<ColumnHead> connectedList;

    public Node(long value){
        this.value = value;
        connectedList = new ArrayList<>();
    }

    public long getValue() {
        return value;
    }

    public List<ColumnHead> getConnectedList() {
        return connectedList;
    }
}
