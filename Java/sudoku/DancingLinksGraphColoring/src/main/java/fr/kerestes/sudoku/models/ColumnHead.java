package fr.kerestes.sudoku.models;

import java.util.List;
import java.util.Optional;

public class ColumnHead extends Node{

    private int size;
    private int choice =  0;
    private int name;

    public ColumnHead(int name){
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
