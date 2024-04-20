package fr.kerestes.sudoku.models;

public class ColumnHead extends Node{

    public int size;
    public int choice =  0;
    public int name;

    public ColumnHead(int name){
        this.name = name;
    }
}
