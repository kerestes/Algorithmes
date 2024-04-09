package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Player {

    private int base;
    private Node[][] nodes;

    public Player(int base, Node[][] nodes){
        this.base=base;
        this.nodes=nodes;
    }

    public Node[][] play(){
        return graphColoringAndBackTracking(cloneArrayNodes(nodes), null);
    }

    //To pass by value it is necessary to clone the nodes' array
    private Node[][] cloneArrayNodes(Node[][] original){
        Node[][] copy = new Node[original.length][];
        for(int i=0; i< original.length; i++){
            copy[i] = new Node[original[i].length];
            for(int j=0; j<original[i].length; j++){
                copy[i][j] = new Node(i, j);
                copy[i][j].setValue(original[i][j].getValue());
                copy[i][j].setPossibilities(original[i][j].getPossibilities());
            }
        }
        SudokuBoard.connectingNeighbors(copy, base);
        return copy;
    }

    private Node[][] graphColoringAndBackTracking(Node[][] copyNodes, List<Node> listNodePossibilities){
        int nodesWithoutValueCheck = Integer.MAX_VALUE;
        if(listNodePossibilities == null)
            listNodePossibilities = Arrays.stream(copyNodes).flatMap(n -> Arrays.stream(n)).filter(n -> n.getPossibilitiesSize() > 1).collect(Collectors.toList());
        while (true){
            int nodesWithoutValue = graphColoring(copyNodes, listNodePossibilities);
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
                        listNodePossibilities.sort(Comparator.comparing(Node::getPossibilitiesSize));
                        return backTracking(copyNodes, listNodePossibilities);
                    }
                }
            } else {
                nodesWithoutValueCheck = nodesWithoutValue;
            }
        }
    }

    private int graphColoring(Node[][] copyNodes, List<Node> listNodePossibilities){
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
                    if(listNodePossibilities.size()>0){
                        for(int k=0; k<listNodePossibilities.size(); k++){
                            if(listNodePossibilities.get(k).getLine() == i && listNodePossibilities.get(k).getColumn() == j)
                                listNodePossibilities.remove(k);
                        }
                    }
                } else {
                    nodesWithoutValue++;
                }
            }
        }
        return nodesWithoutValue;
    }

    private Node[][] backTracking(Node[][] copyNodes, List<Node> listNodePossibilities){
        Node currentNode = listNodePossibilities.get(0);
        for(int i = 0; i< copyNodes[currentNode.getLine()][currentNode.getColumn()].getPossibilitiesSize(); i++){
            List<Node> newListNodePossibilities = new ArrayList<>(listNodePossibilities);
            newListNodePossibilities.remove(listNodePossibilities.get(0));
            Node[][] copyNodes2 = cloneArrayNodes(copyNodes);
            copyNodes2[currentNode.getLine()][currentNode.getColumn()].setValue(copyNodes[currentNode.getLine()][currentNode.getColumn()].getPossibilities().get(i));
            Node [][] tempCopy = graphColoringAndBackTracking(copyNodes2, newListNodePossibilities);
            if (tempCopy != null) {
                return tempCopy;
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
