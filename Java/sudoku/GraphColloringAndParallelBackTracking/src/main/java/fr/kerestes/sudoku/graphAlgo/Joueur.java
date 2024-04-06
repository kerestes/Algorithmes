package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class Joueur extends RecursiveTask<Node[][]> {

    int base;
    Node[][] nodes;
    List<Node> listNodePossibilities;

    public Joueur(int base, Node[][] nodes){
        listNodePossibilities = new ArrayList<>();
        this.base=base;
        this.nodes=nodes;
    }

    public Joueur(int base, Node[][] nodes, List<Node> newListNodePossibilities){
        listNodePossibilities = new ArrayList<>(newListNodePossibilities);
        this.base=base;
        this.nodes=nodes;
    }

    public Node[][] jouer(){
        return graphColoringAndBackTracking(cloneArrayNodes(nodes), listNodePossibilities);
    }

    public Node[][] getNodes(){
        return nodes;
    }

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
        if(listNodePossibilities.size() == 0)
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
                    listNodePossibilities.remove(copyNodes[i][j]);
                } else {
                    nodesWithoutValue++;
                }
            }
        }
        return nodesWithoutValue;
    }

    private Node[][] backTracking(Node[][] copyNodes, List<Node> listNodePossibilities){
        Node currentNode = listNodePossibilities.get(0);
        List<Node> newListNodePossibilities = new ArrayList<>(listNodePossibilities);
        newListNodePossibilities.remove(listNodePossibilities.get(0));
        List<Joueur> tempListJouer = new ArrayList<>();
        for(int k=0; k<copyNodes[currentNode.getLine()][currentNode.getColumn()].getPossibilitiesSize(); k++ ){
            Node[][] copyNodes2 = cloneArrayNodes(copyNodes);
            copyNodes2[currentNode.getLine()][currentNode.getColumn()].setValue(copyNodes[currentNode.getLine()][currentNode.getColumn()].getPossibilities().get(k));
            tempListJouer.add(new Joueur(base, copyNodes2, newListNodePossibilities));
        }
        List<Node[][]> listNodesArray = ForkJoinTask.invokeAll(tempListJouer).stream().map(ForkJoinTask::join).collect(Collectors.toList());
        for(int k=0; k< listNodesArray.size(); k++){
            if(listNodesArray.get(k) != null)
                return listNodesArray.get(k);
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

    @Override
    protected Node[][] compute() {
        return graphColoringAndBackTracking(cloneArrayNodes(nodes), listNodePossibilities);
    }
}
