package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.model.Node;

import java.util.Arrays;

public class Joueur {

    int base;
    Node[][] nodes;

    public Joueur(int base, Node[][] nodes){
        this.base=base;
        this.nodes=nodes;
    }

    public Node[][] jouer(){
        return graphColoringAndBackTracking(cloneArrayNodes(nodes));
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
        SudokuBoard.connectingNeighbors(copy, base);
        return copy;
    }

    private Node[][] graphColoringAndBackTracking(Node[][] copyNodes){
        Integer nodesWithoutValueCheck = Integer.MAX_VALUE;
        while (true){

            Integer nodesWithoutValue = graphColoring(copyNodes);

            if (nodesWithoutValue == nodesWithoutValueCheck){
                if(verifyAllPossibilitiesEqualsOne(copyNodes)){
                    if (verifyThereIsEmptyPossibilitiesNodeOrAnyValueEqualsZero(copyNodes))
                        return null;
                    else
                        return copyNodes;
                } else {
                    if (verifyPossibilitiesEqualsToZero(copyNodes))
                        return null;
                    else{
                        return backTracking(copyNodes);
                    }
                }
            } else {
                nodesWithoutValueCheck = nodesWithoutValue;
            }
        }
    }

    private int graphColoring(Node[][] copyNodes){
        int nodesWithoutValue = 0;
        for(int i=0; i<base*base; i++){
            for(int j=0; j<base*base; j++){
                if (copyNodes[i][j].getValue() != 0)
                    continue;
                for(int k=0; k<copyNodes[i][j].getConnectedNodes().size(); k++){
                    if(verifyMatchBetweenActualNodeAndNeighbor(copyNodes[i][j], k))
                        copyNodes[i][j].getPossibilities().remove(copyNodes[i][j].getConnectedNodes().get(k).getValue());
                }
                if(verifyNodePossibilitiesEqualsToOne(copyNodes[i][j])) {
                    copyNodes[i][j].setValue(copyNodes[i][j].getPossibilities().get(0));
                } else {
                    nodesWithoutValue++;
                }
            }
        }
        return nodesWithoutValue;
    }

    private Node[][] backTracking(Node[][] copyNodes){
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
        return null;
    }

    private boolean verifyAllPossibilitiesEqualsOne(Node[][] copyNodes){
        return Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).noneMatch(node -> node.getPossibilities().size() > 1);
    }

    private boolean verifyNodePossibilitiesEqualsToOne(Node node){
        return node.getPossibilities().size() == 1;
    }

    private boolean verifyThereIsEmptyPossibilitiesNodeOrAnyValueEqualsZero(Node[][] copyNodes){
        return Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).anyMatch(node -> node.getValue() == 0)
                || verifyPossibilitiesEqualsToZero(copyNodes);
    }

    private boolean verifyPossibilitiesEqualsToZero(Node[][] copyNodes){
        return Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).anyMatch(node -> node.getPossibilities().size() == 0);
    }

    private boolean verifyMatchBetweenActualNodeAndNeighbor(Node nodeOrigin, int positionNeighbor){
        return nodeOrigin.getPossibilities().contains(nodeOrigin.getConnectedNodes().get(positionNeighbor).getValue());
    }
}
