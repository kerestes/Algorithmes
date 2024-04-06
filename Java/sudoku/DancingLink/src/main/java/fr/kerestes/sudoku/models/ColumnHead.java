package fr.kerestes.sudoku.models;

import java.util.List;
import java.util.Optional;

public class ColumnHead {

    private Node[] matrix;

    private ColumnHead previous;
    private ColumnHead next;

    private Optional<String> name = Optional.empty();

    public ColumnHead(int size){
        matrix = new Node[size];
    }
    public ColumnHead(String name){
        this.name = Optional.ofNullable(name);
    }


    public ColumnHead getPrevious() {
        return previous;
    }

    public void setPrevious(ColumnHead previous) {
        this.previous = previous;
    }

    public ColumnHead getNext() {
        return next;
    }

    public void setNext(ColumnHead next) {
        this.next = next;
    }

    public void setMatrix(Node[] matrix) {
        this.matrix = matrix;
    }

    public Node[] getMatrix() {
        return matrix;
    }

    public Optional<String> getName() {
        return name;
    }
}
