package labs;

import DocumentClasses.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Lab4 {

//    public static HashSet<Integer> nodes = new HashSet<>();
//    public static HashMap<Integer, ArrayList<Integer>> adjacencyList = new HashMap<>();
//    public static HashMap<Integer, Integer> outgoings = new HashMap<>();

    public static void main (String[] args){
//        parseGraphFile("D:\\College\\Com Sci\\Junior Year\\CSC 466\\Labs\\mainProject\\src\\InputFiles\\human_judgement.txt");

//        Graph graph = new Graph("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\test_graph.txt");
//        ArrayList<Integer> mostPrestigeTest = graph.getHighestNodes(1000000);
//        System.out.println(mostPrestigeTest);
        Graph graph2 = new Graph("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\graph.txt");
        ArrayList<Integer> mostPrestige = graph2.getHighestNodes(0.001);
        System.out.println(mostPrestige);


    }
}
