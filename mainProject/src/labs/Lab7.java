package labs;

import DocumentClasses.ItemSet;
import DocumentClasses.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Lab7 {
    public static void main (String[] args){
        Matrix data = new Matrix(process("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\data.txt"));
        System.out.println(data);
        ArrayList<Integer> rows = new ArrayList<>();
        rows.add(0);
        rows.add(1);
        rows.add(2);
        System.out.println(data.findMostCommonValue(rows));
    }

    public static ArrayList<ArrayList<Integer>> process (String filePath){
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] splitLine = line.split(",");
                ArrayList<Integer> newEntry = new ArrayList<>();
                for (String elem : splitLine){
                    Double value = Double.parseDouble(elem);
                    Integer flooredVal = (int) Math.floor(value);
                    newEntry.add(flooredVal);
                }
                result.add(newEntry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    // TODO: finish function
    public static void printDecisionTree(int[][] data, ArrayList<Integer> attributes, ArrayList<Integer> rows, int level, double currentIGR){

    }
}
