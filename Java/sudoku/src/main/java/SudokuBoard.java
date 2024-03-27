import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuBoard {

    private int base;
    private Node[][] nodes;

    public SudokuBoard(int base){
        this.base=base;
        nodes = new Node[base*base][base*base];
        createGraph();

        nodes[0][3].setValue(8);
        nodes[0][5].setValue(1);
        nodes[1][7].setValue(4);
        nodes[1][8].setValue(3);
        nodes[2][0].setValue(5);
        nodes[3][4].setValue(7);
        nodes[3][6].setValue(8);
        nodes[4][6].setValue(1);
        nodes[5][1].setValue(2);
        nodes[5][4].setValue(3);
        nodes[6][0].setValue(6);
        nodes[6][7].setValue(7);
        nodes[6][8].setValue(5);
        nodes[7][2].setValue(3);
        nodes[7][3].setValue(4);
        nodes[8][3].setValue(2);
        nodes[8][6].setValue(6);


//        nodes[0][2].setValue(9);
//        nodes[0][3].setValue(4);
//        nodes[0][4].setValue(7);
//        nodes[0][5].setValue(2);
//        nodes[0][8].setValue(6);
//        nodes[1][0].setValue(6);
//        nodes[1][2].setValue(5);
//        nodes[1][7].setValue(4);
//        nodes[2][2].setValue(4);
//        nodes[2][4].setValue(9);
//        nodes[2][5].setValue(6);
//        nodes[3][0].setValue(4);
//        nodes[3][3].setValue(8);
//        nodes[3][4].setValue(5);
//        nodes[3][8].setValue(1);
//        nodes[4][1].setValue(5);
//        nodes[4][2].setValue(1);
//        nodes[4][5].setValue(7);
//        nodes[4][8].setValue(9);
//        nodes[5][4].setValue(1);
//        nodes[5][6].setValue(5);
//        nodes[5][8].setValue(8);
//        nodes[6][1].setValue(4);
//        nodes[6][2].setValue(8);
//        nodes[6][7].setValue(9);
//        nodes[6][8].setValue(5);
//        nodes[7][1].setValue(1);
//        nodes[7][3].setValue(9);
//        nodes[7][7].setValue(8);
//        nodes[8][1].setValue(9);
//        nodes[8][2].setValue(3);
//        nodes[8][3].setValue(7);
//        nodes[8][7].setValue(6);
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
        for(int i=0; i<base*base; i++){
            for(int j=0; j<base*base; j++){
                for (int k=0; k<3; k++){
                    for(int l=0; l<base*base; l++){
                        switch (k){
                            case 0:
                                if(l!=j)
                                    nodes[i][j].getConnectedNodes().add(nodes[i][l]);
                                break;
                            case 1:
                                if(l!=i)
                                    nodes[i][j].getConnectedNodes().add(nodes[l][j]);
                                break;
                            case 2:
                                int factorLine = i/base;
                                int facteurColumn = j/base;
                                if(i == l/base + (base * factorLine) && j == l%base + (base * facteurColumn))
                                    continue;
                                if(!nodes[i][j].getConnectedNodes().contains(nodes[l/base + (base * factorLine)] [l%base + (base * facteurColumn)]))
                                    nodes[i][j].getConnectedNodes().add(nodes[l/base + (base * factorLine)] [l%base + (base * facteurColumn)]);
                                break;
                        }
                    }
                }
            }
        }
    }

    public void verifyConnectedNode(){
        System.out.println(nodes[0][0].getConnectedNodes().size());
    }

    public void printSudoku(){
        String line = "";
        for (int i=0; i<base*base; i++){
            line = "| ";
            for(int j=0; j<base*base; j++){
                line += nodes[i][j].getValue() + " | ";
            }
            System.out.println(line);
        }
    }

    public void printSudokuPossibilities(Node[][] nodesPrint){
        String line = "";
        for (int i=0; i<base*base; i++){
            line = "| ";
            for(int j=0; j<base*base; j++){
                if(nodesPrint[i][j].getValue() == 0){
                    for(int m=0; m<nodesPrint[i][j].getPossibilities().size(); m++){
                        line += nodesPrint[i][j].getPossibilities().get(m) + ", ";
                    }
                    line += " | ";
                } else {
                    line += nodesPrint[i][j].getValue() + " | ";
                }
            }
            System.out.println(line);
        }
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
        for(int i=0; i<base*base; i++){
            for(int j=0; j<base*base; j++){
                for (int k=0; k<3; k++){
                    for(int l=0; l<base*base; l++){
                        switch (k){
                            case 0:
                                if(l!=j)
                                    copy[i][j].getConnectedNodes().add(copy[i][l]);
                                break;
                            case 1:
                                if(l!=i)
                                    copy[i][j].getConnectedNodes().add(copy[l][j]);
                                break;
                            case 2:
                                int factorLine = i/base;
                                int facteurColumn = j/base;
                                if(i == l/base + (base * factorLine) && j == l%base + (base * facteurColumn))
                                    continue;
                                if(!copy[i][j].getConnectedNodes().contains(copy[l/base + (base * factorLine)] [l%base + (base * facteurColumn)]))
                                    copy[i][j].getConnectedNodes().add(copy[l/base + (base * factorLine)] [l%base + (base * facteurColumn)]);
                                break;
                        }
                    }
                }
            }
        }
        return copy;
    }

    public void startResolution(){
        nodes = findSolution(cloneArrayNodes(nodes));
    }

    private Node[][] findSolution(Node[][] copyNodes){
        boolean test = false;
        Integer nodesAccumulationCheck = Integer.MAX_VALUE;
        while (true){
            Integer nodesAccumulation = 0;
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
                                        Node [][] tempCopy = findSolution(copyNodes2);
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

            printSudokuPossibilities(copyNodes);
            System.out.println("------------------");
        }
    }



}
