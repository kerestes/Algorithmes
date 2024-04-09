package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.model.Node;
import fr.kerestes.sudoku.repositories.SudokuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class SudokuBoard {

    private int base;
    private SudokuRepository sudokuRepository;
    private Node[][] nodes;

    ForkJoinPool forkJoinPool = new ForkJoinPool();

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
                nodes[i][j] = new Node(possibilities, i, j);
            }
        }
        connectingNeighbors(nodes, base);
    }

    protected static void connectingNeighbors(Node[][] nodesToBeConnected, int base){
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
                                int zoneLine = l/base + (base * (i/base));
                                int zoneColumn = l%base + ( base * (j/base));
                                if(i == zoneLine && j == zoneColumn)
                                    continue;
                                if(!nodesToBeConnected[i][j].getConnectedNodes().contains(nodesToBeConnected[zoneLine] [zoneColumn]))
                                    nodesToBeConnected[i][j].getConnectedNodes().add(nodesToBeConnected[zoneLine] [zoneColumn]);
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

    public void startAutomaticGame(){
        Player player = new Player(base, nodes);
        nodes = forkJoinPool.invoke(player);
        if(nodes != null)
            System.out.println(toString());
        else
            System.out.println("Invalid Sudoku");
    }

    @Override
    public String toString(){
        String line = "";
        for (int i=0; i<base*base; i++){
            line += "| ";
            for(int j=0; j<base*base; j++){
                if(nodes[i][j].getValue() == 0){
                    line += "   | ";
                } else{
                    line += nodes[i][j].getValue() < 10 ? "0" + nodes[i][j].getValue() : nodes[i][j].getValue();
                    line += " | ";
                }
            }
            line += "\n";
        }
        return line;
    }
}
