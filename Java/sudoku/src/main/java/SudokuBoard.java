import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuBoard {

    private int base;
    private Node[][] nodes;

    public SudokuBoard(int base){
        this.base=base;
        nodes = new Node[base*base][base*base];
        createGraph();

        nodes[0][2].setValue(9);
        nodes[0][3].setValue(4);
        nodes[0][4].setValue(7);
        nodes[0][5].setValue(2);
        nodes[0][8].setValue(6);
        nodes[1][0].setValue(6);
        nodes[1][2].setValue(5);
        nodes[1][7].setValue(4);
        nodes[2][2].setValue(4);
        nodes[2][4].setValue(9);
        nodes[2][5].setValue(6);
        nodes[3][0].setValue(4);
        nodes[3][3].setValue(8);
        nodes[3][4].setValue(5);
        nodes[3][8].setValue(1);
        nodes[4][1].setValue(5);
        nodes[4][2].setValue(1);
        nodes[4][5].setValue(7);
        nodes[4][8].setValue(9);
        nodes[5][4].setValue(1);
        nodes[5][6].setValue(5);
        nodes[5][8].setValue(8);
        nodes[6][1].setValue(4);
        nodes[6][2].setValue(8);
        nodes[6][7].setValue(9);
        nodes[6][8].setValue(5);
        nodes[7][1].setValue(1);
        nodes[7][3].setValue(9);
        nodes[7][7].setValue(8);
        nodes[8][1].setValue(9);
        nodes[8][2].setValue(3);
        nodes[8][3].setValue(7);
        nodes[8][7].setValue(6);
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

    public void printSudokuPossibilities(){
        String line = "";
        for (int i=0; i<base*base; i++){
            line = "| ";
            for(int j=0; j<base*base; j++){
                if(nodes[i][j].getValue() == 0){
                    for(int m=0; m<nodes[i][j].getPossibilities().size(); m++){
                        line += nodes[i][j].getPossibilities().get(m) + ", ";
                    }
                    line += " | ";
                } else {
                    line += nodes[i][j].getValue() + " | ";
                }
            }
            System.out.println(line);
        }
    }

    public void findSolution(){
        int incompleteCompte = Integer.MAX_VALUE;
        boolean test = false;
        while (true){
            List<Node> incomplete = new ArrayList<>();
            for(int i=0; i<base*base; i++){
                for(int j=0; j<base*base; j++){
                    if (nodes[i][j].getValue() != 0)
                        continue;
                    for(int k=0; k<nodes[i][j].getConnectedNodes().size(); k++){
                        if(nodes[i][j].getConnectedNodes().get(k).getValue() != 0 && nodes[i][j].getPossibilities().contains(nodes[i][j].getConnectedNodes().get(k).getValue()))
                            nodes[i][j].getPossibilities().remove(nodes[i][j].getConnectedNodes().get(k).getValue());
                    }
                    if(nodes[i][j].getPossibilities().size() == 1){
                        nodes[i][j].setValue(nodes[i][j].getPossibilities().get(0));
                    } else {
                        incomplete.add(nodes[i][j]);
                    }
                }
            }
            if(incomplete.isEmpty()){
                break;
            } else if(incomplete.size() == incompleteCompte) {
                if(test){
                    System.out.println("There is not a unique solution without use probabilities");
                    break;
                } else {
                    test = verifyPossibilites();
                }
            } else{
                incompleteCompte = incomplete.size();
                test = false;
            }
            printSudokuPossibilities();
            System.out.println("");
        }
    }

    private boolean verifyPossibilites(){
        for (int i=0; i<base*base; i++){
            for(int j=0; j<base*base; j++){
                if(nodes[i][j].getPossibilities().size()==2){
                    for(int k=0; k<3 ; k++){
                        for(int m=0; m<base*base; m++){
                            switch (k){
                                case 0:
                                    if(k!=i){
                                        if(nodes[k][j].getPossibilities().size()==2 && nodes[i][j].getPossibilities().containsAll(nodes[k][j].getPossibilities())){
                                            for(int n=0; n<base*base; n++){
                                                if(n!= k && n!= i){
                                                    nodes[n][j].getPossibilities().removeAll(nodes[k][j].getPossibilities());
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case 1:
                                    if(k!=j){
                                        if(nodes[i][k].getPossibilities().size()==2 && nodes[i][j].getPossibilities().containsAll(nodes[i][k].getPossibilities())){
                                            for(int n=0; n<base*base; n++){
                                                if(n!= k && n!= j){
                                                    nodes[i][n].getPossibilities().removeAll(nodes[i][k].getPossibilities());
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
