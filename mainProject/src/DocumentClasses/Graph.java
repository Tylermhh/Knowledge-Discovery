package DocumentClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {
    private HashSet<Integer> nodes;
    private HashMap<Integer, ArrayList<Integer>> adjacencyList;  // all incoming nodes
    private HashMap<Integer, Integer> outgoings; // 155 has 47
    private HashMap<Integer, Double> pageRankOld = new HashMap<>();
    private HashMap<Integer, Double> pageRankNew = new HashMap<>();

    public Graph(String filePath){
        this.nodes = new HashSet<>();
        this.adjacencyList = new HashMap<>();
        this.outgoings = new HashMap<>();

        parseGraphFile(filePath);
        double initialPageRank = (double) 1 / nodes.size();

        // set all the page ranks to 1/N  initially where N is number of nodes in graph
        for (Integer node : nodes){
            this.pageRankOld.put(node, initialPageRank);
        }
    }

    public void parseGraphFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] splitLine = line.split(",");
                Integer node1 = Integer.valueOf(splitLine[0]);
                Integer node2 = Integer.valueOf(splitLine[2]);

                // add them to list of nodes if not already in it
                this.nodes.add(node1);
                this.nodes.add(node2);

                // add one to the num of outgoing links for node1
                if (this.outgoings.containsKey(node1)){
                    this.outgoings.put(node1, this.outgoings.get(node1) + 1);
                } else{
                    this.outgoings.put(node1, 1);
                }

                // add the incoming node to the list of adjacency nodes for node2
                if (this.adjacencyList.containsKey(node2)){
                    if (!this.adjacencyList.get(node2).contains(node1)){
                        this.adjacencyList.get(node2).add(node1);
                    }
                } else {
                    ArrayList<Integer> newList = new ArrayList<>();
                    newList.add(node1);
                    this.adjacencyList.put(node2, newList);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Integer> getHighestNodes(double epsilon) {
        ArrayList<Integer> highestNodes = new ArrayList<>();
        int count = 0;
        int iterations = 0;

        double difference;
        do {
            performIteration();
            iterations++;
            difference = findDistance();
            // copy the new ranks over to old ranks hashmap to prepare for next iteration
            copyRanks();
        } while (difference > epsilon);
        System.out.println("num iterations: " + iterations);

        List<Map.Entry<Integer, Double>> list = new ArrayList<>(pageRankNew.entrySet());

        // Sort the list based on the values in descending order
        Collections.sort(list, (o1, o2) -> {
            // Sort in descending order
            return o2.getValue().compareTo(o1.getValue());
        });

        System.out.println(list);

        // Create a new LinkedHashMap to store the sorted entries
//        HashMap<Integer, Double> sortedMap = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            if (count >= 20) {
                break;
            }
//            sortedMap.put(entry.getKey(), entry.getValue());
            highestNodes.add(entry.getKey());
            count++;
        }

        return highestNodes;
    }

    //todo ask about L1 norm calculation
    public double findDistance(){
        double dist = 0;

//        for (Map.Entry<Integer, Double> old : this.pageRankOld.entrySet()){
////            if (!this.pageRankNew.containsKey(old.getKey())){
////                System.out.println("old and new page ranks keys dont match");
////                throw new RuntimeException();
////            }
//            double newRank = this.pageRankNew.get(old.getKey());
//            dist += Math.abs(newRank-old.getValue());
//        }

        for (Integer node : this.nodes){
            dist = dist + (Math.abs(this.pageRankOld.get(node) - this.pageRankNew.get(node)));
        }

        return dist;
    }

    public void performIteration(){
        double total = 0;
        for (Integer node : nodes){
            double incomingRanks = 0;
            // loop through all incoming nodes and add their page ranks up
            // nodes with no incoming get ignored and just keep getting set to "0.1 / num nodes"
            if (this.adjacencyList.containsKey(node)){
                // loop through all incoming nodes
                for (Integer incoming : this.adjacencyList.get(node)){
                    // add the (pagerank of incoming node divided by the number of outgoings that incomingNode had)
                    incomingRanks += this.pageRankOld.get(incoming) / this.outgoings.get(incoming);
                }
            }
            double newRank = 0.1 / nodes.size() + 0.9 * incomingRanks;
            total += newRank;
            this.pageRankNew.put(node, newRank);
        }

        // :3

        // normalize end results
        for (Map.Entry<Integer, Double> newRankEntry : this.pageRankNew.entrySet()){
            this.pageRankNew.put(newRankEntry.getKey(), newRankEntry.getValue() / total);
        }
    }

    public void copyRanks(){
        for (Map.Entry<Integer, Double> old : this.pageRankOld.entrySet()){
            if (!this.pageRankNew.containsKey(old.getKey())){
                System.out.println("old and new page ranks keys dont match");
                throw new RuntimeException();
            }
            // copy over rank from new into old
            this.pageRankOld.put(old.getKey(), this.pageRankNew.get(old.getKey()));
        }
    }


}
