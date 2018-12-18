import java.util.ArrayList;


public class InitializeNodesAndSets {

    public static ArrayList<Node> initializeNodes(){
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < 9; i++) {
            Node node = new Node(i);
            nodes.add(node);
        }
        return nodes;
    }

    public static ArrayList<Set> initializeSets(ArrayList<Node> nodes){
        ArrayList<Set> sets = new ArrayList<Set>();
        Integer[][] integers = new Integer[3][3];
        for (int i = 0, count = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                integers[i][j] = count;
                count++;
            }
        }
        for (int i = 0, count = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Set set = new Set();
                for (int k = 0; k < 3; k++) {
                    set.insert(integers[i][k]);
                    set.insert(integers[k][j]);
                }
                sets.add(set);
                nodes.get(count).setSet(set);
                count++;
            }
        }
        return sets;
    }
}
