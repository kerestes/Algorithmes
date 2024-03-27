package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.model.Node;
import fr.kerestes.sudoku.repositories.SudokuRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuBoard {

    private int base;
    private SudokuRepository sudokuRepository;
    private Node[][] nodes;

    public SudokuBoard(String filePath){
        sudokuRepository = new SudokuRepository(filePath);

        List<String> nodesCoordinate = sudokuRepository.readFile();

        this.base=Integer.parseInt(nodesCoordinate.get(0));
        nodes = new Node[base*base][base*base];
        createGraph();
        fillBoard(nodesCoordinate);


    }

    private void createGraph(){
        List<Integer> possibilities = new ArrayList<>();
        for(int i=1; i<base*base +1; i++){
            possibilities.add(i);
        }
        for(int i=0; i< base*base; i++){
            for(int j=0; j<base*base; j++){
                nodes[i][j] = new Node(possibilities);
            }
        }
        connectingNeighbors(nodes);
    }

    private Node[][] cloneArrayNodes(Node[][] original){
        Node[][] copy = new Node[original.length][];
        for(int i=0; i< original.length; i++){
            copy[i] = new Node[original[i].length];
            for(int j=0; j<original[i].length; j++){
                copy[i][j] = new Node();
                copy[i][j].setValue(original[i][j].getValue());
                copy[i][j].setPossibilities(original[i][j].getPossibilities());
            }
        }
        connectingNeighbors(copy);
        return copy;
    }

    private void connectingNeighbors(Node[][] nodesToBeConnected){
        for(int i=0; i<base*base; i++){
            for(int j=0; j<base*base; j++){
                for (int k=0; k<3; k++){
                    for(int l=0; l<base*base; l++){
                        switch (k){
                            case 0:
                                if(l!=j)
                                    nodesToBeConnected[i][j].getConnectedNodes().add(nodesToBeConnected[i][l]);
                                break;
                            case 1:
                                if(l!=i)
                                    nodesToBeConnected[i][j].getConnectedNodes().add(nodesToBeConnected[l][j]);
                                break;
                            case 2:
                                int factorLine = i/base;
                                int facteurColumn = j/base;
                                if(i == l/base + (base * factorLine) && j == l%base + (base * facteurColumn))
                                    continue;
                                if(!nodesToBeConnected[i][j].getConnectedNodes().contains(nodesToBeConnected[l/base + (base * factorLine)] [l%base + (base * facteurColumn)]))
                                    nodesToBeConnected[i][j].getConnectedNodes().add(nodesToBeConnected[l/base + (base * factorLine)] [l%base + (base * facteurColumn)]);
                                break;
                        }
                    }
                }
            }
        }
    }

    public void fillBoard(List<String> nodesCoordinate){
        for(int i=1; i<nodesCoordinate.size(); i++){
            String [] coord = nodesCoordinate.get(i).split(",");
            nodes[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])].setValue(Integer.parseInt(coord[2]));
        }
    }

    public void printSudoku(){
        String line = "";
        for (int i=0; i<base*base; i++){
            line = "| ";
            for(int j=0; j<base*base; j++){
                if(nodes[i][j].getValue() == 0)
                    line += "  | ";
                else
                    line += nodes[i][j].getValue() + " | ";
            }
            System.out.println(line);
        }
    }

    public void startResolution(){
        nodes = graphColoringAndBackTracking(cloneArrayNodes(nodes));
        if(nodes != null)
            printSudoku();
        else
            System.out.println("Invalid Sudoku");
    }

    private Node[][] graphColoringAndBackTracking(Node[][] copyNodes){
        boolean test = false;
        Integer nodesAccumulationCheck = Integer.MAX_VALUE;
        while (true){

            Integer nodesAccumulation = graphColoring(copyNodes);

            if (nodesAccumulation == nodesAccumulationCheck){
                if(Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).noneMatch(node -> node.getPossibilities().size() > 1)){
                    if (Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).anyMatch(node -> node.getValue() == 0)
                            || Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).anyMatch(node -> node.getPossibilities().size() != 1))
                        return null;
                    else
                        return copyNodes;
                } else {
                    if (Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).anyMatch(node -> node.getPossibilities().size() == 0)
                            || Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).noneMatch(node -> node.getPossibilities().size() > 0))
                        return null;
                    else{
                        for(int i = 0; i <base*base; i++){
                            for(int j=0; j<base*base; j++){
                                if(copyNodes[i][j].getValue() == 0){
                                    for(int k=0; k<copyNodes[i][j].getPossibilities().size(); k++ ){
                                        Node[][] copyNodes2 = cloneArrayNodes(copyNodes);
                                        copyNodes2[i][j].setValue(copyNodes[i][j].getPossibilities().get(k).intValue());
                                        Node [][] tempCopy = graphColoringAndBackTracking(copyNodes2);
                                        if (tempCopy != null) {
                                            return tempCopy;
                                        }
                                    }
                                    return null;
                                }
                            }
                        }
                    }
                }
            } else {
                nodesAccumulationCheck = nodesAccumulation;
            }
        }
    }

    private int graphColoring(Node[][] copyNodes){
        int nodesAccumulation = 0;
        for(int i=0; i<base*base; i++){
            for(int j=0; j<base*base; j++){
                if (copyNodes[i][j].getValue() != 0)
                    continue;
                for(int k=0; k<copyNodes[i][j].getConnectedNodes().size(); k++){
                    if(copyNodes[i][j].getConnectedNodes().get(k).getValue() != 0 && copyNodes[i][j].getPossibilities().contains(copyNodes[i][j].getConnectedNodes().get(k).getValue()))
                        copyNodes[i][j].getPossibilities().remove(copyNodes[i][j].getConnectedNodes().get(k).getValue());
                }
                if(copyNodes[i][j].getPossibilities().size() == 1) {
                    copyNodes[i][j].setValue(copyNodes[i][j].getPossibilities().get(0));
                } else {
                    nodesAccumulation++;
                }
            }
        }
        return nodesAccumulation;
    }

}
