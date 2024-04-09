package fr.kerestes.sudoku.models;

import java.util.ArrayList;
import java.util.List;

public class Node {
    Long value;
    private Node rightNode;
    private Node leftNode;
    private Node upNode;
    private Node downNode;
    private ColumnHead head;

    public Node(){

    }

    public Node(Long value){
        this.value = value;
    }
    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public Node getUpNode() {
        return upNode;
    }

    public void setUpNode(Node upNode) {
        this.upNode = upNode;
    }

    public Node getDownNode() {
        return downNode;
    }

    public void setDownNode(Node downNode) {
        this.downNode = downNode;
    }

    public ColumnHead getHead() {
        return head;
    }

    public void setHead(ColumnHead head) {
        this.head = head;
    }
}
