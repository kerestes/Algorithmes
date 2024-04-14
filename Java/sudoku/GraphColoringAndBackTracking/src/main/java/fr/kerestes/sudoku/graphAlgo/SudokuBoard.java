package fr.kerestes.sudoku.graphAlgo;

import fr.kerestes.sudoku.model.Node;
import fr.kerestes.sudoku.repositories.SudokuRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuBoard {

    private int base;
    private SudokuRepository sudokuRepository;
    List<String> coordinates;
    private Node[][] nodes;

    public SudokuBoard(String filePath){
        sudokuRepository = new SudokuRepository(filePath);

        coordinates = sudokuRepository.readFile();

        this.base=Integer.parseInt(coordinates.get(0));
        nodes = new Node[base*base][base*base];
        createGraph();
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

    public void fillBoard(int line){
        List<Integer> coordinateNumbers = Arrays.stream(coordinates.get(line).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        for(int i=0; i<coordinateNumbers.size(); i++){
            if(coordinateNumbers.get(i) > 0)
                nodes[i/(base*base)][i%(base*base)].setValue(coordinateNumbers.get(i));
        }
    }

    public void startAutomaticGame(){
        int i;
        long max = Integer.MIN_VALUE, min = Integer.MAX_VALUE, avg = 0, time;
        Player player = new Player(base, nodes);
        for(i = 1; i<coordinates.size(); i++){
            fillBoard(i);
            Long start = System.nanoTime();
            nodes = player.play();
            Long end = System.nanoTime();
            if(verifyResult()) {
                time = (end - start);
                if(min > time)
                    min = time;
                if(max < time)
                    max = time;
                avg +=time;
                //System.out.println(this);
                System.out.println("\nProcess time: " + time + "\n");
            }else
                System.out.println("Invalid Game");
        }
        System.out.println("Lowest time: " + min);
        System.out.println("Highest time: " + max);
        System.out.println("Average: " + (avg/i));
    }

    private boolean verifyResult(){
        for(int i=0; i<base*base; i++){
            for(int j=0; j<base*base; j++){
                for(int k=0; k<3; k++){
                    for(int l=0; l<base*base; l++){
                        switch (k){
                            case 0:
                                if(l!=i)
                                    if(nodes[l][j].getValue() == nodes[i][j].getValue())
                                        return false;
                                break;
                            case 1:
                                if(l!=j)
                                    if(nodes[i][l].getValue() == nodes[i][j].getValue())
                                        return false;
                                break;
                            case 2:
                                int zoneLine = l/base + (base * (i/base));
                                int zoneColumn = l%base + ( base * (j/base));
                                if(i != zoneLine && j != zoneColumn)
                                    if(nodes[zoneLine][zoneColumn].getValue() == nodes[i][j].getValue())
                                        return false;
                                break;
                        }
                    }
                }
            }
        }
        return true;
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
