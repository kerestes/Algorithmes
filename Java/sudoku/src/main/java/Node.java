import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
    private Integer value = 0;

    private List<Integer> possibilities = new ArrayList<>();
    private List<Node> connectedNodes = new ArrayList<>();

    public Node(){

    }
    public Node(List<Integer> possibilities){
        this.possibilities = new ArrayList<>(possibilities);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
        possibilities = Arrays.asList(value);
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public List<Integer> getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(List<Integer> possibilities) {
        this.possibilities = new ArrayList<>(possibilities);
    }

    public void setConnectedNodes(List<Node> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }
}
